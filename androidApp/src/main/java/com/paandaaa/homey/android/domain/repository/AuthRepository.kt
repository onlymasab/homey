package com.paandaaa.homey.android.domain.repository

import com.paandaaa.homey.android.domain.model.UserModel

interface AuthRepository {
    // Google sign-in
    suspend fun signInWithGoogle(idToken: String, rawNonce: String): Result<Unit>

    // Authentication state
    suspend fun isAuthenticated(): Boolean
    suspend fun getCurrentUser(): Result<UserModel?>

    // User management
    suspend fun signOut(): Result<Unit>

    // Token management
    suspend fun getIdToken(forceRefresh: Boolean = false): Result<String?>
}