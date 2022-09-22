package com.example.spotifylike.common.services

import android.media.MediaPlayer
import android.util.Log
import com.example.spotifylike.common.model.Track
import com.example.spotifylike.common.utils.ObjectCallback
import com.example.spotifylike.main.MainActivity
import kotlinx.coroutines.*
import java.io.IOException

class PlayerService(private val mainService: MainService, private val mainActivity: MainActivity) {
    var currentTrack: Track? = null
    var isPlaying = false
    var isPaused = false
    var currentPosition = 0
    var currentTrackDuration = 0

    var onCompleteCallback: ObjectCallback<Boolean>? = null
    var trackProgressionCallback: ObjectCallback<Pair<Int, Int>>? = null
    var trackBottomProgressionCallback: ObjectCallback<Pair<Int, Int>>? = null

    private var mediaPlayer = MediaPlayer()

    fun playTrack(track: Track) {
        if (!isPlaying) {
            // Joue le track
            currentTrack = track
            try {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(currentTrack!!.preview_url)
                mediaPlayer.prepare()
                startMediaPlayer()
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

        // Affiche le bottomFragment
        mainActivity.showBottomFragment(track)
    }

    private fun changeTrack(newTrack: Track) {
        currentTrack = newTrack
        mediaPlayer.reset()
        mediaPlayer.setDataSource(currentTrack!!.preview_url)
        mediaPlayer.prepare()
        startMediaPlayer()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun startMediaPlayer() {
        mediaPlayer.start()
        isPlaying = true
        isPaused = false
        mediaPlayer.setOnCompletionListener {
            isPlaying = false
            isPaused = true
            if (onCompleteCallback !== null) {
                onCompleteCallback!!.callbackObject(true)
            }
        }
        GlobalScope.launch(Dispatchers.Main) {
            updateProgress()
        }
    }

    private suspend fun updateProgress() {
        currentPosition = mediaPlayer.currentPosition
        currentTrackDuration = mediaPlayer.duration
        trackProgressionCallback?.callbackObject(Pair(mediaPlayer.currentPosition, mediaPlayer.duration))
        trackBottomProgressionCallback?.callbackObject(Pair(mediaPlayer.currentPosition, mediaPlayer.duration))
        if (isPlaying && !isPaused) {
            delay(8)
            updateProgress()
        }
    }

    fun restartTrack() {
        mediaPlayer.seekTo(0)
        startMediaPlayer()
    }

    fun goToTime(time: Int) {
        mediaPlayer.seekTo(time)
    }

    fun pauseResumeTrack(): Boolean {
        if (isPlaying) {
            if (isPaused) {
                startMediaPlayer()
            } else {
                mediaPlayer.pause()
                isPaused = true
            }
        } else {
            if (currentTrack !== null) {
                restartTrack()
            }
        }
        return isPaused
    }
}