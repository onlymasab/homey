package com.paandaaa.homey.android.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.paandaaa.homey.android.data.local.converter.ListConverter
import com.paandaaa.homey.android.data.local.dao.GroceryDao
import com.paandaaa.homey.android.data.local.dao.MealPlanDao
import com.paandaaa.homey.android.data.local.dao.RecipeDao
import com.paandaaa.homey.android.data.local.entity.GroceryItemEntity
import com.paandaaa.homey.android.data.local.entity.MealPlanEntity
import com.paandaaa.homey.android.data.local.entity.RecipeEntity

@Database(
    entities = [RecipeEntity::class, GroceryItemEntity::class, MealPlanEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun groceryDao(): GroceryDao
    abstract fun mealPlanDao(): MealPlanDao
}