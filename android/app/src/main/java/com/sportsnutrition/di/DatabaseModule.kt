package com.sportsnutrition.di

import android.content.Context
import androidx.room.Room
import com.sportsnutrition.data.local.AppDatabase
import com.sportsnutrition.data.local.dao.DishDao
import com.sportsnutrition.data.local.dao.RestaurantDao
import com.sportsnutrition.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, Constants.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideRestaurantDao(db: AppDatabase): RestaurantDao = db.restaurantDao()

    @Provides
    fun provideDishDao(db: AppDatabase): DishDao = db.dishDao()
}
