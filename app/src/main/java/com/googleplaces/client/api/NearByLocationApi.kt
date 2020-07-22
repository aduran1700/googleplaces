package com.googleplaces.client.api

import com.googleplaces.client.model.NearByPlaces
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NearByLocationApi {

    @GET("/maps/api/place/nearbysearch/json")
    fun getLocations(
        @Query("location") location: String,
        @Query("radius") radius: Int
    ): Single<NearByPlaces>
}