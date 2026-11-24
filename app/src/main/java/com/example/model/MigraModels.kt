package com.example.model

import androidx.annotation.DrawableRes
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

enum class AppLang(val code: String, val label: String) {
    EN("en", "EN"),
    RU("ru", "RU")
}

enum class MainTab(val route: String) {
    SEARCH("search"),
    FX("fx"),
    CRYPTO("crypto"),
    ABOUT("about"),
    PROFILE("profile")
}

enum class SortOrder {
    RELEVANCE,
    SALARY_ASC,
    SALARY_DESC,
    EXPERIENCE_DESC
}

// ---------- Firestore Document Models ----------

@IgnoreExtraProperties
data class CandidateProfile(
    @DocumentId val id: String = "",
    val userId: String = "",
    val username: String = "",
    val surname: String = "",
    val firstName: String = "",
    val countryCode: String = "UZ",
    val profession: String = "Welder",
    val experienceYears: Int = 1,
    val expectedSalary: Int = 60000,
    val languages: String = "",
    val skills: String = "",
    val about: String = "",
    val phone: String = "",
    val dateOfBirth: String = "",
    val verStatus: String = "pending",
    val banned: Boolean = false,
    val photoCount: Int = 0,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    fun toCreateMap(): Map<String, Any> = mapOf(
        "userId" to userId,
        "username" to username,
        "surname" to surname,
        "firstName" to firstName,
        "countryCode" to countryCode,
        "profession" to profession,
        "experienceYears" to experienceYears,
        "expectedSalary" to expectedSalary,
        "languages" to languages,
        "skills" to skills,
        "about" to about,
        "phone" to phone,
        "dateOfBirth" to dateOfBirth,
        "verStatus" to "pending",
        "photoCount" to photoCount,
        "createdAt" to FieldValue.serverTimestamp(),
        "updatedAt" to FieldValue.serverTimestamp()
    )

    fun toUpdateMap(): Map<String, Any> = mapOf(
        "username" to username,
        "surname" to surname,
        "firstName" to firstName,
        "countryCode" to countryCode,
        "profession" to profession,
        "experienceYears" to experienceYears,
        "expectedSalary" to expectedSalary,
        "languages" to languages,
        "skills" to skills,
        "about" to about,
        "phone" to phone,
        "dateOfBirth" to dateOfBirth,
        "photoCount" to photoCount,
        "updatedAt" to FieldValue.serverTimestamp()
    )
}

@IgnoreExtraProperties
data class FavoriteWorker(
    @DocumentId val id: String = "",
    val userId: String = "",
    val workerId: String = "",
    val workerName: String = "",
    val profession: String = "",
    val countryCode: String = "",
    val salaryRub: Int = 0,
    @ServerTimestamp val createdAt: Timestamp? = null
) {
    fun toCreateMap(): Map<String, Any> = mapOf(
        "userId" to userId,
        "workerId" to workerId,
        "workerName" to workerName,
        "profession" to profession,
        "countryCode" to countryCode,
        "salaryRub" to salaryRub,
        "createdAt" to FieldValue.serverTimestamp()
    )
}

@IgnoreExtraProperties
data class SupportMessage(
    @DocumentId val id: String = "",
    val userId: String = "",
    val senderRole: String = "candidate",
    val textOriginal: String = "",
    val textTranslated: String = "",
    val lang: String = "en",
    @ServerTimestamp val createdAt: Timestamp? = null
) {
    fun toCreateMap(): Map<String, Any> = mapOf(
        "userId" to userId,
        "senderRole" to "candidate",
        "textOriginal" to textOriginal,
        "textTranslated" to textTranslated,
        "lang" to lang,
        "createdAt" to FieldValue.serverTimestamp()
    )
}

@IgnoreExtraProperties
data class CryptoPaymentReceipt(
    @DocumentId val id: String = "",
    val userId: String = "",
    val refId: String = "",
    val planKey: String = "pro",
    val coinKey: String = "USDT_TRC20",
    val amountCrypto: String = "0.00 USDT",
    val amountRub: Double = 499.0,
    val txHash: String = "",
    val payerContact: String = "",
    val status: String = "submitted",
    @ServerTimestamp val createdAt: Timestamp? = null
) {
    fun toCreateMap(): Map<String, Any> = mapOf(
        "userId" to userId,
        "refId" to refId,
        "planKey" to planKey,
        "coinKey" to coinKey,
        "amountCrypto" to amountCrypto,
        "amountRub" to amountRub,
        "txHash" to txHash,
        "payerContact" to payerContact,
        "status" to "submitted",
        "createdAt" to FieldValue.serverTimestamp()
    )
}

// ---------- Directory, FX & Crypto Domain Models ----------

data class CountryInfo(
    val code: String,
    val nameEn: String,
    val nameRu: String,
    val flag: String
)

data class WorkerProfile(
    val id: Int,
    val nameEn: String,
    val nameRu: String,
    val countryCode: String,
    val profession: String,
    val expYears: Int,
    val salaryRub: Int,
    val skills: List<String>,
    val languages: List<String>,
    val verified: Boolean,
    val bioEn: String,
    val bioRu: String,
    val activeViewers: Int,
    val cityEn: String,
    val cityRu: String,
    val responseSpeedEn: String,
    val responseSpeedRu: String
)

data class JobVacancy(
    val id: Int,
    val titleEn: String,
    val titleRu: String,
    val category: String,
    val salaryMinRub: Int,
    val salaryMaxRub: Int,
    val employerEn: String,
    val employerRu: String,
    val verifiedEmployer: Boolean,
    val rating: Double,
    val reviewCount: Int,
    val cityEn: String,
    val cityRu: String,
    val visaCountries: List<String>,
    val experienceEn: String,
    val experienceRu: String,
    val payScheduleEn: String,
    val payScheduleRu: String,
    val responseSpeedEn: String,
    val responseSpeedRu: String,
    val perkEn: String,
    val perkRu: String,
    val postedEn: String,
    val postedRu: String,
    val activeViewers: Int,
    val descEn: String,
    val descRu: String,
    val hrPhone: String,
    val hrEmail: String,
    val docId: String = ""
)

data class ProfessionCategory(
    val key: String,
    val profEn: String,
    val count: Int
)

data class FxCurrency(
    val code: String,
    val flag: String,
    val nameEn: String,
    val nameRu: String,
    val fallbackPerRub: Double
)

data class FxPreset(
    val from: String,
    val to: String,
    val amount: Double,
    val label: String
)

data class CryptoPlan(
    val key: String,
    val nameEn: String,
    val nameRu: String,
    val descEn: String,
    val descRu: String,
    val rubAmount: Double,
    val popular: Boolean = false
)

data class CryptoCoin(
    val key: String,
    val symbol: String,
    val network: String,
    val usdRate: Double,
    val decimals: Int,
    val eta: String,
    val address: String
)

data class TeamMember(
    val nameEn: String,
    val nameRu: String,
    val roleEn: String,
    val roleRu: String,
    val initials: String,
    @DrawableRes val photoRes: Int,
    val bioEn: String,
    val bioRu: String
)

data class InfoTopic(
    val key: String,
    val titleEn: String,
    val titleRu: String,
    val bulletsEn: List<String>,
    val bulletsRu: List<String>
)

sealed interface AuthUiState {
    data object Loading : AuthUiState
    data object SignedOut : AuthUiState
    data class SignedIn(
        val uid: String,
        val displayName: String,
        val email: String,
        val isAutoSignedIn: Boolean = false
    ) : AuthUiState
}

data class JobApplication(
    val id: String = "",
    val jobId: String = "",
    val jobTitle: String = "",
    val location: String = "",
    val salary: String = "",
    val candidateUid: String = "",
    val candidateName: String = "",
    val candidateEmail: String = "",
    val status: String = "pending"
)

data class EmployerAccount(
    val uid: String = "",
    val companyName: String = "",
    val city: String = "",
    val email: String = "",
    val status: String = "pending"
)
