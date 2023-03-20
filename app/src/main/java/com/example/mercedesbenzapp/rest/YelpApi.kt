package com.example.mercedesbenzapp.rest

import com.example.mercedesbenzapp.model.User
import com.example.mercedesbenzapp.model.UserResponse
import com.example.mercedesbenzapp.model.YelpResponse
import com.example.mercedesbenzapp.rest.YelpApi.Companion.USERS_PATH
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET(PATH_ID + USERS_PATH)
    suspend fun getUsersReview(
        @Path("id") id: String,
        @Query("limit") limit: Int = 3,
        @Query("sort_by") sorting: String = "yelp_sort"
    ): Response<UserResponse>

    companion object {
        //'https://api.yelp.com/v3/businesses/
        // search?latitude=33.903438
        // &longitude=-84.48234&term=restaurants
        // &attributes=hot_and_new&sort_by=best_match&limit=20'
        //https://api.yelp.com/v3/businesses/business_id_or_alias/reviews?limit=3&sort_by=yelp_sort
        const val BASE_URL = "https://api.yelp.com/v3/businesses/"
        private const val SEARCH_PATH = "search"
        private const val PATH_ID = "{id}/"
        private const val USERS_PATH = "reviews"

    }
}