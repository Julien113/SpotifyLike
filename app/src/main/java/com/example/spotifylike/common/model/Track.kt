package com.example.spotifylike.common.model

data class Track(/*val album: Album,*/


    val artists: Array<Artist>? = null,
    //val available_markets: Array<String>? = null,
    //val disc_number: Number? = 0,
    val duration_ms: Number = 0,
    //val episode: Boolean? = false,
    //val explicit: Boolean? = false,
    //val external_ids: APITrack.TrackExternalUrls? = null,
    //val external_urls: APITrack.TrackExternalUrls? = null,
    val name: String? = null,
    val preview_url: String? = null
) {
}