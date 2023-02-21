package com.example.mercedesbenzapp.model


import com.google.gson.annotations.SerializedName

data class Region(
    @SerializedName("center")
    val center: Center? = null
)