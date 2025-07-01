package com.paandaaa.homey.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paandaaa.homey.android.data.remote.HomeyRepository
import com.paandaaa.homey.android.ui.model.RecipeUI
import com.paandaaa.homey.android.ui.model.toEntity
import com.paandaaa.homey.android.ui.model.toUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

data class RecipeListState(
    val recipes: List<RecipeUI> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedRecipe: RecipeUI? = null // For detail screen
)

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: HomeyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeListState())
    val uiState: StateFlow<RecipeListState> = _uiState.asStateFlow()

    init {
        // Combine search query and all recipes flow
        viewModelScope.launch {
            combine(
                repository.getSavedRecipes(),
                _uiState.map { it.searchQuery }
            ) { allRecipes, searchQuery ->
                if (searchQuery.isBlank()) {
                    allRecipes.map { it.toUI() }
                } else {
                    allRecipes.filter {
                        it.title.contains(searchQuery, ignoreCase = true) ||
                                it.ingredients.any { ingredient -> ingredient.contains(searchQuery, ignoreCase = true) }
                    }.map { it.toUI() }
                }
            }.collect { filteredRecipes ->
                _uiState.value = _uiState.value.copy(
                    recipes = filteredRecipes,
                    isLoading = false,
                    errorMessage = null
                )
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun toggleFavorite(recipe: RecipeUI) {
        viewModelScope.launch {
            repository.updateRecipe(recipe.copy(isFavorite = !recipe.isFavorite).toEntity())
        }
    }

    fun deleteRecipe(recipeId: Int) {
        viewModelScope.launch {
            repository.deleteRecipe(recipeId)
        }
    }

    fun getRecipeDetails(recipeId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.getRecipeById(recipeId).collect { recipeEntity ->
                _uiState.value = _uiState.value.copy(
                    selectedRecipe = recipeEntity?.toUI(),
                    isLoading = false,
                    errorMessage = if (recipeEntity == null) "Recipe not found." else null
                )
            }
        }
    }
}
