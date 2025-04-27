package com.paandaaa.homey.android.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.paandaaa.homey.android.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepo: AuthRepository
) : ViewModel() {

    var isAuthenticated by mutableStateOf(false)
        private set

    init {
        checkAuth()
    }

    private fun checkAuth() {
        isAuthenticated = authRepo.isUserLoggedIn()
    }
}