package com.example.data.firebase

import android.content.Context
import com.example.R
import com.example.data.MigraCatalogData
import com.example.model.CandidateProfile
import com.example.model.CryptoPaymentReceipt
import com.example.model.EmployerAccount
import com.example.model.FavoriteWorker
import com.example.model.JobApplication
import com.example.model.JobVacancy
import com.example.model.SupportMessage
import com.example.model.WorkerProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlin.math.abs

sealed interface StreamResult<out T> {
    data object Loading : StreamResult<Nothing>
    data class Success<T>(val data: T) : StreamResult<T>
    data class Error(val message: String, val cause: Throwable) : StreamResult<Nothing>
}

class MigraFirestoreRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    constructor(context: Context) : this(
        db = FirebaseFirestore.getInstance(context.getString(R.string.firestore_database_id)),
        auth = FirebaseAuth.getInstance()
    )

    private fun requireUserId(): String =
        auth.currentUser?.uid ?: throw IllegalStateException("User must be signed in.")

    private fun profileDocRef(uid: String) =
        db.collection("candidates").document(uid)

    private fun favoritesColRef(uid: String) =
        db.collection("users").document(uid).collection("favorites")

    private fun messagesColRef(uid: String) =
        db.collection("candidates").document(uid).collection("messages")

    private fun paymentsColRef(uid: String) =
        db.collection("users").document(uid).collection("crypto_payments")

    fun observeLiveJobs(): Flow<List<JobVacancy>> {
        return db.collection("jobs")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .snapshots()
            .map { snap -> snap.documents.mapNotNull { mapJobDoc(it) } }
            .catch { error ->
                if (error is CancellationException) throw error
                emit(emptyList())
            }
    }

    private fun mapJobDoc(doc: DocumentSnapshot): JobVacancy? {
        val titleEn = doc.getString("title")?.trim().orEmpty()
        if (titleEn.isEmpty()) return null
        val titleRu = doc.getString("titleRu")?.trim()?.ifEmpty { titleEn } ?: titleEn
        val cityEn = doc.getString("location")?.trim()?.ifEmpty { "Russia" } ?: "Russia"
        val cityRu = doc.getString("locationRu")?.trim()?.ifEmpty { cityEn } ?: cityEn
        val salaryStr = doc.getString("salary")?.trim().orEmpty()
        val nums = Regex("\\d+").findAll(salaryStr.replace(",", "").replace(" ", ""))
            .mapNotNull { it.value.toIntOrNull() }
            .toList()
        val salMin = nums.firstOrNull() ?: 60000
        val salMax = nums.getOrNull(1) ?: salMin
        val cat = inferCategory(titleEn)
        val stableId = abs(doc.id.hashCode()).let { if (it == 0) 1 else it }

        return JobVacancy(
            id = stableId,
            titleEn = titleEn,
            titleRu = titleRu,
            category = cat,
            salaryMinRub = salMin,
            salaryMaxRub = salMax,
            employerEn = "MigraStaff Direct",
            employerRu = "МиграСтафф Прямой найм",
            verifiedEmployer = true,
            rating = 4.9,
            reviewCount = 24,
            cityEn = cityEn,
            cityRu = cityRu,
            visaCountries = MigraCatalogData.countries.map { it.code },
            experienceEn = "1+ yrs",
            experienceRu = "от 1 года",
            payScheduleEn = "Monthly / Direct",
            payScheduleRu = "Ежемесячно",
            responseSpeedEn = "Responds within 1 day",
            responseSpeedRu = "Ответ в течение 1 дня",
            perkEn = "Direct Official Contract",
            perkRu = "Прямой официальный договор",
            postedEn = "Live",
            postedRu = "Актуально",
            activeViewers = 12,
            descEn = "$titleEn in $cityEn · Salary: $salaryStr. Apply directly through MigraStaff Director.",
            descRu = "$titleRu ($cityRu) · Зарплата: $salaryStr. Отклик напрямую через директора MigraStaff.",
            hrPhone = "+7 912 850-03-33",
            hrEmail = "migrapro@imap.mail.ru",
            docId = doc.id
        )
    }

    private fun inferCategory(title: String): String {
        val low = title.lowercase()
        return when {
            "weld" in low -> "Welder"
            "cook" in low || "chef" in low -> "Cook"
            "driv" in low || "truck" in low -> "Driver"
            "nurs" in low -> "Nurse"
            "construct" in low || "plumb" in low -> "Construction"
            "electr" in low -> "Electrician"
            "warehous" in low || "pack" in low -> "Warehouse"
            "mason" in low -> "Mason"
            "house" in low || "clean" in low -> "Housekeeper"
            "mechan" in low -> "Mechanic"
            "it" in low || "support" in low -> "IT Support"
            "farm" in low -> "Farmer"
            else -> "Warehouse"
        }
    }

    suspend fun resolveEmailForLogin(rawInput: String): String {
        val clean = rawInput.trim()
        if (clean.contains("@") && !clean.startsWith("@")) return clean.lowercase()
        val uname = clean.removePrefix("@").trim().lowercase()
        if (uname.isEmpty()) return clean
        val doc = db.collection("usernames").document(uname).get().await()
        val email = doc.getString("email")?.trim()?.lowercase().orEmpty()
        return email.ifEmpty { clean }
    }

    fun observeProfile(uid: String): Flow<StreamResult<CandidateProfile?>> {
        val path = "candidates/$uid"
        return profileDocRef(uid)
            .snapshots()
            .map<DocumentSnapshot, StreamResult<CandidateProfile?>> { doc ->
                if (!doc.exists()) {
                    StreamResult.Success(null)
                } else {
                    val expVal = when (val raw = doc.get("experienceYears")) {
                        is Number -> raw.toInt()
                        is String -> raw.filter { it.isDigit() }.toIntOrNull() ?: 1
                        else -> 1
                    }
                    val salVal = when (val raw = doc.get("expectedSalary")) {
                        is Number -> raw.toInt()
                        is String -> raw.filter { it.isDigit() }.toIntOrNull() ?: 60000
                        else -> 60000
                    }
                    val isVer = doc.getBoolean("verified") == true ||
                        doc.getString("verStatus")?.equals("verified", ignoreCase = true) == true
                    val photos = (doc.get("workPhotos") as? List<*>)?.size ?: 0
                    StreamResult.Success(
                        CandidateProfile(
                            id = doc.id,
                            userId = doc.getString("uid") ?: uid,
                            username = doc.getString("username").orEmpty(),
                            surname = doc.getString("surname").orEmpty(),
                            firstName = doc.getString("firstName").orEmpty(),
                            countryCode = doc.getString("countryCode")?.ifEmpty { "IN" } ?: "IN",
                            profession = doc.getString("profession")?.ifEmpty { "Warehouse Worker" } ?: "Warehouse Worker",
                            experienceYears = expVal,
                            expectedSalary = salVal,
                            languages = doc.getString("languages").orEmpty(),
                            skills = doc.getString("skills").orEmpty(),
                            about = doc.getString("about").orEmpty(),
                            phone = doc.getString("phone").orEmpty(),
                            dateOfBirth = doc.getString("dateOfBirth").orEmpty(),
                            verStatus = if (isVer) "verified" else "pending",
                            photoCount = photos,
                            banned = doc.getBoolean("banned") == true,
                            createdAt = doc.getTimestamp("createdAt"),
                            updatedAt = doc.getTimestamp("updatedAt")
                        )
                    )
                }
            }
            .catch { error ->
                if (error is CancellationException) throw error
                val logged = FirestoreErrorHandler.handleFirestoreError(error, OperationType.GET, path)
                emit(StreamResult.Error(error.localizedMessage ?: "Failed to load profile", logged))
            }
    }

    suspend fun saveCandidateProfile(profile: CandidateProfile): Result<Unit> {
        val uid = runCatching { requireUserId() }.getOrElse { return Result.failure(it) }
        val path = "candidates/$uid"
        return try {
            val docRef = profileDocRef(uid)
            val existing = docRef.get().await()
            val email = auth.currentUser?.email.orEmpty()
            val fullName = "${profile.surname.trim()} ${profile.firstName.trim()}".trim()
            val countryName = MigraCatalogData.countries
                .firstOrNull { it.code.equals(profile.countryCode, ignoreCase = true) }
                ?.nameEn ?: profile.countryCode
            val data = hashMapOf<String, Any>(
                "uid" to uid,
                "username" to profile.username.trim().lowercase(),
                "surname" to profile.surname.trim(),
                "firstName" to profile.firstName.trim(),
                "fullName" to fullName,
                "email" to email,
                "countryCode" to profile.countryCode,
                "countryName" to countryName,
                "profession" to profile.profession,
                "experienceYears" to profile.experienceYears.toString(),
                "expectedSalary" to profile.expectedSalary.toString(),
                "languages" to profile.languages.trim(),
                "skills" to profile.skills.trim(),
                "about" to profile.about.trim(),
                "phone" to profile.phone.trim(),
                "dateOfBirth" to profile.dateOfBirth.trim(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            if (!existing.exists()) {
                data["verified"] = false
                data["createdAt"] = FieldValue.serverTimestamp()
            }
            docRef.set(data, SetOptions.merge()).await()
            val uname = profile.username.trim().lowercase()
            if (uname.length >= 3 && email.isNotEmpty()) {
                runCatching {
                    db.collection("usernames").document(uname)
                        .set(mapOf("uid" to uid, "email" to email.lowercase()), SetOptions.merge())
                        .await()
                }
            }
            Result.success(Unit)
        } catch (error: Exception) {
            if (error is CancellationException) throw error
            Result.failure(FirestoreErrorHandler.handleFirestoreError(error, OperationType.WRITE, path))
        }
    }

    fun observeFavorites(uid: String): Flow<StreamResult<List<FavoriteWorker>>> {
        val path = "users/$uid/favorites"
        return favoritesColRef(uid)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .dataObjects<FavoriteWorker>()
            .map<List<FavoriteWorker>, StreamResult<List<FavoriteWorker>>> { StreamResult.Success(it) }
            .catch { error ->
                if (error is CancellationException) throw error
                val logged = FirestoreErrorHandler.handleFirestoreError(error, OperationType.LIST, path)
                emit(StreamResult.Error(error.localizedMessage ?: "Failed to load favorites", logged))
            }
    }

    suspend fun toggleFavorite(worker: WorkerProfile, currentlySaved: Boolean): Result<Unit> {
        val uid = runCatching { requireUserId() }.getOrElse { return Result.failure(it) }
        val wid = worker.id.toString()
        val path = "users/$uid/favorites/$wid"
        return try {
            val docRef = favoritesColRef(uid).document(wid)
            if (currentlySaved) {
                docRef.delete().await()
            } else {
                val fav = FavoriteWorker(
                    userId = uid,
                    workerId = wid,
                    workerName = worker.nameEn,
                    profession = worker.profession,
                    countryCode = worker.countryCode,
                    salaryRub = worker.salaryRub
                )
                docRef.set(fav.toCreateMap()).await()
            }
            Result.success(Unit)
        } catch (error: Exception) {
            if (error is CancellationException) throw error
            Result.failure(
                FirestoreErrorHandler.handleFirestoreError(
                    error,
                    if (currentlySaved) OperationType.DELETE else OperationType.CREATE,
                    path
                )
            )
        }
    }

    fun observeMessages(uid: String): Flow<StreamResult<List<SupportMessage>>> {
        val path = "candidates/$uid/messages"
        return messagesColRef(uid)
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .snapshots()
            .map<com.google.firebase.firestore.QuerySnapshot, StreamResult<List<SupportMessage>>> { snap ->
                val list = snap.documents.map { d ->
                    val sender = d.getString("sender") ?: d.getString("senderRole") ?: "candidate"
                    val tEn = d.getString("textEn")?.trim().orEmpty()
                    val tRu = d.getString("textRu")?.trim().orEmpty()
                    val rawOrig = listOf(d.getString("textOriginal"), d.getString("origText"), d.getString("text"), tEn, tRu).firstOrNull { !it.isNullOrBlank() }?.trim().orEmpty()
                    val orig = if (sender == "director" && tEn.isNotEmpty()) tEn else rawOrig
                    val trans = if (sender == "director") tRu.ifEmpty { rawOrig } else tRu.ifEmpty { d.getString("textTranslated")?.trim().orEmpty() }.ifEmpty { rawOrig }
                    SupportMessage(
                        id = d.id,
                        userId = uid,
                        senderRole = sender,
                        textOriginal = orig,
                        textTranslated = trans,
                        lang = d.getString("lang") ?: "en",
                        createdAt = d.getTimestamp("createdAt")
                    )
                }
                StreamResult.Success(list)
            }
            .catch { error ->
                if (error is CancellationException) throw error
                val logged = FirestoreErrorHandler.handleFirestoreError(error, OperationType.LIST, path)
                emit(StreamResult.Error(error.localizedMessage ?: "Failed to load messages", logged))
            }
    }

    private fun translateTextSync(text: String, targetLang: String): String {
        val clean = text.trim()
        if (clean.isEmpty()) return ""
        val q = java.net.URLEncoder.encode(clean, "UTF-8")
        // 1. Google Translate with User-Agent
        runCatching {
            val conn = (java.net.URL("https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=$targetLang&dt=t&q=$q").openConnection() as java.net.HttpURLConnection).apply {
                connectTimeout = 5000
                readTimeout = 5000
                setRequestProperty("User-Agent", "Mozilla/5.0")
            }
            val body = conn.inputStream.bufferedReader().use { it.readText() }
            val arr = org.json.JSONArray(body).getJSONArray(0)
            val res = buildString { for (i in 0 until arr.length()) append(arr.getJSONArray(i).getString(0)) }.trim()
            if (res.isNotEmpty() && !res.equals(clean, ignoreCase = true)) return res
        }
        // 2. Fallback: MyMemory API
        val pair = if (targetLang == "ru") "en|ru" else "ru|en"
        runCatching {
            val conn = (java.net.URL("https://api.mymemory.translated.net/get?q=$q&langpair=$pair").openConnection() as java.net.HttpURLConnection).apply {
                connectTimeout = 5000
                readTimeout = 5000
                setRequestProperty("User-Agent", "Mozilla/5.0")
            }
            val body = conn.inputStream.bufferedReader().use { it.readText() }
            val res = org.json.JSONObject(body).optJSONObject("responseData")?.optString("translatedText", "")?.trim().orEmpty()
            if (res.isNotEmpty() && !res.equals(clean, ignoreCase = true)) return res
        }
        return clean
    }

    suspend fun sendMessage(text: String, translated: String, langCode: String): Result<Unit> {
        val uid = runCatching { requireUserId() }.getOrElse { return Result.failure(it) }
        val path = "candidates/$uid/messages"
        return try {
            val clean = text.trim()
            val isRuText = clean.any { it in '\u0400'..'\u04FF' }
            val target = if (isRuText) "en" else "ru"
            val trans = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                translateTextSync(clean, target)
            }
            val textRu = if (isRuText) clean else trans
            val textEn = if (isRuText) trans else clean
            val msgMap = hashMapOf<String, Any>(
                "sender" to "candidate",
                "senderRole" to "candidate",
                "text" to clean,
                "textOrig" to clean,
                "origText" to clean,
                "textOriginal" to clean,
                "textTranslated" to trans,
                "textEn" to textEn,
                "textRu" to textRu,
                "lang" to if (isRuText) "ru" else "en",
                "createdAt" to FieldValue.serverTimestamp()
            )
            messagesColRef(uid).add(msgMap).await()
            Result.success(Unit)
        } catch (error: Exception) {
            if (error is CancellationException) throw error
            Result.failure(FirestoreErrorHandler.handleFirestoreError(error, OperationType.CREATE, path))
        }
    }

    fun observeCryptoReceipts(uid: String): Flow<StreamResult<List<CryptoPaymentReceipt>>> {
        val path = "users/$uid/crypto_payments"
        return paymentsColRef(uid)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .dataObjects<CryptoPaymentReceipt>()
            .map<List<CryptoPaymentReceipt>, StreamResult<List<CryptoPaymentReceipt>>> {
                StreamResult.Success(it)
            }
            .catch { error ->
                if (error is CancellationException) throw error
                val logged = FirestoreErrorHandler.handleFirestoreError(error, OperationType.LIST, path)
                emit(StreamResult.Error(error.localizedMessage ?: "Failed to load receipts", logged))
            }
    }

    suspend fun submitCryptoReceipt(receipt: CryptoPaymentReceipt): Result<String> {
        val uid = runCatching { requireUserId() }.getOrElse { return Result.failure(it) }
        val path = "cryptoPayments/${receipt.refId}"
        return try {
            val payload = receipt.copy(userId = uid, status = "submitted")
            val map = payload.toCreateMap().toMutableMap()
            db.collection("cryptoPayments").document(payload.refId).set(map, SetOptions.merge()).await()
            paymentsColRef(uid).add(map).await()
            Result.success(payload.refId)
        } catch (error: Exception) {
            if (error is CancellationException) throw error
            Result.failure(FirestoreErrorHandler.handleFirestoreError(error, OperationType.CREATE, path))
        }
    }

    suspend fun applyToJob(vacancy: JobVacancy): Result<Unit> {
        val uid = runCatching { requireUserId() }.getOrElse { return Result.failure(it) }
        val jId = vacancy.docId.ifEmpty { vacancy.id.toString() }
        return try {
            val existing = db.collection("applications")
                .whereEqualTo("candidateUid", uid)
                .whereEqualTo("jobId", jId)
                .get()
                .await()
            if (!existing.isEmpty) return Result.success(Unit)
            val candDoc = runCatching { profileDocRef(uid).get().await() }.getOrNull()
            val cName = candDoc?.getString("fullName")?.takeIf { it.isNotBlank() }
                ?: auth.currentUser?.displayName?.takeIf { it.isNotBlank() }
                ?: auth.currentUser?.email?.substringBefore("@")
                ?: "Candidate"
            val cEmail = auth.currentUser?.email.orEmpty()
            val salStr = if (vacancy.salaryMinRub == vacancy.salaryMaxRub) {
                "${vacancy.salaryMinRub}"
            } else {
                "${vacancy.salaryMinRub} - ${vacancy.salaryMaxRub}"
            }
            val data = hashMapOf<String, Any>(
                "jobId" to jId,
                "jobTitle" to vacancy.titleEn,
                "location" to vacancy.cityEn,
                "salary" to salStr,
                "candidateUid" to uid,
                "candidateName" to cName,
                "candidateEmail" to cEmail,
                "status" to "pending",
                "createdAt" to FieldValue.serverTimestamp()
            )
            db.collection("applications").add(data).await()
            Result.success(Unit)
        } catch (error: Exception) {
            if (error is CancellationException) throw error
            Result.failure(FirestoreErrorHandler.handleFirestoreError(error, OperationType.CREATE, "applications"))
        }
    }

    fun observeMyApplications(uid: String): Flow<List<JobApplication>> {
        return db.collection("applications")
            .whereEqualTo("candidateUid", uid)
            .snapshots()
            .map { snap ->
                snap.documents.map { d ->
                    JobApplication(
                        id = d.id,
                        jobId = d.getString("jobId").orEmpty(),
                        jobTitle = d.getString("jobTitle").orEmpty(),
                        location = d.getString("location").orEmpty(),
                        salary = d.getString("salary").orEmpty(),
                        candidateUid = d.getString("candidateUid").orEmpty(),
                        candidateName = d.getString("candidateName").orEmpty(),
                        candidateEmail = d.getString("candidateEmail").orEmpty(),
                        status = d.getString("status")?.ifEmpty { "pending" } ?: "pending"
                    )
                }
            }
            .catch { error ->
                if (error is CancellationException) throw error
                emit(emptyList())
            }
    }

    fun observeAllApplications(): Flow<List<JobApplication>> {
        return db.collection("applications")
            .snapshots()
            .map { snap ->
                snap.documents.map { d ->
                    JobApplication(
                        id = d.id,
                        jobId = d.getString("jobId").orEmpty(),
                        jobTitle = d.getString("jobTitle").orEmpty(),
                        location = d.getString("location").orEmpty(),
                        salary = d.getString("salary").orEmpty(),
                        candidateUid = d.getString("candidateUid").orEmpty(),
                        candidateName = d.getString("candidateName").orEmpty(),
                        candidateEmail = d.getString("candidateEmail").orEmpty(),
                        status = d.getString("status")?.ifEmpty { "pending" } ?: "pending"
                    )
                }
            }
            .catch { error ->
                if (error is CancellationException) throw error
                emit(emptyList())
            }
    }

    suspend fun updateApplicationStatus(appId: String, status: String): Result<Unit> {
        return try {
            db.collection("applications").document(appId)
                .update(mapOf("status" to status, "updatedAt" to FieldValue.serverTimestamp()))
                .await()
            Result.success(Unit)
        } catch (error: Exception) {
            if (error is CancellationException) throw error
            Result.failure(error)
        }
    }

    fun observeAllEmployers(): Flow<List<EmployerAccount>> {
        return db.collection("employers")
            .snapshots()
            .map { snap ->
                snap.documents.map { d ->
                    EmployerAccount(
                        uid = d.id,
                        companyName = d.getString("companyName").orEmpty(),
                        city = d.getString("city").orEmpty(),
                        email = d.getString("email").orEmpty(),
                        status = d.getString("status")?.ifEmpty { "pending" } ?: "pending"
                    )
                }
            }
            .catch { error ->
                if (error is CancellationException) throw error
                emit(emptyList())
            }
    }

    suspend fun updateEmployerStatus(empUid: String, status: String): Result<Unit> {
        return try {
            db.collection("employers").document(empUid)
                .update(mapOf("status" to status, "updatedAt" to FieldValue.serverTimestamp()))
                .await()
            Result.success(Unit)
        } catch (error: Exception) {
            if (error is CancellationException) throw error
            Result.failure(error)
        }
    }

    fun observeEmployerProfile(uid: String): Flow<EmployerAccount?> {
        return db.collection("employers").document(uid)
            .snapshots()
            .map { d ->
                if (!d.exists()) null
                else EmployerAccount(
                    uid = d.id,
                    companyName = d.getString("companyName").orEmpty(),
                    city = d.getString("city").orEmpty(),
                    email = d.getString("email").orEmpty(),
                    status = d.getString("status")?.ifEmpty { "pending" } ?: "pending"
                )
            }
            .catch { error ->
                if (error is CancellationException) throw error
                emit(null)
            }
    }

    suspend fun registerEmployerProfile(uid: String, companyName: String, city: String, email: String): Result<Unit> {
        return try {
            val data = hashMapOf<String, Any>(
                "uid" to uid,
                "companyName" to companyName.trim(),
                "city" to city.trim().ifEmpty { "Moscow" },
                "email" to email.trim().lowercase(),
                "status" to "pending",
                "createdAt" to FieldValue.serverTimestamp()
            )
            db.collection("employers").document(uid).set(data, SetOptions.merge()).await()
            Result.success(Unit)
        } catch (error: Exception) {
            if (error is CancellationException) throw error
            Result.failure(error)
        }
    }

    suspend fun publishJob(
        title: String,
        location: String,
        salary: String,
        companyName: String,
        employerUid: String
    ): Result<Unit> {
        return try {
            val t = title.trim()
            val l = location.trim()
            val tRu = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                translateTextSync(t, "ru").ifEmpty { t }
            }
            val lRu = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                translateTextSync(l, "ru").ifEmpty { l }
            }
            val data = hashMapOf<String, Any>(
                "title" to t,
                "titleRu" to tRu,
                "location" to l,
                "locationRu" to lRu,
                "salary" to salary.trim(),
                "salaryRu" to salary.trim(),
                "companyName" to companyName,
                "employerUid" to employerUid,
                "createdAt" to FieldValue.serverTimestamp()
            )
            db.collection("jobs").add(data).await()
            Result.success(Unit)
        } catch (error: Exception) {
            if (error is CancellationException) throw error
            Result.failure(error)
        }
    }

    suspend fun deleteJob(jobDocId: String): Result<Unit> {
        return try {
            db.collection("jobs").document(jobDocId).delete().await()
            Result.success(Unit)
        } catch (error: Exception) {
            if (error is CancellationException) throw error
            Result.failure(error)
        }
    }


    fun observeAllCandidates(): Flow<List<CandidateProfile>> {
        return db.collection("candidates").snapshots().map { snap ->
            snap.documents.map { doc ->
                val expVal = when (val raw = doc.get("experienceYears")) {
                    is Number -> raw.toInt()
                    is String -> raw.filter { it.isDigit() }.toIntOrNull() ?: 1
                    else -> 1
                }
                val salVal = when (val raw = doc.get("expectedSalary")) {
                    is Number -> raw.toInt()
                    is String -> raw.filter { it.isDigit() }.toIntOrNull() ?: 60000
                    else -> 60000
                }
                val isVer = doc.getBoolean("verified") == true || doc.getString("verStatus")?.equals("verified", true) == true
                CandidateProfile(
                    id = doc.id, userId = doc.getString("uid") ?: doc.id,
                    username = doc.getString("username").orEmpty(),
                    surname = doc.getString("surname").orEmpty(),
                    firstName = doc.getString("firstName").orEmpty(),
                    countryCode = doc.getString("countryCode")?.ifEmpty { "IN" } ?: "IN",
                    profession = doc.getString("profession")?.ifEmpty { "Worker" } ?: "Worker",
                    experienceYears = expVal, expectedSalary = salVal,
                    phone = doc.getString("phone").orEmpty(),
                    verStatus = if (isVer) "verified" else "pending",
                    banned = doc.getBoolean("banned") == true
                )
            }
        }.catch { e -> if (e is CancellationException) throw e; emit(emptyList()) }
    }

    suspend fun setCandidateVerified(uid: String, verified: Boolean): Result<Unit> = try {
        db.collection("candidates").document(uid).update(mapOf("verified" to verified, "updatedAt" to FieldValue.serverTimestamp())).await()
        Result.success(Unit)
    } catch (e: Exception) { if (e is CancellationException) throw e; Result.failure(e) }

    suspend fun setCandidateBanned(uid: String, banned: Boolean): Result<Unit> = try {
        val m = hashMapOf<String, Any>("banned" to banned, "updatedAt" to FieldValue.serverTimestamp())
        if (banned) m["verified"] = false
        db.collection("candidates").document(uid).update(m).await()
        Result.success(Unit)
    } catch (e: Exception) { if (e is CancellationException) throw e; Result.failure(e) }

    suspend fun deleteCandidate(uid: String, username: String): Result<Unit> = try {
        db.collection("candidates").document(uid).delete().await()
        val u = username.trim().lowercase()
        if (u.isNotEmpty()) runCatching { db.collection("usernames").document(u).delete().await() }
        Result.success(Unit)
    } catch (e: Exception) { if (e is CancellationException) throw e; Result.failure(e) }

}
