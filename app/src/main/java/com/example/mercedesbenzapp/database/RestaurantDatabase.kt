package com.example.mercedesbenzapp.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mercedesbenzapp.model.domain.RestaurantDomain

@Database(
    entities = [RestaurantDomain::class],
    version = 1
)
abstract class RestaurantDatabase : RoomDatabase() {

    abstract fun getRestaurantsDao(): RestaurantsDAO

}