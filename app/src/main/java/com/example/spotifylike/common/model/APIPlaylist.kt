package com.example.spotifylike.common.model

data class APIPlaylist(
    val message: String? = null,
    val playlists: PlaylistsSubObject
) {

    data class PlaylistsSubObject(
        val href: String? = null,
        val items: Array<Playlist>,
        val limit: Int = 0,
        val next: String? = null,
        val offset: Int = 0,
        val previous: String? = null,
        val total: Int = 0
    )
}