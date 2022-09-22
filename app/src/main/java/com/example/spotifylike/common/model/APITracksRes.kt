package com.example.spotifylike.common.model

data class APITracksRes(
    val href: String? = null,
    val items: Array<APITrack>,
    var limit: Number? = 0,
    val next: String? = null,
    val offset: Number? = 0,
    val previous: String? = null,
    val total: Int? = 0
) {
}