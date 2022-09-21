package com.example.spotifylike.common.model

data class Playlist(
    val collaborative: Boolean? = false,
    val description: String? = null,
    val external_urls: PlaylistsExternalUrls? = null,
    val href: String? = null,
    val id: String? = null,
    val images: Array<Image>? = null,
    val name: String? = null,
    val owner: PlaylistsOwner? = null,
    val primary_color: String? = null,
    val public: String? = null,
    val snapshot_id: String? = null,
    val tracks: PlaylistsTracks? = null,
    val type: String? = null,
    val uri: String? = null
) {

    data class PlaylistsExternalUrls(
        val spotify: String? = null
    )

    data class PlaylistsOwner(
        val display_name: String? = null,
        val external_urls: PlaylistsExternalUrls?,
        val href: String? = null,
        val id: String? = null,
        val type: String? = null,
        val uri: String? = null
    )

    data class PlaylistsTracks(
        val href: String? = null,
        val total: Int? = 0,
    )
}