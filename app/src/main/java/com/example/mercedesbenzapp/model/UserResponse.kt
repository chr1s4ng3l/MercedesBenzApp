package com.example.mercedesbenzapp.model


import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("possible_languages")
    val possibleLanguages: List<String?>? = null,
    @SerializedName("reviews")
    val reviews: List<Review>? = null,
    @SerializedName("total")
    val total: Int? = null
)