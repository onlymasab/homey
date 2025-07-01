package com.paandaaa.homey.android.data.remote.api

import RecipeApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApiService {
    // This is a placeholder. For this app, Gemini generates recipes.
    // If you integrate a third-party recipe API, define its endpoints here.
    @GET("search")
    suspend fun searchRecipes(@Query("query") query: String): Response<RecipeApiResponse>
}