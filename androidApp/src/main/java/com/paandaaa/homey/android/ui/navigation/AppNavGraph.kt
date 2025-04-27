package com.paandaaa.homey.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.paandaaa.homey.android.ui.screens.auth.AuthScreen
import com.paandaaa.homey.android.ui.screens.splash.SplashScreen
import com.paandaaa.homey.android.ui.screens.home.HomeScreen
import com.paandaaa.homey.android.ui.screens.onboarding.OnboardingScreen
import com.paandaaa.homey.android.ui.screens.profile.ProfileScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String,
    isAuthenticated: Boolean
) {
    NavHost(navController, startDestination = startDestination) {
        composable(Screen.Splash.route) { SplashScreen(navController) }
        composable(Screen.Onboarding.route) { OnboardingScreen(navController) }
        composable(Screen.Auth.route) { AuthScreen(navController) }
        composable(Screen.Home.route) {
            if (isAuthenticated) HomeScreen(navController)
            else navController.navigate(Screen.Auth.route)
        }
        composable(Screen.Profile.route) {
            if (isAuthenticated) ProfileScreen(navController)
            else navController.navigate(Screen.Auth.route)
        }
    }
}


