package com.example.mercedesbenzapp.database

import com.example.mercedesbenzapp.model.domain.RestaurantDomain
import javax.inject.Inject


interface LocalRepository {

    fun insertRestaurants(restaurant: RestaurantDomain)


}

class LocalRepositoryImpl @Inject constructor(
    private val restaurantsDAO: RestaurantsDAO
) : LocalRepository {

    override fun insertRestaurants(restaurant: RestaurantDomain) {
        try {
            restaurantsDAO.insertRestaurant(restaurant)
        } catch (e: Exception) {
        }
    }


}