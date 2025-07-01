package com.paandaaa.homey.android.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
// Import your BottomNavItem and the list
import com.paandaaa.homey.android.ui.navigation.BottomNavItem
import com.paandaaa.homey.android.ui.navigation.bottomNavScreenItems // Assuming you have this list

@Composable
fun BottomNavBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = androidx.compose.ui.graphics.Color.White,
        modifier = modifier.shadow(16.dp)
    ) {
        bottomNavScreenItems.forEach { screenItem ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screenItem.icon,
                        contentDescription = screenItem.title
                    )
                },
                label = {
                    Text(text = screenItem.title)
                },
                selected = currentDestination?.hierarchy?.any { it.route == screenItem.route } == true,
                onClick = {
                    navController.navigate(screenItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}