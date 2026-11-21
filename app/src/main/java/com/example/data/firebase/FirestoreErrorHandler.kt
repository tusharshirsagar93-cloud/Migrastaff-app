package com.example.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONObject

enum class OperationType {
    CREATE,
    UPDATE,
    DELETE,
    LIST,
    GET,
    WRITE
}

object FirestoreErrorHandler {
    fun handleFirestoreError(
        exception: Throwable,
        operationType: OperationType,
        path: String
    ): Exception {
        val user = FirebaseAuth.getInstance().currentUser
        val errorJson = JSONObject().apply {
            put("error", exception.message ?: exception.toString())
            put("operationType", operationType.name.lowercase())
            put("path", path)
            put("authInfo", JSONObject().apply {
                put("userId", user?.uid ?: JSONObject.NULL)
                put("email", user?.email ?: JSONObject.NULL)
                put("emailVerified", user?.isEmailVerified ?: false)
                put("isAnonymous", user?.isAnonymous ?: false)
                put("providerId", user?.providerData?.firstOrNull()?.providerId ?: JSONObject.NULL)
            })
        }
        Log.e("FirestoreError", errorJson.toString(), exception)
        return RuntimeException("FirestoreError: ${errorJson}", exception)
    }
}
