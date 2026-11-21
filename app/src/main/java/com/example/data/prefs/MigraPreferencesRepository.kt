package com.example.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.AppLang
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONObject

private val Context.migraDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "migrastaff_prefs"
)

data class CachedFxSnapshot(
    val ratesPerRub: Map<String, Double>,
    val updatedAtLabel: String
)

class MigraPreferencesRepository(private val context: Context) {

    private object Keys {
        val LANG = stringPreferencesKey("app_lang")
        val FX_JSON = stringPreferencesKey("fx_cache_json")
        val FX_TIME = stringPreferencesKey("fx_cache_time")
        val UNLOCKED_WORKERS = stringSetPreferencesKey("unlocked_worker_ids")
        val APPLIED_VACANCIES = stringSetPreferencesKey("applied_vacancy_ids")
        val LOCAL_FAVORITES = stringSetPreferencesKey("local_favorite_worker_ids")
    }

    val languageFlow: Flow<AppLang> = context.migraDataStore.data.map { prefs ->
        if (prefs[Keys.LANG] == AppLang.RU.code) AppLang.RU else AppLang.EN
    }

    val unlockedWorkerIdsFlow: Flow<Set<Int>> = context.migraDataStore.data.map { prefs ->
        prefs[Keys.UNLOCKED_WORKERS].orEmpty().mapNotNull { it.toIntOrNull() }.toSet()
    }

    val appliedVacancyIdsFlow: Flow<Set<Int>> = context.migraDataStore.data.map { prefs ->
        prefs[Keys.APPLIED_VACANCIES].orEmpty().mapNotNull { it.toIntOrNull() }.toSet()
    }

    val localFavoritesFlow: Flow<Set<Int>> = context.migraDataStore.data.map { prefs ->
        prefs[Keys.LOCAL_FAVORITES].orEmpty().mapNotNull { it.toIntOrNull() }.toSet()
    }

    val cachedFxFlow: Flow<CachedFxSnapshot?> = context.migraDataStore.data.map { prefs ->
        val raw = prefs[Keys.FX_JSON] ?: return@map null
        val time = prefs[Keys.FX_TIME].orEmpty()
        runCatching {
            val obj = JSONObject(raw)
            val parsed = mutableMapOf<String, Double>()
            obj.keys().forEach { key ->
                val value = obj.optDouble(key, Double.NaN)
                if (value.isFinite() && value > 0.0) {
                    parsed[key] = value
                }
            }
            if (parsed.isEmpty()) null else CachedFxSnapshot(parsed, time)
        }.getOrNull()
    }

    suspend fun setLanguage(lang: AppLang) {
        context.migraDataStore.edit { prefs ->
            prefs[Keys.LANG] = lang.code
        }
    }

    suspend fun saveFxCache(ratesPerRub: Map<String, Double>, timeLabel: String) {
        val json = JSONObject().apply {
            ratesPerRub.forEach { (k, v) -> put(k, v) }
        }.toString()
        context.migraDataStore.edit { prefs ->
            prefs[Keys.FX_JSON] = json
            prefs[Keys.FX_TIME] = timeLabel
        }
    }

    suspend fun toggleLocalFavorite(workerId: Int) {
        val key = workerId.toString()
        context.migraDataStore.edit { prefs ->
            val current = prefs[Keys.LOCAL_FAVORITES].orEmpty().toMutableSet()
            if (current.contains(key)) current.remove(key) else current.add(key)
            prefs[Keys.LOCAL_FAVORITES] = current
        }
    }

    suspend fun markWorkerUnlocked(workerId: Int) {
        context.migraDataStore.edit { prefs ->
            val current = prefs[Keys.UNLOCKED_WORKERS].orEmpty().toMutableSet()
            current.add(workerId.toString())
            prefs[Keys.UNLOCKED_WORKERS] = current
        }
    }

    suspend fun markVacancyApplied(vacancyId: Int) {
        context.migraDataStore.edit { prefs ->
            val current = prefs[Keys.APPLIED_VACANCIES].orEmpty().toMutableSet()
            current.add(vacancyId.toString())
            prefs[Keys.APPLIED_VACANCIES] = current
        }
    }
}
