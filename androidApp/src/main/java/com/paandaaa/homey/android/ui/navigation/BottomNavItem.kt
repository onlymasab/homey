// Assume this is in your com.paandaaa.homey.android.ui.navigation package
package com.paandaaa.homey.android.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.* // Or your specific icons

// This is an assumption of what BottomNavItem might look like.
// It should align with your actual Screen definitions for navigation.
sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Chat : BottomNavItem(Screen.Chat.route, "Chat", Icons.Default.Home)
    object Recipes : BottomNavItem(Screen.Recipes.route, "Recipes", Icons.Default.Restaurant)
    object Groceries: BottomNavItem(Screen.Groceries.route, "Groceries", Icons.Default.ShoppingCart)
    object MealPlan : BottomNavItem(Screen.MealPlan.route, "MealPlan", Icons.Default.CalendarMonth)
    object Health : BottomNavItem(Screen.Health.route, "Health", Icons.Default.HealthAndSafety)

}

// It's good practice to have a list of items to display
val bottomNavScreenItems = listOf(
    BottomNavItem.Chat,
    BottomNavItem.Recipes,
    BottomNavItem.Groceries,
    BottomNavItem.MealPlan,
    BottomNavItem.Health,
)