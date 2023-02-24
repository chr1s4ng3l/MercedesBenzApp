package com.example.mercedesbenzapp.rest

import com.example.mercedesbenzapp.model.domain.RestaurantDomain
import com.example.mercedesbenzapp.model.domain.UserDomain
import com.example.mercedesbenzapp.model.domain.mapToDomainRestaurants
import com.example.mercedesbenzapp.model.domain.mapToUsers
import com.example.mercedesbenzapp.utils.FailureResponse
import com.example.mercedesbenzapp.utils.NullCocktailResponse
import com.example.mercedesbenzapp.utils.UIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface MercedesRepository {
    suspend fun getRestaurants(lat: Double, long: Double): Flow<UIState<List<RestaurantDomain>>>
    suspend fun getUsers(id: String): Flow<UIState<List<UserDomain>>>


}

class MercedesRepositoryImpl @Inject constructor(private val api: YelpApi) : MercedesRepository {
    override suspend fun getRestaurants(
        lat: Double,
        long: Double
    ): Flow<UIState<List<RestaurantDomain>>> = flow {
        emit(UIState.LOADING)
        try {
            val response = api.getHotNewRestaurants(lat, long)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(UIState.SUCCESS(it.businesses.mapToDomainRestaurants()))
                } ?: throw NullCocktailResponse() //check if response was null
            } else throw FailureResponse(response.errorBody()?.string())
        } catch (e: Exception) {
            emit(UIState.ERROR(e))
        }
    }

    override suspend fun getUsers(id: String): Flow<UIState<List<UserDomain>>> = flow {
        emit(UIState.LOADING)
        try {
            val response = api.getUsersReview(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(UIState.SUCCESS(it.reviews.mapToUsers()))
                } ?: throw NullCocktailResponse() //check if response was null
            } else throw FailureResponse(response.errorBody()?.string())
        } catch (e: Exception) {
            emit(UIState.ERROR(e))
        }

    }

}

