package com.example.mercedesbenzapp.rest

import com.example.mercedesbenzapp.model.YelpResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YelpApi {

    @GET(SEARCH_PATH)
    suspend fun getHotNewRestaurants(
        @Query("latitude") latitude: Double?,
        @Query("longitude") longitude: Double?,
        @Query("term") term: String = "restaurants",
        @Query("attributes") attributes: String = "hot_and_new",
        @Query("sort_by") sort: String = "best_match",
        @Query("limit") limit: Int = 20,
    ): Response<YelpResponse>

    companion object {
        //'https://api.yelp.com/v3/businesses/
        // search?latitude=33.903438
        // &longitude=-84.48234&term=restaurants
        // &attributes=hot_and_new&sort_by=best_match&limit=20'
        const val BASE_URL = "https://api.yelp.com/v3/businesses/"
        private const val SEARCH_PATH = "search"

    }
}