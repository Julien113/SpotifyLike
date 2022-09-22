package com.example.spotifylike.common.services.api

import com.example.spotifylike.common.model.APIPlaylist
import com.example.spotifylike.common.model.APITracksRes
import com.example.spotifylike.common.model.Playlist
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface SpotifyApiService {

    @GET("v1/browse/featured-playlists")
    suspend fun getListOfFeaturedPlaylists(): Response<APIPlaylist>

    @GET("v1/playlists/{playListId}")
    suspend fun getPlaylistInformation(@Path("playListId") playListId: String): Response<Playlist>

    @Headers("fields: artists,duration_ms,name,preview_url")
    @GET("v1/playlists/{playListId}/tracks")
    suspend fun getPlaylistTracks(@Path("playListId") playListId: String): Response<APITracksRes>
}