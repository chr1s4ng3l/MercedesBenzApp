package com.example.mercedesbenzapp.model


import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("address1")
    val address1: String? = null,
    @SerializedName("address2")
    val address2: String? = null,
    @SerializedName("address3")
    val address3: String? = null,
    @SerializedName("city")
    val city: String? = null,
    @SerializedName("country")
    val country: String? = null,
    @SerializedName("display_address")
    val displayAddress: List<String?>? = null,
    @SerializedName("state")
    val state: String? = null,
    @SerializedName("zip_code")
    val zipCode: String? = null
)