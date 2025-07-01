package com.paandaaa.homey.android.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle // Example icon
import androidx.compose.material.icons.filled.Home // Example icon
import androidx.compose.material.icons.filled.RestaurantMenu // Example Icon
import androidx.compose.material.icons.filled.ShoppingCart // Example Icon
import androidx.compose.ui.graphics.vector.ImageVector

// Your existing Screen class
sealed class Screen(val route: String, val label: String? = null, val icon: ImageVector? = null) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Auth : Screen("auth")

    object Main : Screen("main")

    // Screens for Bottom Navigation
    object Chat : Screen("chat", "Chat", Icons.Default.Home)
    object Recipes : Screen("recipes", "Recipes", Icons.Default.RestaurantMenu) // Added for example
    object Groceries : Screen("groceries", "Groceries", Icons.Default.ShoppingCart) // Added for example
    object MealPlan : Screen("mealplan", "MealPlan", Icons.Default.AccountCircle)
    object Health : Screen("health", "Health", Icons.Default.AccountCircle)

    // Add other screens if they are part of the bottom nav
}

