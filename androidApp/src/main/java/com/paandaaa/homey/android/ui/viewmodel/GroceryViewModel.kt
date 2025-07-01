package com.paandaaa.homey.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paandaaa.homey.android.data.local.entity.GroceryItemEntity
import com.paandaaa.homey.android.data.remote.HomeyRepository
import com.paandaaa.homey.android.ui.model.GroceryItemUI
import com.paandaaa.homey.android.ui.model.toEntity
import com.paandaaa.homey.android.ui.model.toUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.map


data class GroceryListState(
    val items: List<GroceryItemUI> = emptyList(),
    val newItemName: String = "",
    val newItemQuantity: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showGenerateGroceryListDialog: Boolean = false,
    val groceryGenerationPrompt: String = ""
)

@HiltViewModel
class GroceryViewModel @Inject constructor(
    private val repository: HomeyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroceryListState())
    val uiState: StateFlow<GroceryListState> = _uiState.asStateFlow()

    init {
        loadGroceryItems()
    }

    private fun loadGroceryItems() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.getAllGroceryItems()
                .map { entities -> entities.map { it.toUI() } }
                .collect { items ->
                    _uiState.value = _uiState.value.copy(
                        items = items,
                        isLoading = false,
                        errorMessage = null
                    )
                }
        }
    }

    fun onNewItemNameChange(name: String) {
        _uiState.value = _uiState.value.copy(newItemName = name)
    }

    fun onNewItemQuantityChange(quantity: String) {
        _uiState.value = _uiState.value.copy(newItemQuantity = quantity)
    }

    fun addGroceryItem() {
        val name = _uiState.value.newItemName.trim()
        val quantity = _uiState.value.newItemQuantity.trim()
        if (name.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Item name cannot be empty.")
            return
        }
        viewModelScope.launch {
            try {
                repository.addGroceryItem(GroceryItemEntity(name = name, quantity = quantity))
                _uiState.value = _uiState.value.copy(newItemName = "", newItemQuantity = "", errorMessage = null)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = "Failed to add item: ${e.message}")
            }
        }
    }

    fun toggleItemChecked(item: GroceryItemUI) {
        viewModelScope.launch {
            repository.updateGroceryItem(item.copy(isChecked = !item.isChecked).toEntity())
        }
    }

    fun deleteItem(item: GroceryItemUI) {
        viewModelScope.launch {
            repository.deleteGroceryItem(item.toEntity())
        }
    }

    fun clearCheckedItems() {
        viewModelScope.launch {
            repository.clearCheckedGroceryItems()
        }
    }

    fun showGenerateGroceryListDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showGenerateGroceryListDialog = show, groceryGenerationPrompt = "")
    }

    fun onGroceryGenerationPromptChange(prompt: String) {
        _uiState.value = _uiState.value.copy(groceryGenerationPrompt = prompt)
    }

    fun generateAndAddGroceryList() {
        val prompt = _uiState.value.groceryGenerationPrompt.trim()
        if (prompt.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Prompt cannot be empty.")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, showGenerateGroceryListDialog = false)
            try {
                val aiResponse = repository.generateGroceryListWithAI(prompt)
                val newItems = parseAiGroceryList(aiResponse)
                newItems.forEach { item ->
                    repository.addGroceryItem(item.toEntity())
                }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = if (newItems.isEmpty()) "AI generated no new items." else null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to generate grocery list: ${e.message}"
                )
            }
        }
    }

    private fun parseAiGroceryList(aiResponse: String): List<GroceryItemUI> {
        val items = mutableListOf<GroceryItemUI>()
        val lines = aiResponse.split("\n").filter { it.isNotBlank() }
        val itemRegex = Regex("^[*-]?\\s*(\\d+\\s*[a-zA-Z]*\\s*)?(.+)") // Matches optional quantity and item name

        for (line in lines) {
            val match = itemRegex.find(line.trim())
            if (match != null) {
                val quantity = match.groupValues.getOrNull(1)?.trim() ?: ""
                val name = match.groupValues.getOrNull(2)?.trim() ?: ""
                if (name.isNotBlank()) {
                    items.add(GroceryItemUI(name = name, quantity = quantity, isChecked = false))
                }
            }
        }
        return items
    }

    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}