package com.paandaaa.homey.android.data.repository

import com.paandaaa.homey.android.domain.repository.AuthRepository
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(): AuthRepository {
    override fun isUserLoggedIn(): Boolean {
        // Use SharedPreferences / Firebase / Token check here
        return true
    }
}