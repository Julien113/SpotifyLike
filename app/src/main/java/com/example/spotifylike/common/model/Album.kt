package com.example.spotifylike.common.model

data class Album(
    val images: Array<Image>,
    val artists: Array<Artist>? = null,
    val name: String? = null
)