package com.sportsnutrition.data.local.dao

import androidx.room.*
import com.sportsnutrition.data.local.entities.RestaurantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {
    @Query("SELECT * FROM restaurants ORDER BY name ASC")
    fun getAllRestaurants(): Flow<List<RestaurantEntity>>

    @Query("SELECT * FROM restaurants WHERE id = :id")
    suspend fun getRestaurantById(id: String): RestaurantEntity?

    @Query("SELECT * FROM restaurants WHERE city LIKE '%' || :city || '%'")
    fun getRestaurantsByCity(city: String): Flow<List<RestaurantEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(restaurants: List<RestaurantEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(restaurant: RestaurantEntity)

    @Query("DELETE FROM restaurants")
    suspend fun deleteAll()

    @Query("SELECT MAX(cachedAt) FROM restaurants")
    suspend fun getLastCacheTime(): Long?
}
