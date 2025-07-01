package com.paandaaa.homey.android.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paandaaa.homey.android.data.local.entity.MealPlanEntity
import com.paandaaa.homey.android.data.remote.HomeyRepository
import com.paandaaa.homey.android.ui.model.MealPlanUI
import com.paandaaa.homey.android.ui.model.RecipeUI
import com.paandaaa.homey.android.ui.model.toUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
data class MealPlanScreenState @RequiresApi(Build.VERSION_CODES.O) constructor(
    val selectedDate: LocalDate = LocalDate.now(),
    val mealPlans: List<MealPlanUI> = emptyList(),
    val availableRecipes: List<RecipeUI> = emptyList(),
    val showAddMealDialog: Boolean = false,
    val mealTypeToAdd: String = "Breakfast",
    val selectedRecipeId: Int? = null,
    val customMealName: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class MealPlanViewModel @Inject constructor(
    private val repository: HomeyRepository
) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    private val _uiState = MutableStateFlow(MealPlanScreenState())
    @RequiresApi(Build.VERSION_CODES.O)
    val uiState: StateFlow<MealPlanScreenState> = _uiState.asStateFlow()

    init {
        loadDataForSelectedDate()
        loadAvailableRecipes()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadDataForSelectedDate() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.getMealPlansForDate(_uiState.value.selectedDate.toString())
                .collect { mealPlanEntities ->
                    val mealPlansWithTitles = mealPlanEntities.map { entity ->
                        val recipe = entity.recipeId?.let { id ->
                            repository.getRecipeById(id).first() // Get recipe details
                        }
                        entity.toUI(recipeTitle = recipe?.title)
                    }
                    _uiState.value = _uiState.value.copy(
                        mealPlans = mealPlansWithTitles,
                        isLoading = false,
                        errorMessage = null
                    )
                }
        }
    }

    private fun loadAvailableRecipes() {
        viewModelScope.launch {
            repository.getSavedRecipes()
                .map { entities -> entities.map { it.toUI() } }
                .collect { recipes ->
                    _uiState.value = _uiState.value.copy(availableRecipes = recipes)
                }
        }
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
        loadDataForSelectedDate()
    }

    fun showAddMealDialog(mealType: String) {
        _uiState.value = _uiState.value.copy(
            showAddMealDialog = true,
            mealTypeToAdd = mealType,
            selectedRecipeId = null,
            customMealName = ""
        )
    }

    fun dismissAddMealDialog() {
        _uiState.value = _uiState.value.copy(showAddMealDialog = false)
    }

    fun onRecipeSelected(recipeId: Int?) {
        _uiState.value = _uiState.value.copy(selectedRecipeId = recipeId, customMealName = "")
    }

    fun onCustomMealNameChange(name: String) {
        _uiState.value = _uiState.value.copy(customMealName = name, selectedRecipeId = null)
    }

    fun addMealToPlan() {
        viewModelScope.launch {
            val date = _uiState.value.selectedDate.toString()
            val mealType = _uiState.value.mealTypeToAdd
            val recipeId = _uiState.value.selectedRecipeId
            val customName = _uiState.value.customMealName.trim().takeIf { it.isNotBlank() }

            if (recipeId == null && customName == null) {
                _uiState.value = _uiState.value.copy(errorMessage = "Please select a recipe or enter a custom meal name.")
                return@launch
            }

            val newMealPlan = MealPlanEntity(
                date = date,
                mealType = mealType,
                recipeId = recipeId,
                customMealName = customName
            )
            try {
                repository.saveMealPlan(newMealPlan)
                dismissAddMealDialog()
                _uiState.value = _uiState.value.copy(errorMessage = "Meal added successfully!")
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = "Failed to add meal: ${e.message}")
            }
        }
    }

    fun deleteMealPlan(mealPlanId: Int) {
        viewModelScope.launch {
            repository.deleteMealPlan(mealPlanId)
        }
    }

    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}