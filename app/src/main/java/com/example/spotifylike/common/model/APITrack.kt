package com.example.spotifylike.common.model

import java.util.*

data class APITrack(
    val added_at: Date? = null,
    val added_by: TrackAddedBy,
    val is_local: Boolean = false,
    val primary_color: String? = null,
    val track: Track? = null,
    val video_thumbnail: TrackVideoThumbnail? = null
) {

    data class TrackAddedBy(
        val external_urls: TrackExternalUrls? = null,
        val href: String? = null,
        val id: String? = null,
        val type: String? = null,
        val uri: String? = null
    )

    data class TrackExternalUrls(
        val spotify: String? = null
    )

    data class TrackVideoThumbnail(
        val url: String? = null
    )
}