package com.example.spotifylike.common.services

import com.example.spotifylike.common.model.APIPlaylist
import com.example.spotifylike.common.model.APITrack
import com.example.spotifylike.common.model.APITracksRes
import com.example.spotifylike.common.model.Playlist
import com.example.spotifylike.common.services.api.SpotifyApiClient
import com.example.spotifylike.common.utils.ObjectCallback


class PlaylistService(private val mainService: MainService) {

    fun getListOfFeaturedPlaylists(callback: ObjectCallback<APIPlaylist>) {
        mainService.httpCallForObject(callback, SpotifyApiClient.apiService::getListOfFeaturedPlaylists)
    }

    fun getPlaylistInformations(playlistId: String, callback: ObjectCallback<Playlist>) {
        mainService.httpCallForObjectWithParams(callback, SpotifyApiClient.apiService::getPlaylistInformation, playlistId)
    }

    fun getPlaylistTracks(playlistId: String, callback: ObjectCallback<APITracksRes>) {
        mainService.httpCallForObjectWithParams(callback, SpotifyApiClient.apiService::getPlaylistTracks, playlistId)
    }
}