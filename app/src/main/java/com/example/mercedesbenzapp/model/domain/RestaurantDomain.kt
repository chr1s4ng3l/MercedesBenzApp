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
    val distance: Double
)

fun List<Business>?.mapToDomainRestaurants(): List<RestaurantDomain> =
    this?.map {
        RestaurantDomain(
            id = it.id ?: "ID not available",
            image = it.imageUrl ?: "Image not available",
            name = it.name ?: "Name not available",
            phone = it.displayPhone ?: "Phone not available",
            price = it.price ?: "$$ not available",
            rating = it.rating ?: 0.0,
            distance = it.distance ?: 0.0
        )
    } ?: emptyList()
