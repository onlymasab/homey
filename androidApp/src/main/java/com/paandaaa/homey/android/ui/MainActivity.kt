package com.paandaaa.homey.android.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.paandaaa.homey.android.ui.navigation.AppNavGraph
import com.paandaaa.homey.android.ui.navigation.Screen
import com.paandaaa.homey.android.ui.theme.HomeyTheme
import com.paandaaa.homey.android.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = hiltViewModel()

            HomeyTheme {
                AppNavGraph(
                    navController = navController,
                    startDestination = Screen.Splash.route,
                    authViewModel = authViewModel
                )
            }

        }
    }
}
















