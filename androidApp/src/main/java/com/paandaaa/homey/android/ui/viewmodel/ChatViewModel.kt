package com.paandaaa.homey.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paandaaa.homey.android.data.local.entity.RecipeEntity
import com.paandaaa.homey.android.data.remote.HomeyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


data class ChatScreenState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val inputMessage: String = "",
    val showSaveRecipeDialog: Boolean = false,
    val recipeToSave: RecipeEntity? = null,
    val errorMessage: String? = null
)

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val isAiGeneratedRecipe: Boolean = false // Flag for AI generated recipes
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: HomeyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatScreenState())
    val uiState: StateFlow<ChatScreenState> = _uiState.asStateFlow()

    fun onInputMessageChange(newMessage: String) {
        _uiState.value = _uiState.value.copy(inputMessage = newMessage)
    }

    fun sendMessage() {
        val userMessage = _uiState.value.inputMessage.trim()
        if (userMessage.isBlank()) return

        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages + ChatMessage(userMessage, isUser = true),
            inputMessage = "",
            isLoading = true
        )

        viewModelScope.launch {
            val aiResponse: String
            var isAiRecipe = false
            try {
                // Simple keyword-based intent detection (can be more sophisticated with NLU)
                when {
                    userMessage.contains("recipe for", ignoreCase = true) -> {
                        val ingredients = userMessage.substringAfter("recipe for").trim()
                        aiResponse = repository.generateRecipeWithAI(ingredients)
                        isAiRecipe = true
                    }
                    userMessage.contains("meal plan", ignoreCase = true) -> {
                        val promptDetail = userMessage.substringAfter("meal plan").trim()
                        val availableIngredients = repository.getAllGroceryItems().first().joinToString { it.name }
                        aiResponse = repository.generateMealPlanWithAI(promptDetail, availableIngredients)
                    }
                    userMessage.contains("grocery list", ignoreCase = true) -> {
                        val mealPlanDescription = userMessage.substringAfter("grocery list").trim()
                        aiResponse = repository.generateGroceryListWithAI(mealPlanDescription)
                    }
                    userMessage.contains("health tip about", ignoreCase = true) -> {
                        val topic = userMessage.substringAfter("health tip about").trim()
                        aiResponse = repository.getHealthTipWithAI(topic)
                    }
                    userMessage.contains("health goal for", ignoreCase = true) -> {
                        val preferences = userMessage.substringAfter("health goal for").trim()
                        aiResponse = repository.generateHealthGoalWithAI(preferences)
                    }
                    else -> {
                        aiResponse = repository.generateContentWithGemini(userMessage)
                    }
                }

                _uiState.value = _uiState.value.copy(
                    messages = _uiState.value.messages + ChatMessage(aiResponse, isUser = false, isAiGeneratedRecipe = isAiRecipe),
                    isLoading = false,
                    errorMessage = null
                )

                // If it's an AI-generated recipe, prepare for saving
                if (isAiRecipe && aiResponse.contains("Ingredients:") && aiResponse.contains("Steps:")) {
                    val parsedRecipe = parseAiRecipe(aiResponse)
                    _uiState.value = _uiState.value.copy(
                        showSaveRecipeDialog = true,
                        recipeToSave = parsedRecipe
                    )
                }

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    messages = _uiState.value.messages + ChatMessage("Error: ${e.message}", isUser = false),
                    isLoading = false,
                    errorMessage = "Failed to get AI response: ${e.message}"
                )
            }
        }
    }

    // Helper to parse AI-generated recipe into a savable RecipeEntity
    private fun parseAiRecipe(aiResponse: String): RecipeEntity? {
        val titleRegex = Regex("(?i)Recipe: (.+)")
        val ingredientsRegex = Regex("(?i)Ingredients:\\s*\\n([\\s\\S]+?)(?=\\n\\nSteps:|$|Approximate Cooking Time:)")
        val instructionsRegex = Regex("(?i)Steps:\\s*\\n([\\s\\S]+?)(?=\\n\\nApproximate Cooking Time:|$|Ingredients:)")

        val titleMatch = titleRegex.find(aiResponse)
        val ingredientsMatch = ingredientsRegex.find(aiResponse)
        val instructionsMatch = instructionsRegex.find(aiResponse)

        val title = titleMatch?.groupValues?.getOrNull(1)?.trim() ?: "AI Generated Recipe"
        val ingredients = ingredientsMatch?.groupValues?.getOrNull(1)?.split("\n")?.map { it.trim().removePrefix("- ").removePrefix("* ") }?.filter { it.isNotBlank() } ?: emptyList()
        val instructions = instructionsMatch?.groupValues?.getOrNull(1)?.trim() ?: "No instructions provided."

        if (ingredients.isNotEmpty() && instructions.isNotBlank()) {
            return RecipeEntity(
                title = title,
                imageUrl = null, // AI doesn't generate images easily, leave null or provide a default
                ingredients = ingredients,
                instructions = instructions,
                isFavorite = false,
                generatedByAi = true,
                remoteId = null, // or "" or a UUID if needed
            )
        }
        return null
    }

    fun saveAiGeneratedRecipe() {
        viewModelScope.launch {
            _uiState.value.recipeToSave?.let { recipe ->
                repository.saveRecipe(recipe)
                _uiState.value = _uiState.value.copy(
                    showSaveRecipeDialog = false,
                    recipeToSave = null,
                    errorMessage = "Recipe saved successfully!"
                )
            } ?: run {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "No recipe to save."
                )
            }
        }
    }

    fun dismissSaveRecipeDialog() {
        _uiState.value = _uiState.value.copy(
            showSaveRecipeDialog = false,
            recipeToSave = null
        )
    }

    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
