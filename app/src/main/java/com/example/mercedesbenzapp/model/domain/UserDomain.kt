package com.example.mercedesbenzapp.model.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mercedesbenzapp.model.Review
import com.example.mercedesbenzapp.model.User


@Entity(tableName = "users")
data class UserDomain(

    @PrimaryKey
    val id: String,
    val rating: Double,
    val user: User,
    val urlImage: String,
    val date: String,
    val review: String

)

fun List<Review>?.mapToUsers(): List<UserDomain> =
    this?.map {
        UserDomain(
            id = it.id ?: "ID not available",
            urlImage = it.url ?: "Image not available",
            user = it.user ?: User(
                id = "Id not available",
                name = "Name not available",
                profileUrl = "",
                imageUrl = ""
            ),
            date = it.timeCreated ?: "Date not available",
            review = it.text ?: "Review not available",
            rating = it.rating ?: 0.0
        )
    } ?: emptyList()