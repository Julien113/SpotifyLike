package com.example.spotifylike.common.model

data class Track(
    val album: Album? = null,
    val artists: Array<Artist>? = null,
    val duration_ms: Long = 0,
    val name: String? = null,
    val preview_url: String? = null
) {
}