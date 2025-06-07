package com.practicum.kotlinplaylistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    fun searchTracks(
        @Query("term") query: String,
        @Query("entity") entity: String = "song"
    ): Call<TracksResponse>
}