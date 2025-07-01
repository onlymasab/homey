package com.paandaaa.homey.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.paandaaa.homey.android.data.local.entity.MealPlanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealPlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealPlan(mealPlan: MealPlanEntity)

    @Update
    suspend fun updateMealPlan(mealPlan: MealPlanEntity)

    @Query("SELECT * FROM meal_plans WHERE date = :date")
    fun getMealPlansForDate(date: String): Flow<List<MealPlanEntity>>

    @Query("SELECT * FROM meal_plans ORDER BY date ASC")
    fun getAllMealPlans(): Flow<List<MealPlanEntity>>

    @Query("DELETE FROM meal_plans WHERE id = :id")
    suspend fun deleteMealPlan(id: Int)
}