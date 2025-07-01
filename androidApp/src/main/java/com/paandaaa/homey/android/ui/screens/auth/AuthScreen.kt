package com.paandaaa.homey.android.ui.screens.auth

import android.app.Activity
import android.widget.Toast
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.paandaaa.homey.android.BuildConfig
import com.paandaaa.homey.android.R
import com.paandaaa.homey.android.ui.navigation.Screen
import com.paandaaa.homey.android.ui.viewmodel.AuthUiState
import com.paandaaa.homey.android.ui.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    onSignInSuccess: () -> Unit,
) {

    val authState by authViewModel.authState
    val context = LocalContext.current

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


        when (authState) {
            is AuthUiState.Idle -> {
                Column (
                    verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    SignInButton (
                        onClick = { authViewModel.signInWithGoogle(context as Activity, BuildConfig.GOOGLE_WEB_CLIENT_ID) },
                        logo = R.drawable.google_icon,
                        provider = "Continue with Google"
                    )
                    SignInButton (
                        onClick = { Toast.makeText(context, "Not Available!", Toast.LENGTH_SHORT).show() },
                        logo = R.drawable.apple_icon,
                        provider = "Continue with Apple"
                    )
                }

            }

            is AuthUiState.Loading -> {
                CircularProgressIndicator()
            }

            is AuthUiState.Success -> {
                Text("Signed in successfully!")
                onSignInSuccess() // You probably want this here
            }

            is AuthUiState.Error -> {
                val message = (authState as AuthUiState.Error).message
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                Column (
                    verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    SignInButton (
                        onClick = { authViewModel.signInWithGoogle(context as Activity , BuildConfig.GOOGLE_WEB_CLIENT_ID) },
                        logo = R.drawable.google_icon,
                        provider = "Continue with Google"
                    )
                    SignInButton (
                        onClick = { Toast.makeText(context, "Not Available!", Toast.LENGTH_SHORT).show() },
                        logo = R.drawable.apple_icon,
                        provider = "Continue with Apple"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(0.1f))
    }

}





@Composable
fun SignInButton(onClick: () -> Unit, logo: Int, provider: String) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.LightGray,
            contentColor = Color.Black,
        ),
        shape = CircleShape,
        modifier = Modifier
            .size(
                width = 320.dp,
                height = 44.dp,
            )
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = logo),
                contentDescription = "Google Icon",
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = provider,
                modifier = Modifier
                    .width(180.dp)
            )
        }
    }
}