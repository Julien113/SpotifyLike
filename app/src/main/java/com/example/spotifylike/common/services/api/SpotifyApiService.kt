package com.example.spotifylike.common.services.api

import com.example.spotifylike.common.model.APIPlaylist
import retrofit2.Response
import retrofit2.http.GET

interface SpotifyApiService {

    @GET("v1/browse/featured-playlists")
    suspend fun getListOfFeaturedPlaylists(): Response<APIPlaylist>
}