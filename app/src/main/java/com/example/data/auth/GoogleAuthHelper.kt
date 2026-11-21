package com.example.data.auth

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

sealed interface GoogleSignInResult {
    data class Success(val user: FirebaseUser, val isAutoSignedIn: Boolean) : GoogleSignInResult
    data object Cancelled : GoogleSignInResult
    data object NoSavedCredential : GoogleSignInResult
    data class Error(val message: String) : GoogleSignInResult
}

class GoogleAuthHelper(
    private val injectedAuth: FirebaseAuth? = null
) {
    private fun resolveAuth(): FirebaseAuth? =
        injectedAuth ?: runCatching { FirebaseAuth.getInstance() }.getOrNull()

    private fun resolveWebClientId(context: Context): String? {
        val resId = context.resources.getIdentifier(
            "default_web_client_id",
            "string",
            context.packageName
        )
        if (resId == 0) return null
        return runCatching { context.getString(resId) }
            .getOrNull()
            ?.takeIf { it.isNotBlank() && !it.startsWith("YOUR_") }
    }

    suspend fun attemptAutoSignIn(activity: Activity): GoogleSignInResult {
        val auth = resolveAuth() ?: return GoogleSignInResult.NoSavedCredential
        val current = auth.currentUser
        if (current != null) {
            return GoogleSignInResult.Success(current, isAutoSignedIn = true)
        }
        val serverClientId = resolveWebClientId(activity)
            ?: return GoogleSignInResult.NoSavedCredential

        val credentialManager = CredentialManager.create(activity)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(serverClientId)
            .setAutoSelectEnabled(true)
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val response = credentialManager.getCredential(activity, request)
            val credential = response.credential
            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data)
                val firebaseCred = GoogleAuthProvider.getCredential(googleIdToken.idToken, null)
                val authResult = auth.signInWithCredential(firebaseCred).await()
                val user = authResult.user
                    ?: return GoogleSignInResult.Error("Firebase sign-in returned null user")
                GoogleSignInResult.Success(user, isAutoSignedIn = true)
            } else {
                GoogleSignInResult.NoSavedCredential
            }
        } catch (_: NoCredentialException) {
            GoogleSignInResult.NoSavedCredential
        } catch (_: GetCredentialCancellationException) {
            GoogleSignInResult.Cancelled
        } catch (e: GetCredentialException) {
            Log.w(TAG, "Silent Google sign-in skipped: ${e.message}")
            GoogleSignInResult.NoSavedCredential
        } catch (e: Exception) {
            Log.e(TAG, "Auto sign-in error", e)
            GoogleSignInResult.NoSavedCredential
        }
    }

    suspend fun signInWithGoogle(activity: Activity): GoogleSignInResult {
        val auth = resolveAuth()
            ?: return GoogleSignInResult.Error("Firebase authentication is not configured in this build.")
        val serverClientId = resolveWebClientId(activity)
            ?: return GoogleSignInResult.Error("Google Sign-In is not configured in this build.")

        val credentialManager = CredentialManager.create(activity)
        val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(serverClientId).build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        return try {
            val response = credentialManager.getCredential(activity, request)
            val credential = response.credential
            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data)
                val firebaseCred = GoogleAuthProvider.getCredential(googleIdToken.idToken, null)
                val authResult = auth.signInWithCredential(firebaseCred).await()
                val user = authResult.user
                    ?: return GoogleSignInResult.Error("Firebase authentication failed")
                GoogleSignInResult.Success(user, isAutoSignedIn = false)
            } else {
                GoogleSignInResult.Error("Unsupported credential type returned")
            }
        } catch (_: GetCredentialCancellationException) {
            GoogleSignInResult.Cancelled
        } catch (e: NoCredentialException) {
            GoogleSignInResult.Error("No Google account available on device. Please add a Google account in device settings.")
        } catch (e: GetCredentialException) {
            Log.e(TAG, "CredentialManager error", e)
            GoogleSignInResult.Error(e.errorMessage?.toString() ?: "Google Sign-In failed")
        } catch (e: Exception) {
            Log.e(TAG, "Firebase Google sign-in error", e)
            GoogleSignInResult.Error(e.localizedMessage ?: "Google Sign-In failed")
        }
    }

    suspend fun signOut(context: Context) {
        withContext(NonCancellable) {
            resolveAuth()?.signOut()
            try {
                CredentialManager.create(context).clearCredentialState(ClearCredentialStateRequest())
            } catch (e: ClearCredentialException) {
                Log.w(TAG, "Failed to clear credential state", e)
            }
        }
    }

    private companion object {
        const val TAG = "GoogleAuthHelper"
    }
}
