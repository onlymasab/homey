package com.paandaaa.homey.android.ui.model

import com.paandaaa.homey.android.data.local.entity.GroceryItemEntity
import com.paandaaa.homey.android.data.local.entity.MealPlanEntity
import com.paandaaa.homey.android.data.local.entity.RecipeEntity

data class RecipeUI(
    val id: Int = 0,
    val remoteId: String?,
    val title: String,
    val imageUrl: String?,
    val ingredients: List<String>,
    val instructions: String,
    val isFavorite: Boolean,
    val generatedByAi: Boolean
)

fun RecipeEntity.toUI(): RecipeUI {
    return RecipeUI(
        id = id,
        remoteId = remoteId,
        title = title,
        imageUrl = imageUrl,
        ingredients = ingredients,
        instructions = instructions,
        isFavorite = isFavorite,
        generatedByAi = generatedByAi
    )
}

fun RecipeUI.toEntity(): RecipeEntity {
    return RecipeEntity(
        id = id,
        remoteId = remoteId,
        title = title,
        imageUrl = imageUrl,
        ingredients = ingredients,
        instructions = instructions,
        isFavorite = isFavorite,
        generatedByAi = generatedByAi
    )
}

data class GroceryItemUI(
    val id: Int = 0,
    val name: String,
    val quantity: String,
    val isChecked: Boolean
)

fun GroceryItemEntity.toUI(): GroceryItemUI {
    return GroceryItemUI(
        id = id,
        name = name,
        quantity = quantity,
        isChecked = isChecked
    )
}

fun GroceryItemUI.toEntity(): GroceryItemEntity {
    return GroceryItemEntity(
        id = id,
        name = name,
        quantity = quantity,
        isChecked = isChecked
    )
}

data class MealPlanUI(
    val id: Int = 0,
    val date: String,
    val mealType: String,
    val recipeId: Int?,
    val customMealName: String?,
    val recipeTitle: String? = null // For displaying in UI
)

fun MealPlanEntity.toUI(recipeTitle: String? = null): MealPlanUI {
    return MealPlanUI(
        id = id,
        date = date,
        mealType = mealType,
        recipeId = recipeId,
        customMealName = customMealName,
        recipeTitle = recipeTitle
    )
}

fun MealPlanUI.toEntity(): MealPlanEntity {
    return MealPlanEntity(
        id = id,
        date = date,
        mealType = mealType,
        recipeId = recipeId,
        customMealName = customMealName
    )
}

data class HealthTipUI(
    val id: Int = 0, // Not persisted, just for display
    val content: String
)