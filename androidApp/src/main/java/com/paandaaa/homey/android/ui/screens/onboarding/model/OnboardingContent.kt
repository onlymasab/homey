package com.paandaaa.homey.android.ui.screens.onboarding.model


import androidx.annotation.DrawableRes
import com.paandaaa.homey.android.R

data class OnboardingPage(
    val title: String,
    val description: String,
    @DrawableRes val imageRes: Int
)

val onboardingPages = listOf(
    OnboardingPage(title = "Healthy & Delicious Recipes", description =
        "Discover, save, and organize delicious recipes customized to your taste, dietary preferences, and lifestyle.", imageRes =  R.drawable.onboarding_cover_1),
    OnboardingPage(title = "Meal & Grocery Planning",description =
        "Easily plan your meals, create smart grocery lists, and streamline your shopping experience—all  in one place.", imageRes =  R.drawable.onboarding_cover_2),
    OnboardingPage(title = "Your Home Companion",  description =
        "Effortlessly manage your home, kitchen, and meals with smart tools that simplify your daily life—one step at a time.", imageRes =  R.drawable.onboarding_cover_3),
)