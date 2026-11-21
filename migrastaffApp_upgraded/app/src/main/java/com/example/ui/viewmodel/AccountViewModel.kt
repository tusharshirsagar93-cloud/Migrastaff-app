package com.example.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.firebase.MigraFirestoreRepository
import com.example.data.firebase.StreamResult
import com.example.model.CandidateProfile
import com.example.model.CryptoPaymentReceipt
import com.example.model.FavoriteWorker
import com.example.model.SupportMessage
import com.example.model.WorkerProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Instantiated ONLY inside `if (authState is AuthUiState.SignedIn)` keyed by `uid`,
 * preventing any unauthenticated Firestore listeners from firing on cold launch.
 */
class AccountViewModel(
    private val uid: String,
    private val repository: MigraFirestoreRepository
) : ViewModel() {

    val profileStream: StateFlow<StreamResult<CandidateProfile?>> =
        repository.observeProfile(uid)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StreamResult.Loading)

    val favoritesStream: StateFlow<StreamResult<List<FavoriteWorker>>> =
        repository.observeFavorites(uid)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StreamResult.Loading)

    val messagesStream: StateFlow<StreamResult<List<SupportMessage>>> =
        repository.observeMessages(uid)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StreamResult.Loading)

    val receiptsStream: StateFlow<StreamResult<List<CryptoPaymentReceipt>>> =
        repository.observeCryptoReceipts(uid)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StreamResult.Loading)

    private val _isSavingProfile = MutableStateFlow(false)
    val isSavingProfile: StateFlow<Boolean> = _isSavingProfile.asStateFlow()

    private val _isSendingMessage = MutableStateFlow(false)
    val isSendingMessage: StateFlow<Boolean> = _isSendingMessage.asStateFlow()

    fun saveCandidateProfile(
        profile: CandidateProfile,
        onResult: (Result<Unit>) -> Unit
    ) {
        viewModelScope.launch {
            _isSavingProfile.value = true
            val res = repository.saveCandidateProfile(profile.copy(userId = uid))
            _isSavingProfile.value = false
            onResult(res)
        }
    }

    fun toggleCloudFavorite(
        worker: WorkerProfile,
        currentlySaved: Boolean,
        onResult: (Result<Unit>) -> Unit = {}
    ) {
        viewModelScope.launch {
            onResult(repository.toggleFavorite(worker, currentlySaved))
        }
    }

    fun sendDirectorMessage(
        text: String,
        langCode: String,
        onResult: (Result<Unit>) -> Unit
    ) {
        val clean = text.trim()
        if (clean.isEmpty()) return
        val translatedPreview = if (langCode == "ru") {
            "[Auto EN]: $clean"
        } else {
            "[Авто RU]: $clean"
        }
        viewModelScope.launch {
            _isSendingMessage.value = true
            val res = repository.sendMessage(clean, translatedPreview, langCode)
            _isSendingMessage.value = false
            onResult(res)
        }
    }

    fun submitCryptoReceipt(
        receipt: CryptoPaymentReceipt,
        onResult: (Result<String>) -> Unit
    ) {
        viewModelScope.launch {
            onResult(repository.submitCryptoReceipt(receipt.copy(userId = uid)))
        }
    }

    class Factory(
        private val uid: String,
        private val context: Context
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AccountViewModel(
                uid = uid,
                repository = MigraFirestoreRepository(context.applicationContext)
            ) as T
        }
    }
}
