package com.sportsnutrition.di

import com.sportsnutrition.data.repository.AuthRepositoryImpl
import com.sportsnutrition.data.repository.DishRepositoryImpl
import com.sportsnutrition.data.repository.RestaurantRepositoryImpl
import com.sportsnutrition.domain.repository.AuthRepository
import com.sportsnutrition.domain.repository.DishRepository
import com.sportsnutrition.domain.repository.RestaurantRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindRestaurantRepository(impl: RestaurantRepositoryImpl): RestaurantRepository

    @Binds
    @Singleton
    abstract fun bindDishRepository(impl: DishRepositoryImpl): DishRepository
}
