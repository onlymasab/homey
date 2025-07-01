package com.paandaaa.homey.android.ui.screens.grocery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.paandaaa.homey.android.ui.model.GroceryItemUI
import com.paandaaa.homey.android.ui.viewmodel.GroceryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryScreen(viewModel: GroceryViewModel = hiltViewModel()) {
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
        // Optional top bar (can be removed if parent handles it)


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = uiState.newItemName,
                onValueChange = viewModel::onNewItemNameChange,
                label = { Text("Item Name") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            OutlinedTextField(
                value = uiState.newItemQuantity,
                onValueChange = viewModel::onNewItemQuantityChange,
                label = { Text("Quantity") },
                modifier = Modifier.width(100.dp),
                singleLine = true
            )
            Button(
                onClick = viewModel::addGroceryItem,
                enabled = uiState.newItemName.isNotBlank()
            ) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.showGenerateGroceryListDialog(true) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) {
            Text("Generate Grocery List with AI")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Text("Loading grocery items...", modifier = Modifier.padding(top = 8.dp))
        } else if (uiState.items.isEmpty()) {
            Text("Your grocery list is empty. Add some items or ask AI for a list!")
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(uiState.items) { item ->
                    GroceryItemCard(item = item, viewModel = viewModel)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = viewModel::clearCheckedItems,
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.items.any { it.isChecked },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Clear Checked Items")
            }
        }
    }

    if (uiState.showGenerateGroceryListDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.showGenerateGroceryListDialog(false) },
            title = { Text("Generate Grocery List") },
            text = {
                Column {
                    Text("Enter details for the grocery list you want AI to generate (e.g., 'for a week of healthy vegan meals').")
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.groceryGenerationPrompt,
                        onValueChange = viewModel::onGroceryGenerationPromptChange,
                        label = { Text("Prompt for AI") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 5
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = viewModel::generateAndAddGroceryList,
                    enabled = uiState.groceryGenerationPrompt.isNotBlank()
                ) {
                    Text("Generate & Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.showGenerateGroceryListDialog(false) }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun GroceryItemCard(item: GroceryItemUI, viewModel: GroceryViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Checkbox(
                    checked = item.isChecked,
                    onCheckedChange = { viewModel.toggleItemChecked(item) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (item.isChecked) Color.Gray else MaterialTheme.colorScheme.onSurface
                    )
                    if (item.quantity.isNotBlank()) {
                        Text(
                            text = item.quantity,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (item.isChecked) Color.Gray else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            IconButton(onClick = { viewModel.deleteItem(item) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Item", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}