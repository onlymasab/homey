package com.paandaaa.homey.android.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier // Import Modifier
import androidx.compose.material3.Text // For placeholder screens
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.paandaaa.homey.android.ui.screens.auth.AuthScreen
import com.paandaaa.homey.android.ui.screens.chat.ChatScreen
import com.paandaaa.homey.android.ui.screens.grocery.GroceryScreen
import com.paandaaa.homey.android.ui.screens.health.HealthDashboardScreen
import com.paandaaa.homey.android.ui.screens.home.HomeScreen
import com.paandaaa.homey.android.ui.screens.main.MainScreen
import com.paandaaa.homey.android.ui.screens.mealplan.MealPlanScreen
import com.paandaaa.homey.android.ui.screens.onboarding.OnboardingScreen
import com.paandaaa.homey.android.ui.screens.profile.ProfileScreen
import com.paandaaa.homey.android.ui.screens.recipes.RecipeListScreen
import com.paandaaa.homey.android.ui.screens.splash.SplashScreen
import com.paandaaa.homey.android.ui.viewmodel.AuthViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier // Add modifier parameter
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier // Apply the modifier here
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.Onboarding.route) {
            OnboardingScreen(navController)
        }
        composable(Screen.Auth.route) {
            AuthScreen(
                navController = navController,
                authViewModel = authViewModel,
                onSignInSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true } // Clear auth from backstack
                    }
                }
            )
        }

        composable (Screen.Main.route) {
            MainScreen()
        }

        composable(Screen.Chat.route) {
            ChatScreen()
        }
        // Add new destinations for Bottom Navigation Bar items
        composable(Screen.Recipes.route) {
            // Replace with your actual MealsScreen composable
            // Example: MealsScreen(navController)
            RecipeListScreen(navController = navController)
        }
        composable(Screen.Groceries.route) {
            // Replace with your actual GroceryScreen composable
            // Example: GroceryScreen(navController)
            GroceryScreen()
        }
        composable(Screen.MealPlan.route) {
            // Replace with your actual MealPlanScreen composable
            // Example: MealPlanScreen(navController)
            MealPlanScreen()

        }
        composable(Screen.Health.route) {
            // Replace with your actual MealPlanScreen composable
            // Example: MealPlanScreen(navController)
            HealthDashboardScreen()

        }
    }
}