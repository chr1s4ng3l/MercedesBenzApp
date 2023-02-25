package com.example.mercedesbenzapp.database

import androidx.room.*
import com.example.mercedesbenzapp.model.domain.RestaurantDomain

@Dao
interface RestaurantsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRestaurant(vararg restaurant: RestaurantDomain)

    @Delete
    fun deleteRestaurant(vararg restaurant: RestaurantDomain)

    @Query("SELECT * FROM restaurants")
    suspend fun getRestaurants(): List<RestaurantDomain>

}