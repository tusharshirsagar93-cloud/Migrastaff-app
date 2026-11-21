package com.example.data.fx

import com.example.data.MigraCatalogData
import com.example.data.prefs.MigraPreferencesRepository
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs

@JsonClass(generateAdapter = true)
data class ErApiResponse(
    val result: String? = null,
    val rates: Map<String, Double>? = null
)

@JsonClass(generateAdapter = true)
data class FawazRubResponse(
    val date: String? = null,
    val rub: Map<String, Double>? = null
)

interface ErApiService {
    @GET("v6/latest/RUB")
    suspend fun getLatestRub(): ErApiResponse
}

interface FawazCurrencyApiService {
    @GET("npm/@fawazahmed0/currency-api@latest/v1/currencies/rub.json")
    suspend fun getLatestRub(): FawazRubResponse
}

data class FxFetchOutcome(
    val ratesPerRub: Map<String, Double>,
    val isLive: Boolean,
    val updatedAtLabel: String
)

class FxRepository(
    private val prefsRepo: MigraPreferencesRepository? = null
) {
    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttp: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    private val erApi: ErApiService = Retrofit.Builder()
        .baseUrl("https://open.er-api.com/")
        .client(okHttp)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(ErApiService::class.java)

    private val fawazApi: FawazCurrencyApiService = Retrofit.Builder()
        .baseUrl("https://cdn.jsdelivr.net/")
        .client(okHttp)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(FawazCurrencyApiService::class.java)

    suspend fun fetchLiveRates(): FxFetchOutcome = withContext(Dispatchers.IO) {
        val baseline = MigraCatalogData.defaultRatesPerRub().toMutableMap()
        val timeFmt = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        // 1. Primary endpoint: open.er-api.com
        val primaryRates = runCatching { erApi.getLatestRub().rates }.getOrNull()
        if (!primaryRates.isNullOrEmpty() && (primaryRates["INR"] ?: 0.0) > 0.0) {
            val merged = mergeKnownCurrencies(baseline, primaryRates)
            val ts = timeFmt.format(Date())
            prefsRepo?.saveFxCache(merged, ts)
            return@withContext FxFetchOutcome(merged, isLive = true, updatedAtLabel = ts)
        }

        // 2. Fallback CDN endpoint: @fawazahmed0/currency-api
        val fallbackRates = runCatching { fawazApi.getLatestRub().rub }.getOrNull()
        if (!fallbackRates.isNullOrEmpty() && (fallbackRates["inr"] ?: 0.0) > 0.0) {
            val upper = fallbackRates.mapKeys { it.key.uppercase(Locale.US) }
            val merged = mergeKnownCurrencies(baseline, upper)
            val ts = timeFmt.format(Date())
            prefsRepo?.saveFxCache(merged, ts)
            return@withContext FxFetchOutcome(merged, isLive = true, updatedAtLabel = ts)
        }

        FxFetchOutcome(baseline, isLive = false, updatedAtLabel = "")
    }

    private fun mergeKnownCurrencies(
        baseline: MutableMap<String, Double>,
        incoming: Map<String, Double>
    ): Map<String, Double> {
        MigraCatalogData.fxCurrencies.forEach { c ->
            val v = incoming[c.code]
            if (v != null && v.isFinite() && v > 0.0) {
                baseline[c.code] = v
            }
        }
        baseline["RUB"] = 1.0
        return baseline
    }

    companion object {
        fun getCrossRate(fromCode: String, toCode: String, ratesPerRub: Map<String, Double>): Double {
            val rFrom = ratesPerRub[fromCode] ?: 1.0
            val rTo = ratesPerRub[toCode] ?: 1.0
            if (rFrom <= 0.0) return 1.0
            return rTo / rFrom
        }

        fun formatFxMoney(value: Double, currencyCode: String): String {
            if (!value.isFinite()) return "0.00"
            val zeroDecimals = (currencyCode == "UZS" || currencyCode == "VND") && abs(value) >= 1000.0
            return if (zeroDecimals) {
                String.format(Locale.US, "%,.0f", value)
            } else {
                String.format(Locale.US, "%,.2f", value)
            }
        }

        fun computeRubPerUsd(ratesPerRub: Map<String, Double>): Double {
            val usdPerRub = ratesPerRub["USD"] ?: 0.01081
            return if (usdPerRub > 0.0) 1.0 / usdPerRub else 92.5
        }
    }
}
