package com.paandaaa.homey.android.ui.screens.mealplan

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.paandaaa.homey.android.ui.model.MealPlanUI
import com.paandaaa.homey.android.ui.viewmodel.MealPlanViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlanScreen(viewModel: MealPlanViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearErrorMessage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {


        // Date Selector Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.onDateSelected(uiState.selectedDate.minusDays(1)) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Day")
            }
            Text(
                text = uiState.selectedDate.dayOfWeek.name + ", " + uiState.selectedDate.month.name + " " + uiState.selectedDate.dayOfMonth,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            IconButton(onClick = { viewModel.onDateSelected(uiState.selectedDate.plusDays(1)) }) {
                Icon(Icons.Filled.Add, contentDescription = "Next Day")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Text("Loading meal plan...", modifier = Modifier.padding(top = 8.dp))
        } else {
            val mealsByTime = uiState.mealPlans.groupBy { it.mealType }
            val mealTypes = listOf("Breakfast", "Lunch", "Dinner")

            mealTypes.forEach { mealType ->
                MealTypeSection(
                    mealType = mealType,
                    meals = mealsByTime[mealType] ?: emptyList(),
                    onAddMealClick = { viewModel.showAddMealDialog(mealType) },
                    onDeleteMealClick = viewModel::deleteMealPlan
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }

    if (uiState.showAddMealDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissAddMealDialog() },
            title = { Text("Add ${uiState.mealTypeToAdd}") },
            text = {
                Column {
                    Text("Choose a recipe or enter a custom meal name.")
                    Spacer(Modifier.height(8.dp))
                    var expanded by remember { mutableStateOf(false) }

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = uiState.availableRecipes.find { it.id == uiState.selectedRecipeId }?.title ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select Recipe") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            uiState.availableRecipes.forEach { recipe ->
                                DropdownMenuItem(
                                    text = { Text(recipe.title) },
                                    onClick = {
                                        viewModel.onRecipeSelected(recipe.id)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("OR", modifier = Modifier.align(Alignment.CenterHorizontally))
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.customMealName,
                        onValueChange = viewModel::onCustomMealNameChange,
                        label = { Text("Custom Meal Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(onClick = viewModel::addMealToPlan) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissAddMealDialog() }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun MealTypeSection(
    mealType: String,
    meals: List<MealPlanUI>,
    onAddMealClick: () -> Unit,
    onDeleteMealClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = mealType, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Button(
                onClick = onAddMealClick,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Add Meal")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (meals.isEmpty()) {
            Text("No ${mealType.lowercase()} planned.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        } else {
            meals.forEach { meal ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = meal.recipeTitle ?: meal.customMealName ?: "Unnamed Meal",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { meal.id.let(onDeleteMealClick) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Meal", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}