package com.paandaaa.homey.android.ui.screens.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.paandaaa.homey.android.ui.components.BottomNavBar
import com.paandaaa.homey.android.ui.navigation.AppNavGraph
import com.paandaaa.homey.android.ui.navigation.Screen
import com.paandaaa.homey.android.ui.navigation.bottomNavScreenItems
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Homey", // Changed to more localized name
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor =  Color(0xFFf242a7),
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
            )
        },
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val showBottomBar = bottomNavScreenItems.any { screen ->
                currentDestination?.hierarchy?.any { it.route == screen.route } == true
            }

            if (showBottomBar) {
                BottomNavBar(
                    navController = navController,
                )
            }
        },

    ) { innerPadding ->
        AppNavGraph(
            navController = navController,
            startDestination = Screen.Chat.route,
            authViewModel = hiltViewModel(),
            modifier = Modifier.padding(innerPadding)
        )
    }
}