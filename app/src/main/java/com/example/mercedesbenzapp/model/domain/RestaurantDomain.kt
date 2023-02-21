package com.example.mercedesbenzapp.model.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mercedesbenzapp.model.Business
import com.example.mercedesbenzapp.model.Location

@Entity(tableName = "restaurants")
data class RestaurantDomain(
    @PrimaryKey
    val id: String,
    val image: String,
    val name: String,
    val phone: String,
    val price: String,
    val rating: Double,
    val location: Location,
    val distance: Double
)

fun List<Business>?.mapToDomainRestaurants(): List<RestaurantDomain> =
    this?.map {
        RestaurantDomain(
            id = it.id ?: "ID not available",
            image = it.imageUrl ?: "not available",
            name = it.name ?: "not available",
            phone = it.displayPhone ?: "not available",
            price = it.price ?: "not available",
            rating = it.rating ?: 0.0,
            location = it.location ?: Location(address1 = "not available"),
            distance = it.distance ?: 0.0
        )
    } ?: emptyList()
