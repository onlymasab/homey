package com.paandaaa.homey.android.ui.screens.onboarding


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.paandaaa.homey.android.ui.screens.onboarding.model.OnboardingPage
import com.paandaaa.homey.android.ui.screens.onboarding.model.onboardingPages
import com.paandaaa.homey.android.ui.viewmodel.OnboardingViewModel

@Composable
fun OnboardingPage(page: OnboardingPage, currentPage: Int = 0, navHostController: NavHostController, viewModel: OnboardingViewModel) {

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Box (

            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
        ) {
                Image(
                    contentScale = ContentScale.FillWidth,
                    alignment = Alignment.Center,
                    painter = painterResource(id = page.imageRes),
                    contentDescription = "Onboarding Image",
                    modifier = Modifier
                        .fillMaxWidth()

                )

            if (currentPage < 2) {
                TextButton(
                    onClick = { viewModel.skip() },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(24.dp)
                ) {
                    Text("Skip", color = Color.Black, fontSize = 16.sp)
                }
            } else {
                Spacer(Modifier.size(48.dp))
            }
        }
        Spacer(modifier = Modifier.weight(0.1f))
        Column (
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text(
                text = page.title,
                style = TextStyle(
                    fontSize = 21.88.sp,
                    lineHeight = 30.63.sp,
                    //                fontFamily = FontFamily(Font(R.font.poppins)),
                    fontWeight = FontWeight(600),
                    color = Color(0xFF18181A),
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.22.sp,
                )
            )
            Text(
                text = page.description,
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
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {

            if( currentPage > 0 ) {
                Button(
                    shape = RoundedCornerShape(size = 8.dp),
                    onClick = { viewModel.prevPage() },
                    modifier = Modifier
                        .padding(4.dp),
                ) {
                    Text(text = "Back")
                }
            } else {
                Spacer(modifier = Modifier.width(64.dp)) // Placeholder space
            }


            Button(
                shape = RoundedCornerShape(size = 8.dp),
                colors = ButtonColors(
                    containerColor = Color(0xFFF7B3C6),
                    contentColor = Color.Black,
                    disabledContainerColor = Color(0xFF0058A3),
                    disabledContentColor = Color.White
                ),
                onClick = {
                    if (currentPage == onboardingPages.lastIndex) {
                        navHostController.navigate("auth") {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    } else {
                        viewModel.nextPage()
                    }
                },
                modifier = Modifier
                    .padding(4.dp),
            ) {

                Text(text = if (currentPage == onboardingPages.lastIndex) "Done" else "Next")

            }
        }
        Spacer(modifier = Modifier.weight(0.1f))
    }

}