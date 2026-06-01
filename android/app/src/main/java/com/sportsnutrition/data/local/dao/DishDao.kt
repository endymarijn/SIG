package com.sportsnutrition.data.local.dao

import androidx.room.*
import com.sportsnutrition.data.local.entities.DishEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DishDao {
    @Query("SELECT * FROM dishes WHERE restaurantId = :restaurantId")
    fun getDishesByRestaurant(restaurantId: String): Flow<List<DishEntity>>

    @Query("SELECT * FROM dishes WHERE id = :id")
    suspend fun getDishById(id: String): DishEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dishes: List<DishEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dish: DishEntity)

    @Query("DELETE FROM dishes WHERE restaurantId = :restaurantId")
    suspend fun deleteByRestaurant(restaurantId: String)
}
