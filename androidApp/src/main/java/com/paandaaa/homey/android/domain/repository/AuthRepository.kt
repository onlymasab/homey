package com.paandaaa.homey.android.domain.repository

interface AuthRepository {
    fun isUserLoggedIn(): Boolean
}