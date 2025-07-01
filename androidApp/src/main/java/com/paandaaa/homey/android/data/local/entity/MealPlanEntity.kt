package com.paandaaa.homey.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_plans")
data class MealPlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String, // Store as String "YYYY-MM-DD"
    val mealType: String, // e.g., "Breakfast", "Lunch", "Dinner"
    val recipeId: Int?, // Foreign key to RecipeEntity
    val customMealName: String? // For meals not linked to a specific recipe
)