package com.sportsnutrition.data.api

import com.sportsnutrition.data.api.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Auth
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("auth/me")
    suspend fun getMe(): Response<ProfileResponse>

    // Restaurants
    @GET("restaurants")
    suspend fun getRestaurants(
        @Query("search") search: String? = null,
        @Query("city") city: String? = null,
        @Query("cuisine") cuisine: String? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
    ): Response<RestaurantsResponse>

    @GET("restaurants/{id}")
    suspend fun getRestaurantById(@Path("id") id: String): Response<RestaurantResponse>

    // Dishes
    @GET("dishes")
    suspend fun getDishes(
        @Query("restaurantId") restaurantId: String? = null,
        @Query("search") search: String? = null,
        @Query("tags") tags: String? = null,
        @Query("minProtein") minProtein: Double? = null,
        @Query("maxCalories") maxCalories: Double? = null,
        @Query("page") page: Int = 1,
    ): Response<DishesResponse>

    @GET("dishes/{id}")
    suspend fun getDishById(@Path("id") id: String): Response<DishResponse>

    // Favorites
    @GET("favorites")
    suspend fun getFavorites(@Query("type") type: String? = null): Response<FavoritesResponse>

    @POST("favorites")
    suspend fun addFavorite(@Body request: AddFavoriteRequest): Response<Any>

    @DELETE("favorites/{id}")
    suspend fun removeFavorite(@Path("id") id: String): Response<Any>

    // Profile
    @GET("profile")
    suspend fun getProfile(): Response<ProfileResponse>

    @PUT("profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<ProfileResponse>
}
