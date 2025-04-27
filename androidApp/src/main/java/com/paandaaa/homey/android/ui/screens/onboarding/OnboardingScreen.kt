package com.paandaaa.homey.android.ui.screens.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.paandaaa.homey.android.ui.screens.onboarding.model.onboardingPages
import com.paandaaa.homey.android.ui.viewmodel.OnboardingViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paandaaa.homey.android.ui.screens.onboarding.OnboardingPage

@Composable
fun OnboardingScreen(
    navHostController: NavHostController,
    viewModel: OnboardingViewModel = viewModel()
) {
    val currentPage = viewModel.page.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {

        OnboardingPage(
            page = onboardingPages[currentPage],
            currentPage = currentPage,
            navHostController = navHostController,
            viewModel = viewModel
            )

    }
}





