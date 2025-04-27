package com.paandaaa.homey.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class OnboardingViewModel(private val totalPages: Int = 3) : ViewModel() {

    private val _page = MutableStateFlow(0)
    val page = _page.asStateFlow()

    fun nextPage() {
        if (_page.value < totalPages - 1) _page.value += 1
    }

    fun prevPage() {
        if (_page.value > 0) _page.value -= 1
    }

    fun skip() {
        _page.value = totalPages - 1
    }
}