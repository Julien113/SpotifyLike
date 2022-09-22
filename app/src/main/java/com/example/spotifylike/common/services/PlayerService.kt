package com.example.spotifylike.common.services

import android.media.MediaPlayer
import android.util.Log
import com.example.spotifylike.common.model.Track
import com.example.spotifylike.main.MainActivity
import java.io.IOException

class PlayerService(private val mainService: MainService, private val mainActivity: MainActivity) {
    private var currentTrack: Track? = null
    private var isPlaying = false
    private var isPaused = false

    private var mediaPlayer = MediaPlayer()


    fun playTrack(track: Track) {
        if (!isPlaying) {
            // Joue le track
            currentTrack = track
            try {
                mediaPlayer.setDataSource(currentTrack!!.preview_url)
                mediaPlayer.prepare()
                mediaPlayer.start()
                isPlaying = true
            } catch (e: IOException) {
                Log.e("PlayerService", "playTrack: prepare failed with ${currentTrack!!.preview_url}")
                isPlaying = false
            }
        } else {
            if (track !== currentTrack) {
                // Change de track
                changeTrack(track)
            } else {
                // Relance le track du début
                restartTrack()
            }
        }
        isPaused = false

        // Affiche le bottomFragment
        mainActivity.showBottomFragment(track)
    }

    fun changeTrack(newTrack: Track) {
        currentTrack = newTrack
        mediaPlayer.reset()
        mediaPlayer.setDataSource(currentTrack!!.preview_url)
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    fun restartTrack() {
        mediaPlayer.seekTo(0)
        mediaPlayer.start()
    }

    fun pauseResumeTrack(): Boolean {
        if (isPlaying) {
            if (isPaused) {
                mediaPlayer.start()
            } else {
                mediaPlayer.pause()
            }
            isPaused = !isPaused
        }
        return isPaused
    }
}