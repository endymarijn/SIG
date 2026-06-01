package com.sportsnutrition.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sportsnutrition.data.local.dao.DishDao
import com.sportsnutrition.data.local.dao.RestaurantDao
import com.sportsnutrition.data.local.entities.DishEntity
import com.sportsnutrition.data.local.entities.RestaurantEntity
import com.sportsnutrition.utils.Constants

@Database(
    entities = [RestaurantEntity::class, DishEntity::class],
    version = Constants.DB_VERSION,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao
    abstract fun dishDao(): DishDao
}
