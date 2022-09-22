package com.example.spotifylike.common.services

import android.media.MediaPlayer
import android.util.Log
import com.example.spotifylike.common.model.Track
import com.example.spotifylike.common.utils.ObjectCallback
import com.example.spotifylike.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

class PlayerService(private val mainService: MainService, private val mainActivity: MainActivity) {
    private var currentTrack: Track? = null
    private var isPlaying = false
    private var isPaused = false

    var onCompleteCallback: ObjectCallback<Boolean>? = null
    var trackProgressionCallback: ObjectCallback<Pair<Int, Int>>? = null

    private var mediaPlayer = MediaPlayer()


    fun playTrack(track: Track) {
        if (!isPlaying) {
            // Joue le track
            currentTrack = track
            try {
                mediaPlayer.setDataSource(currentTrack!!.preview_url)
                mediaPlayer.prepare()
                startMediaPlayer()
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
        startMediaPlayer()
    }

    fun startMediaPlayer() {
        mediaPlayer.start()
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

    suspend fun updateProgress() {
        if (onCompleteCallback !== null) {
            trackProgressionCallback!!.callbackObject(Pair(mediaPlayer.currentPosition, mediaPlayer.duration))
        }
        if (isPlaying && !isPaused) {
            delay(8)
            updateProgress()
        }
    }

    fun restartTrack() {
        mediaPlayer.seekTo(0)
        startMediaPlayer()
    }

    fun pauseResumeTrack(): Boolean {
        if (isPlaying) {
            if (isPaused) {
                startMediaPlayer()
            } else {
                mediaPlayer.pause()
            }
            isPaused = !isPaused
        }
        return isPaused
    }
}