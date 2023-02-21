package com.example.mercedesbenzapp.model


import com.google.gson.annotations.SerializedName

data class YelpResponse(
    @SerializedName("businesses")
    val businesses: List<Business>? = null,
    @SerializedName("region")
    val region: Region? = null,
    @SerializedName("total")
    val total: Int? = null
)