package com.example.mercedesbenzapp.model


import com.google.gson.annotations.SerializedName

data class Business(
    @SerializedName("alias")
    val alias: String? = null,
    @SerializedName("categories")
    val categories: List<Category>? = null,
    @SerializedName("coordinates")
    val coordinates: Coordinates? = null,
    @SerializedName("display_phone")
    val displayPhone: String? = null,
    @SerializedName("distance")
    val distance: Double? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("image_url")
    val imageUrl: String? = null,
    @SerializedName("is_closed")
    val isClosed: Boolean? = null,
    @SerializedName("location")
    val location: Location? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("price")
    val price: String? = null,
    @SerializedName("rating")
    val rating: Double? = null,
    @SerializedName("review_count")
    val reviewCount: Int? = null,
    @SerializedName("transactions")
    val transactions: List<String>? = null,
    @SerializedName("url")
    val url: String? = null
)