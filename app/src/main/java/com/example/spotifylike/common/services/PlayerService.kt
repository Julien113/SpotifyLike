package com.example.spotifylike.common.services

import com.example.spotifylike.common.model.APIPlaylist
import com.example.spotifylike.common.services.api.SpotifyApiClient
import com.example.spotifylike.common.utils.ObjectCallback


class PlayerService(private val mainService: MainService) {

    fun getListOfFeaturedPlaylists(callback: ObjectCallback<APIPlaylist>) {
        mainService.httpCallForObject(callback, SpotifyApiClient.apiService::getListOfFeaturedPlaylists)
    }
}