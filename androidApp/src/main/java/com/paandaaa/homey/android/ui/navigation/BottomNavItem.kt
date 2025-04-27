package com.paandaaa.homey.android.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val title: String
) {
    data object Home : BottomNavItem( route ="home", icon = Icons.Default.Home, title ="Home")
    data object Cart : BottomNavItem( route ="cart", icon = Icons.Default.Home, title ="Cart")
    data object Wishlist : BottomNavItem( route ="wishlist", icon = Icons.Default.Home, title ="Wishlist")
    data object Profile : BottomNavItem( route ="profile", icon = Icons.Default.Home, title ="Profile")
    data object Setting : BottomNavItem( route ="setting", icon = Icons.Default.Home, title ="setting")
}