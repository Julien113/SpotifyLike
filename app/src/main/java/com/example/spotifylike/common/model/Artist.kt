package com.example.spotifylike.common.model

data class Artist(
    val externalUrls: ArtistExternalUrls? = null,
    val href: String? = null,
    val id: String? = null,
    val name: String? = null,
    val type: String? = null,
    val uri: String? = null
) {
    data class ArtistExternalUrls(
        val spotify: String? = null
    )

}