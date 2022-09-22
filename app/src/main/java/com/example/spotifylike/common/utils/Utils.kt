package com.example.spotifylike.common.utils

import com.example.spotifylike.common.model.Artist
import java.util.concurrent.TimeUnit


class Utils {

    companion object {
        fun artistsNames(artists: Array<Artist>): String {
            var res = ""
            for ((index, artist) in artists.withIndex()) {
                if (index > 0) {
                    res = "$res,"
                }
                res = "$res${artist.name}"
            }
            return res
        }

        fun millisecToMinSec(millis: Long): String {
            val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(millis)
            val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
            return if (seconds > 10) {
                "$minutes:${seconds}"
            } else {
                "$minutes:0${seconds}"
            }
        }
    }
}