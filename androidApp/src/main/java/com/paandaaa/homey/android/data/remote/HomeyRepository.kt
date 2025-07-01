package com.paandaaa.homey.android.data.remote

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.paandaaa.homey.android.data.local.dao.GroceryDao
import com.paandaaa.homey.android.data.local.dao.MealPlanDao
import com.paandaaa.homey.android.data.local.dao.RecipeDao
import com.paandaaa.homey.android.data.local.entity.GroceryItemEntity
import com.paandaaa.homey.android.data.local.entity.MealPlanEntity
import com.paandaaa.homey.android.data.local.entity.RecipeEntity
import com.paandaaa.homey.android.data.remote.api.GeminiApiService
import com.paandaaa.homey.android.data.remote.api.RecipeApiService
import com.paandaaa.homey.android.data.remote.model.Content
import com.paandaaa.homey.android.data.remote.model.GeminiRequest
import com.paandaaa.homey.android.data.remote.model.Part
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.JsonNull.content
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class HomeyRepository @Inject constructor(
    private val recipeApiService: RecipeApiService, // Placeholder for external API
    private val geminiApiService: GeminiApiService, // Injected Gemini Retrofit service
    private val recipeDao: RecipeDao,
    private val groceryDao: GroceryDao,
    private val mealPlanDao: MealPlanDao
) {
    // IMPORTANT: Replace with your actual Gemini API key.
    // In a real app, secure this key (e.g., BuildConfig field, environment variable).
    private val GEMINI_API_KEY = "AIzaSyDNwYMSiouA1FogK1oLQBXSOwzBddZ3NOM"
    private val GEMINI_MODEL_NAME = "gemini-2.0-flash" // Or "gemini-pro"

    // --- Recipe Operations ---
    // This function is illustrative for a remote API. For this app, recipes are AI-generated or saved locally.
    suspend fun searchRecipesRemote(query: String): Flow<List<RecipeEntity>> = flow {
        try {
            // Example: If you had a real API, you'd fetch here
            // val response = recipeApiService.searchRecipes(query)
            // if (response.isSuccessful) { /* ... process and emit ... */ }
            emit(emptyList()) // For now, just emit empty list or from local DB
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    fun getSavedRecipes(): Flow<List<RecipeEntity>> {
        return recipeDao.getAllRecipes()
    }

    fun searchSavedRecipes(query: String): Flow<List<RecipeEntity>> {
        return recipeDao.searchRecipes(query)
    }

    fun getRecipeById(id: Int): Flow<RecipeEntity?> {
        return recipeDao.getRecipeById(id)
    }

    fun getFavoriteRecipes(): Flow<List<RecipeEntity>> {
        return recipeDao.getFavoriteRecipes()
    }

    suspend fun saveRecipe(recipe: RecipeEntity) {
        recipeDao.insertRecipe(recipe)
    }

    suspend fun updateRecipe(recipe: RecipeEntity) {
        recipeDao.updateRecipe(recipe)
    }

    suspend fun deleteRecipe(recipeId: Int) {
        recipeDao.deleteRecipe(recipeId)
    }

    // --- Grocery Operations ---
    fun getAllGroceryItems(): Flow<List<GroceryItemEntity>> {
        return groceryDao.getAllGroceryItems()
    }

    suspend fun addGroceryItem(item: GroceryItemEntity) {
        groceryDao.insertGroceryItem(item)
    }

    suspend fun updateGroceryItem(item: GroceryItemEntity) {
        groceryDao.updateGroceryItem(item)
    }

    suspend fun deleteGroceryItem(item: GroceryItemEntity) {
        groceryDao.deleteGroceryItem(item)
    }

    suspend fun clearCheckedGroceryItems() {
        groceryDao.deleteCheckedItems()
    }

    // --- Meal Plan Operations ---
    fun getMealPlansForDate(date: String): Flow<List<MealPlanEntity>> {
        return mealPlanDao.getMealPlansForDate(date)
    }

    suspend fun saveMealPlan(mealPlan: MealPlanEntity) {
        mealPlanDao.insertMealPlan(mealPlan)
    }

    suspend fun updateMealPlan(mealPlan: MealPlanEntity) {
        mealPlanDao.updateMealPlan(mealPlan)
    }

    suspend fun deleteMealPlan(id: Int) {
        mealPlanDao.deleteMealPlan(id)
    }

    // --- Gemini AI Integration (via Retrofit) ---
    suspend fun generateContentWithGemini(prompt: String): String {
        return try {
            val request =
                GeminiRequest(contents = listOf(Content(parts = listOf(Part(text = prompt)))))
            val response = geminiApiService.generateContent(GEMINI_MODEL_NAME, GEMINI_API_KEY, request)

            if (response.isSuccessful) {
                response.body()?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    ?: "Could not generate content from AI."
            } else {
                val errorBody = response.errorBody()?.string()
                "Error from AI: ${response.code()} - ${errorBody ?: "Unknown error"}"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Network or API error: ${e.message}"
        }
    }

    suspend fun generateRecipeWithAI(ingredients: String): String {
        val prompt = "Generate a healthy recipe using these ingredients: $ingredients. Provide ingredients list, steps, and approximate cooking time. Format it clearly with 'Recipe: [Title]', 'Ingredients:', and 'Steps:' sections."
        return generateContentWithGemini(prompt)
    }

    suspend fun generateMealPlanWithAI(preferences: String, availableIngredients: String): String {
        val prompt = "Generate a 7-day healthy meal plan for someone with preferences: $preferences. Consider these available ingredients: $availableIngredients. Provide breakfast, lunch, and dinner for each day. Format clearly."
        return generateContentWithGemini(prompt)
    }

    suspend fun generateGroceryListWithAI(mealPlanDescription: String): String {
        val prompt = "Based on the following meal plan, generate a comprehensive grocery list with approximate quantities: $mealPlanDescription. List items clearly with quantities, e.g., '- 2 apples', '- 1 kg chicken breast'."
        return generateContentWithGemini(prompt)
    }

    suspend fun getHealthTipWithAI(topic: String): String {
        val prompt = "Provide a concise health tip about: $topic. Focus on healthy eating and daily habits."
        return generateContentWithGemini(prompt)
    }

    suspend fun generateHealthGoalWithAI(userPreferences: String): String {
        val prompt = "Suggest a personalized, achievable health goal for someone with preferences: $userPreferences. Focus on diet or simple exercise. Provide actionable steps."
        return generateContentWithGemini(prompt)
    }
}