package com.paandaaa.homey.android.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.paandaaa.homey.android.data.local.entity.GroceryItemEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface GroceryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroceryItem(item: GroceryItemEntity)

    @Update
    suspend fun updateGroceryItem(item: GroceryItemEntity)

    @Delete
    suspend fun deleteGroceryItem(item: GroceryItemEntity)

    @Query("SELECT * FROM grocery_items ORDER BY name ASC")
    fun getAllGroceryItems(): Flow<List<GroceryItemEntity>>

    @Query("DELETE FROM grocery_items WHERE isChecked = 1")
    suspend fun deleteCheckedItems()
}