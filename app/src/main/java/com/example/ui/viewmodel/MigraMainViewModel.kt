package com.example.ui.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MigraCatalogData
import com.example.data.firebase.MigraFirestoreRepository
import com.example.data.firebase.StreamResult
import com.example.util.MigraNotificationHelper
import kotlinx.coroutines.tasks.await
import com.example.data.auth.GoogleAuthHelper
import com.example.data.auth.GoogleSignInResult
import com.example.data.fx.FxRepository
import com.example.data.prefs.MigraPreferencesRepository
import com.example.model.AppLang
import com.example.model.EmployerAccount
import com.example.model.JobApplication
import com.example.model.AuthUiState
import com.example.model.InfoTopic
import com.example.model.JobVacancy
import com.example.model.MainTab
import com.example.model.SortOrder
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

data class BrowseFilterState(
    val query: String = "",
    val selectedProfession: String = "",
    val selectedCountry: String = "",
    val verifiedOnly: Boolean = false,
    val savedOnly: Boolean = false,
    val sortOrder: SortOrder = SortOrder.RELEVANCE
)

data class FxUiState(
    val fromCode: String = "RUB",
    val toCode: String = "INR",
    val amountInput: String = "10000",
    val ratesPerRub: Map<String, Double> = MigraCatalogData.defaultRatesPerRub(),
    val isLive: Boolean = false,
    val isSyncing: Boolean = false,
    val updatedAtLabel: String = ""
) {
    val parsedAmount: Double get() = amountInput.toDoubleOrNull()?.takeIf { it >= 0.0 } ?: 0.0
    val crossRate: Double get() = FxRepository.getCrossRate(fromCode, toCode, ratesPerRub)
    val convertedAmount: Double get() = parsedAmount * crossRate
    val rubPerUsd: Double get() = FxRepository.computeRubPerUsd(ratesPerRub)
}

data class CryptoUiState(
    val selectedPlanKey: String = "pro",
    val selectedCoinKey: String = "USDT_TRC20",
    val customAmountInput: String = "5000",
    val customCurrency: String = "RUB",
    val payerContact: String = "",
    val txHash: String = "",
    val lastReceiptRef: String? = null,
    val isSubmitting: Boolean = false
)

class MigraMainViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsRepo = MigraPreferencesRepository(application)
    private val fxRepo = FxRepository(prefsRepo)
    private val authHelper = GoogleAuthHelper()
    private val firestoreRepo = MigraFirestoreRepository(application)

    val language: StateFlow<AppLang> = prefsRepo.languageFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppLang.EN)

    val localFavorites: StateFlow<Set<Int>> = prefsRepo.localFavoritesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())


    private val _activeTab = MutableStateFlow(MainTab.SEARCH)
    val activeTab: StateFlow<MainTab> = _activeTab.asStateFlow()

    private val _filterState = MutableStateFlow(BrowseFilterState())
    val filterState: StateFlow<BrowseFilterState> = _filterState.asStateFlow()

    private val _fxState = MutableStateFlow(FxUiState())
    val fxState: StateFlow<FxUiState> = _fxState.asStateFlow()

    private val _cryptoState = MutableStateFlow(CryptoUiState())
    val cryptoState: StateFlow<CryptoUiState> = _cryptoState.asStateFlow()

    private val _selectedVacancy = MutableStateFlow<JobVacancy?>(null)
    val selectedVacancy: StateFlow<JobVacancy?> = _selectedVacancy.asStateFlow()

    private val _selectedInfoTopic = MutableStateFlow<InfoTopic?>(null)
    val selectedInfoTopic: StateFlow<InfoTopic?> = _selectedInfoTopic.asStateFlow()

    private val _showFilterSheet = MutableStateFlow(false)
    val showFilterSheet: StateFlow<Boolean> = _showFilterSheet.asStateFlow()

    private val _authState = MutableStateFlow<AuthUiState>(AuthUiState.Loading)
    val authState: StateFlow<AuthUiState> = _authState.asStateFlow()

    private val _authErrorMessage = MutableStateFlow<String?>(null)
    val authErrorMessage: StateFlow<String?> = _authErrorMessage.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    val liveJobsFlow: StateFlow<List<JobVacancy>> = firestoreRepo.observeLiveJobs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _myApplications = MutableStateFlow<List<JobApplication>>(emptyList())
    val myApplications: StateFlow<List<JobApplication>> = _myApplications.asStateFlow()

    private val _employerProfile = MutableStateFlow<EmployerAccount?>(null)
    val employerProfile: StateFlow<EmployerAccount?> = _employerProfile.asStateFlow()

    private val _allApplications = MutableStateFlow<List<JobApplication>>(emptyList())
    val allApplications: StateFlow<List<JobApplication>> = _allApplications.asStateFlow()

    private val _allEmployers = MutableStateFlow<List<EmployerAccount>>(emptyList())
    val allEmployers: StateFlow<List<EmployerAccount>> = _allEmployers.asStateFlow()

    private var userStreamsJob: Job? = null

    val appliedVacancyIds: StateFlow<Set<Int>> = combine(
        prefsRepo.appliedVacancyIdsFlow,
        _myApplications,
        liveJobsFlow
    ) { localIds, cloudApps, jobs ->
        val cloudJobIds = cloudApps.map { it.jobId }.toSet()
        val matchedIds = jobs.filter {
            cloudJobIds.contains(it.docId) || cloudJobIds.contains(it.id.toString())
        }.map { it.id }.toSet()
        localIds + matchedIds
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())


    val filteredVacancies: StateFlow<List<JobVacancy>> = combine(
        _filterState,
        localFavorites,
        language,
        liveJobsFlow
    ) { filter, favs, _, liveJobs ->
        val q = filter.query.trim().lowercase()
        val matched = liveJobs.filter { job ->
            val countryMatch = filter.selectedCountry.isEmpty() ||
                job.visaCountries.contains(filter.selectedCountry)
            val profMatch = filter.selectedProfession.isEmpty() || job.category == filter.selectedProfession
            val verMatch = !filter.verifiedOnly || job.verifiedEmployer
            val savedMatch = !filter.savedOnly || favs.contains(job.id)
            val queryMatch = q.isEmpty() ||
                job.titleEn.lowercase().contains(q) ||
                job.titleRu.lowercase().contains(q) ||
                job.employerEn.lowercase().contains(q) ||
                job.employerRu.lowercase().contains(q) ||
                job.category.lowercase().contains(q) ||
                MigraCatalogData.profName(job.category, AppLang.RU).lowercase().contains(q) ||
                job.cityEn.lowercase().contains(q) ||
                job.cityRu.lowercase().contains(q) ||
                job.descEn.lowercase().contains(q) ||
                job.descRu.lowercase().contains(q)
            countryMatch && profMatch && verMatch && savedMatch && queryMatch
        }
        when (filter.sortOrder) {
            SortOrder.RELEVANCE -> matched
            SortOrder.SALARY_ASC -> matched.sortedBy { it.salaryMinRub }
            SortOrder.SALARY_DESC -> matched.sortedByDescending { it.salaryMaxRub }
            SortOrder.EXPERIENCE_DESC -> matched.sortedByDescending { it.rating }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        val current = runCatching { FirebaseAuth.getInstance().currentUser }.getOrNull()
        _authState.value = if (current != null) {
            startUserStreams(current.uid, current.email ?: "")
            AuthUiState.SignedIn(
                uid = current.uid,
                displayName = current.displayName ?: "MigraStaff User",
                email = current.email ?: "",
                isAutoSignedIn = true
            )
        } else {
            AuthUiState.SignedOut
        }

        viewModelScope.launch {
            val cached = prefsRepo.cachedFxFlow.first()
            if (cached != null) {
                _fxState.update {
                    it.copy(
                        ratesPerRub = it.ratesPerRub + cached.ratesPerRub,
                        updatedAtLabel = cached.updatedAtLabel
                    )
                }
            }
        }
    }

    private var fxFetchedOnce = false

    fun selectTab(tab: MainTab) {
        _activeTab.value = tab
        if (tab == MainTab.CRYPTO && !fxFetchedOnce) {
            fxFetchedOnce = true
            refreshFxRates(manual = false)
        }
    }

    fun toggleLanguage() {
        val next = if (language.value == AppLang.EN) AppLang.RU else AppLang.EN
        viewModelScope.launch { prefsRepo.setLanguage(next) }
    }

    fun updateSearchQuery(query: String) {
        _filterState.update { it.copy(query = query) }
    }

    fun selectCategory(profession: String) {
        _filterState.update {
            val next = if (it.selectedProfession == profession) "" else profession
            it.copy(selectedProfession = next)
        }
    }

    fun toggleSavedOnlyFilter() {
        _filterState.update { it.copy(savedOnly = !it.savedOnly) }
    }

    fun setFilterSheetVisible(visible: Boolean) {
        _showFilterSheet.value = visible
    }

    fun updateFilters(
        country: String,
        profession: String,
        verifiedOnly: Boolean,
        sortOrder: SortOrder
    ) {
        _filterState.update {
            it.copy(
                selectedCountry = country,
                selectedProfession = profession,
                verifiedOnly = verifiedOnly,
                sortOrder = sortOrder
            )
        }
    }

    fun resetFilters() {
        _filterState.value = BrowseFilterState()
    }

    fun openVacancyDetail(vacancy: JobVacancy) {
        _selectedVacancy.value = vacancy
    }

    fun closeVacancyDetail() {
        _selectedVacancy.value = null
    }

    fun openInfoTopic(topic: InfoTopic) {
        _selectedInfoTopic.value = topic
    }

    fun closeInfoTopic() {
        _selectedInfoTopic.value = null
    }

    fun toggleLocalFavorite(vacancyId: Int) {
        viewModelScope.launch { prefsRepo.toggleLocalFavorite(vacancyId) }
    }

    fun applyToVacancy(vacancy: JobVacancy) {
        viewModelScope.launch {
            prefsRepo.markVacancyApplied(vacancy.id)
            val isSignedIn = _authState.value is AuthUiState.SignedIn
            if (isSignedIn) {
                val res = firestoreRepo.applyToJob(vacancy)
                _snackbarMessage.value = if (res.isSuccess) {
                    if (language.value == AppLang.RU) "Отклик отправлен и сохранён!" else "Application sent & synced!"
                } else {
                    if (language.value == AppLang.RU) "Отклик сохранён локально" else "Saved locally (check sign-in)"
                }
            } else {
                _snackbarMessage.value = if (language.value == AppLang.RU) {
                    "Войдите в Профиль, чтобы сохранить отклик в облаке"
                } else {
                    "Applied locally! Sign in on Profile to sync status."
                }
            }
        }
    }

    // FX Controls
    fun setFxFrom(code: String) {
        _fxState.update { it.copy(fromCode = code) }
    }

    fun setFxTo(code: String) {
        _fxState.update { it.copy(toCode = code) }
    }

    fun setFxAmount(input: String) {
        val sanitized = input.filter { it.isDigit() || it == '.' }
        _fxState.update { it.copy(amountInput = sanitized) }
    }

    fun swapFxCurrencies() {
        _fxState.update { it.copy(fromCode = it.toCode, toCode = it.fromCode) }
    }

    fun applyFxPreset(from: String, to: String, amount: Double) {
        _fxState.update {
            it.copy(
                fromCode = from,
                toCode = to,
                amountInput = amount.toLong().toString()
            )
        }
    }

    fun refreshFxRates(manual: Boolean = true) {
        viewModelScope.launch {
            _fxState.update { it.copy(isSyncing = true) }
            val outcome = fxRepo.fetchLiveRates()
            _fxState.update { current ->
                current.copy(
                    ratesPerRub = if (outcome.isLive) outcome.ratesPerRub else current.ratesPerRub,
                    isLive = outcome.isLive,
                    isSyncing = false,
                    updatedAtLabel = if (outcome.updatedAtLabel.isNotEmpty()) {
                        outcome.updatedAtLabel
                    } else {
                        current.updatedAtLabel
                    }
                )
            }
            if (manual) {
                _snackbarMessage.value = if (outcome.isLive) {
                    if (language.value == AppLang.RU) "Курсы валют обновлены" else "Live FX rates updated"
                } else {
                    if (language.value == AppLang.RU) "Используется сохранённый курс" else "Using cached FX rate"
                }
            }
        }
    }

    // Crypto Controls
    fun selectCryptoPlan(planKey: String) {
        _cryptoState.update { it.copy(selectedPlanKey = planKey, lastReceiptRef = null) }
    }

    fun selectCryptoCoin(coinKey: String) {
        _cryptoState.update { it.copy(selectedCoinKey = coinKey) }
    }

    fun updateCustomCryptoAmount(raw: String) {
        val clean = raw.filter { it.isDigit() || it == '.' }
        _cryptoState.update { it.copy(customAmountInput = clean) }
    }

    fun toggleCustomCryptoCurrency() {
        _cryptoState.update {
            it.copy(customCurrency = if (it.customCurrency == "RUB") "USD" else "RUB")
        }
    }

    fun updateCryptoContact(contact: String) {
        _cryptoState.update { it.copy(payerContact = contact) }
    }

    fun updateCryptoTxHash(tx: String) {
        _cryptoState.update { it.copy(txHash = tx) }
    }

    fun submitCryptoReceipt(receipt: com.example.model.CryptoPaymentReceipt) {
        viewModelScope.launch {
            _cryptoState.update { it.copy(isSubmitting = true) }
            val currentUser = runCatching { FirebaseAuth.getInstance().currentUser }.getOrNull()
            if (currentUser != null) {
                val repo = com.example.data.firebase.MigraFirestoreRepository(getApplication())
                val res = repo.submitCryptoReceipt(receipt.copy(userId = currentUser.uid))
                res.onSuccess { ref ->
                    _cryptoState.update {
                        it.copy(isSubmitting = false, lastReceiptRef = ref, txHash = "")
                    }
                    _snackbarMessage.value = if (language.value == AppLang.RU) {
                        "Чек $ref сохранён в облаке"
                    } else {
                        "Receipt $ref saved to your cloud account"
                    }
                }.onFailure { err ->
                    _cryptoState.update { it.copy(isSubmitting = false) }
                    _snackbarMessage.value = err.localizedMessage ?: "Submission failed"
                }
            } else {
                _cryptoState.update {
                    it.copy(isSubmitting = false, lastReceiptRef = receipt.refId, txHash = "")
                }
                _snackbarMessage.value = if (language.value == AppLang.RU) {
                    "Заявка ${receipt.refId} сформирована (войдите для сохранения в облаке)"
                } else {
                    "Receipt ${receipt.refId} generated (sign in on Profile to sync)"
                }
            }
        }
    }

    // Auth Controls
    fun tryAutoSignIn(activity: Activity) {
        viewModelScope.launch {
            val res = authHelper.attemptAutoSignIn(activity)
            when (res) {
                is GoogleSignInResult.Success -> {
                    _authErrorMessage.value = null
                    startUserStreams(res.user.uid, res.user.email ?: "")
                    _authState.value = AuthUiState.SignedIn(
                        uid = res.user.uid,
                        displayName = res.user.displayName ?: "MigraStaff User",
                        email = res.user.email ?: "",
                        isAutoSignedIn = res.isAutoSignedIn
                    )
                }
                is GoogleSignInResult.NoSavedCredential,
                is GoogleSignInResult.Cancelled -> {
                    if (_authState.value is AuthUiState.Loading) {
                        _authState.value = AuthUiState.SignedOut
                    }
                }
                is GoogleSignInResult.Error -> {
                    _authState.value = AuthUiState.SignedOut
                }
            }
        }
    }

    fun signInWithGoogle(activity: Activity) {
        viewModelScope.launch {
            _authErrorMessage.value = null
            _authState.value = AuthUiState.Loading
            when (val res = authHelper.signInWithGoogle(activity)) {
                is GoogleSignInResult.Success -> {
                    startUserStreams(res.user.uid, res.user.email ?: "")
                    _authState.value = AuthUiState.SignedIn(
                        uid = res.user.uid,
                        displayName = res.user.displayName ?: "MigraStaff User",
                        email = res.user.email ?: "",
                        isAutoSignedIn = false
                    )
                    _snackbarMessage.value = if (language.value == AppLang.RU) {
                        "Вход выполнен: ${res.user.email ?: res.user.displayName}"
                    } else {
                        "Signed in as ${res.user.email ?: res.user.displayName}"
                    }
                }
                is GoogleSignInResult.Cancelled -> {
                    _authState.value = AuthUiState.SignedOut
                }
                is GoogleSignInResult.NoSavedCredential -> {
                    _authState.value = AuthUiState.SignedOut
                    _authErrorMessage.value = "No Google account found on device."
                }
                is GoogleSignInResult.Error -> {
                    _authState.value = AuthUiState.SignedOut
                    _authErrorMessage.value = res.message
                }
            }
        }
    }

    fun signInWithEmail(emailOrUsername: String, password: String) {
        val raw = emailOrUsername.trim()
        if (raw.isEmpty() || password.length < 6) {
            _authErrorMessage.value = if (language.value == AppLang.RU) {
                "Введите корректный Email/@username и пароль (от 6 символов)"
            } else {
                "Enter a valid Email/@username and password (6+ chars)"
            }
            return
        }
        viewModelScope.launch {
            _authErrorMessage.value = null
            _authState.value = AuthUiState.Loading
            try {
                val resolvedEmail = firestoreRepo.resolveEmailForLogin(raw)
                val res = FirebaseAuth.getInstance().signInWithEmailAndPassword(resolvedEmail, password).await()
                val user = res.user ?: throw IllegalStateException("Sign in failed")
                startUserStreams(user.uid, user.email ?: resolvedEmail)
                _authState.value = AuthUiState.SignedIn(
                    uid = user.uid,
                    displayName = user.displayName ?: resolvedEmail.substringBefore("@"),
                    email = user.email ?: resolvedEmail,
                    isAutoSignedIn = false
                )
            } catch (e: Exception) {
                _authState.value = AuthUiState.SignedOut
                _authErrorMessage.value = e.localizedMessage ?: "Invalid email or password"
            }
        }
    }

    fun signUpWithEmail(emailInput: String, password: String) {
        val email = emailInput.trim().lowercase()
        if (!email.contains("@") || password.length < 6) {
            _authErrorMessage.value = if (language.value == AppLang.RU) {
                "Введите корректный Email и пароль (минимум 6 символов)"
            } else {
                "Enter a valid Email and password (6+ chars)"
            }
            return
        }
        viewModelScope.launch {
            _authErrorMessage.value = null
            _authState.value = AuthUiState.Loading
            try {
                val res = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
                val user = res.user ?: throw IllegalStateException("Account creation failed")
                startUserStreams(user.uid, user.email ?: email)
                _authState.value = AuthUiState.SignedIn(
                    uid = user.uid,
                    displayName = email.substringBefore("@"),
                    email = user.email ?: email,
                    isAutoSignedIn = false
                )
            } catch (e: Exception) {
                _authState.value = AuthUiState.SignedOut
                _authErrorMessage.value = e.localizedMessage ?: "Could not create account"
            }
        }
    }

    fun resetPassword(emailOrUsername: String) {
        val raw = emailOrUsername.trim()
        if (raw.isEmpty()) {
            _authErrorMessage.value = if (language.value == AppLang.RU) {
                "Сначала введите Email или @username"
            } else {
                "Enter your Email or @username first"
            }
            return
        }
        viewModelScope.launch {
            try {
                val resolved = firestoreRepo.resolveEmailForLogin(raw)
                FirebaseAuth.getInstance().sendPasswordResetEmail(resolved).await()
                _authErrorMessage.value = null
                _snackbarMessage.value = if (language.value == AppLang.RU) {
                    "Письмо для сброса пароля отправлено на $resolved"
                } else {
                    "Password reset email sent to $resolved"
                }
            } catch (e: Exception) {
                _authErrorMessage.value = e.localizedMessage ?: "Could not send reset email"
            }
        }
    }

    fun signOut(context: Context) {
        viewModelScope.launch {
            userStreamsJob?.cancel()
            _myApplications.value = emptyList()
            _employerProfile.value = null
            _allApplications.value = emptyList()
            _allEmployers.value = emptyList()
            authHelper.signOut(context)
            _authState.value = AuthUiState.SignedOut
            _authErrorMessage.value = null
            _snackbarMessage.value = if (language.value == AppLang.RU) "Вы вышли из аккаунта" else "Signed out"
        }
    }

    fun showMessage(msg: String) {
        _snackbarMessage.value = msg
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    private fun startUserStreams(uid: String, email: String) {
        userStreamsJob?.cancel()
        userStreamsJob = viewModelScope.launch {
            launch {
                var prevStatuses: Map<String, String>? = null
                firestoreRepo.observeMyApplications(uid).collect { list ->
                    _myApplications.value = list
                    val prev = prevStatuses
                    if (prev != null) {
                        list.forEach { app ->
                            val oldSt = prev[app.id]
                            if (oldSt != null && oldSt != app.status) {
                                MigraNotificationHelper.show(
                                    getApplication(),
                                    app.id.hashCode(),
                                    "Application Update: ${app.jobTitle}",
                                    "Your status is now ${app.status.uppercase()}"
                                )
                            }
                        }
                    }
                    prevStatuses = list.associate { it.id to it.status }
                }
            }
            launch {
                var prevMsgIds: Set<String>? = null
                firestoreRepo.observeMessages(uid).collect { res ->
                    if (res is StreamResult.Success) {
                        val msgs = res.data
                        val prev = prevMsgIds
                        if (prev != null) {
                            msgs.filter { !prev.contains(it.id) && it.senderRole == "director" }
                                .forEach { m ->
                                    MigraNotificationHelper.show(
                                        getApplication(),
                                        m.id.hashCode(),
                                        "New Message from Director",
                                        m.textOriginal.take(120)
                                    )
                                }
                        }
                        prevMsgIds = msgs.map { it.id }.toSet()
                    }
                }
            }
            launch {
                firestoreRepo.observeEmployerProfile(uid).collect { _employerProfile.value = it }
            }
            if (email.equals("rajgaikwad9891@gmail.com", ignoreCase = true)) {
                launch {
                    firestoreRepo.observeAllApplications().collect { _allApplications.value = it }
                }
                launch {
                    firestoreRepo.observeAllEmployers().collect { _allEmployers.value = it }
                }
            } else {
                _allApplications.value = emptyList()
                _allEmployers.value = emptyList()
            }
        }
    }

    fun registerEmployerAccount(companyName: String, city: String, emailInput: String, password: String) {
        val email = emailInput.trim().lowercase()
        if (companyName.trim().length < 2 || !email.contains("@") || password.length < 6) {
            _authErrorMessage.value = if (language.value == AppLang.RU) {
                "Заполните название компании, Email и пароль (от 6 символов)"
            } else {
                "Enter company name, valid Email, and password (6+ chars)"
            }
            return
        }
        viewModelScope.launch {
            _authErrorMessage.value = null
            _authState.value = AuthUiState.Loading
            try {
                val res = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
                val user = res.user ?: throw IllegalStateException("Registration failed")
                firestoreRepo.registerEmployerProfile(user.uid, companyName, city, email)
                startUserStreams(user.uid, email)
                _authState.value = AuthUiState.SignedIn(
                    uid = user.uid,
                    displayName = companyName.trim(),
                    email = email,
                    isAutoSignedIn = false
                )
                _snackbarMessage.value = "Company registered! Pending Director approval."
            } catch (e: Exception) {
                _authState.value = AuthUiState.SignedOut
                _authErrorMessage.value = e.localizedMessage ?: "Could not register employer"
            }
        }
    }

    fun updateApplicationStatus(appId: String, status: String) {
        viewModelScope.launch {
            val res = firestoreRepo.updateApplicationStatus(appId, status)
            _snackbarMessage.value = if (res.isSuccess) "Status updated: $status" else "Update failed"
        }
    }

    fun updateEmployerStatus(empUid: String, status: String) {
        viewModelScope.launch {
            val res = firestoreRepo.updateEmployerStatus(empUid, status)
            _snackbarMessage.value = if (res.isSuccess) "Employer: $status" else "Update failed"
        }
    }

    fun publishJob(title: String, location: String, salary: String, companyName: String, employerUid: String) {
        if (title.isBlank() || location.isBlank() || salary.isBlank()) {
            _snackbarMessage.value = "Fill job title, location, and salary"
            return
        }
        viewModelScope.launch {
            val res = firestoreRepo.publishJob(title, location, salary, companyName, employerUid)
            _snackbarMessage.value = if (res.isSuccess) "Job published live!" else "Could not publish job"
        }
    }

    fun deleteJob(jobDocId: String) {
        if (jobDocId.isBlank()) return
        viewModelScope.launch {
            val res = firestoreRepo.deleteJob(jobDocId)
            _snackbarMessage.value = if (res.isSuccess) "Job deleted" else "Delete failed"
        }
    }

}
