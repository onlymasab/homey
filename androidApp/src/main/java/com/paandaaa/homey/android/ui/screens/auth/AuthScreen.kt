package com.paandaaa.homey.android.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.paandaaa.homey.android.R
import com.paandaaa.homey.android.ui.navigation.Screen

@Composable
fun AuthScreen(navController: NavHostController) {

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Spacer(modifier = Modifier.weight(0.1f))
        Image(
            painter = painterResource(R.drawable.cover_auth),
            contentDescription = "Auth Cover Image",
            modifier = Modifier
                .width(228.dp),
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.weight(0.1f))
        Column (
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Text(
                text = "Welcome Back!",
                style = TextStyle(
                    fontSize = 21.88.sp,
                    lineHeight = 30.63.sp,
                    //                fontFamily = FontFamily(Font(R.font.poppins)),
                    fontWeight = FontWeight(600),
                    color = Color(0xFF0058A3),

                    textAlign = TextAlign.Center,
                    letterSpacing = 0.22.sp,
                )
            )
            Text(
                text = "We missed you — please sign in to continue.",
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 19.6.sp,
//                fontFamily = FontFamily(Font(R.font.poppins)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF18181A),
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.14.sp,
                )
            )
        }

        Spacer(modifier = Modifier.weight(0.1f))


        Column (
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Button (
                onClick = {
                    // After login success
                    navController.navigate(Screen.Home.route)
                },
                shape = RoundedCornerShape(size = 8.dp),
                modifier = Modifier
                    .width(320.dp)
                    .height(48.dp)

            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 24.dp)
                ) {
                    // Child views.
                    Image(
                        painter = painterResource(id = R.drawable.google_icon),
                        contentDescription = "Google Icon",
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        text = "Continue with Google",
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 19.6.sp,
//                      fontFamily = FontFamily(Font(R.font.poppins)),
                            fontWeight = FontWeight(500),
                            color = Color.White,
                            letterSpacing = 0.14.sp,

                            )
                    )
                }
            }
            Button (
                onClick = {
                    // After login success
                    navController.navigate(Screen.Home.route)
                },
                shape = RoundedCornerShape(size = 8.dp),
                modifier = Modifier
                    .width(320.dp)
                    .height(48.dp)

            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 24.dp)
                ) {
                    // Child views.
                    Image(
                        painter = painterResource(id = R.drawable.apple_icon),
                        contentDescription = "Apple Icon",
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        text = "Continue with Apple",
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 19.6.sp,
//                      fontFamily = FontFamily(Font(R.font.poppins)),
                            fontWeight = FontWeight(500),
                            color = Color.White,
                            letterSpacing = 0.14.sp,

                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(0.1f))
    }

}




