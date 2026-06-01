package com.sportsnutrition.domain.repository

import com.sportsnutrition.domain.models.Favorite
import com.sportsnutrition.domain.models.FavoriteType
import com.sportsnutrition.utils.Result
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavorites(type: FavoriteType? = null): Flow<Result<List<Favorite>>>
    suspend fun addFavorite(type: FavoriteType, itemId: String): Result<Favorite>
    suspend fun removeFavorite(favoriteId: String): Result<Unit>
}
