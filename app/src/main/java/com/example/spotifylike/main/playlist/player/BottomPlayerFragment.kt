package com.example.spotifylike.main.playlist.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.spotifylike.R
import com.example.spotifylike.common.model.APITracksRes
import com.example.spotifylike.common.model.Playlist
import com.example.spotifylike.common.model.Track
import com.example.spotifylike.common.services.PlayerService
import com.example.spotifylike.common.utils.ObjectCallback
import com.example.spotifylike.databinding.FragmentBottomPlayerBinding
import com.example.spotifylike.databinding.FragmentInfoPlaylistBinding
import com.example.spotifylike.main.MainActivity
import java.util.*

class BottomPlayerFragment(private val mainActivity: MainActivity, private val track: Track?) : Fragment(), View.OnClickListener {
    private var _binding: FragmentBottomPlayerBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.ibPlay.setOnClickListener(this)

        if (track != null) {
            binding.tTitle.text = track.name
            if (track.artists!!.isNotEmpty()) {
                binding.tArtist.text = track.artists.get(0).name
            } else {
                binding.tArtist.text = getString(R.string.fragment_bottom_player_unknown_artist)
            }
        }

        // Met un callback pour changer le bouton à la fin du track
        MainActivity.mainService.playerService.onCompleteCallback = object : ObjectCallback<Boolean> {
            override fun callbackObject(res: Boolean) {
                doMusicEnded()
            }
        }

        // Met un callback pour recevoir la progression de la musique
        MainActivity.mainService.playerService.trackProgressionCallback = object : ObjectCallback<Pair<Int, Int>> {
            override fun callbackObject(res: Pair<Int, Int>) {
                updateProgress(res)
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.ibPlay -> {
                val isPaused = MainActivity.mainService.playerService.pauseResumeTrack()
                if (isPaused) {
                    binding.ibPlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
                } else {
                    binding.ibPlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
                }
                val animation = AnimationUtils.loadAnimation(context, R.anim.imagebutton_pressed)
                binding.ibPlay.startAnimation(animation)
            }
        }
    }

    fun doMusicEnded() {
        binding.ibPlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
    }

    fun updateProgress(pairProgressDuration: Pair<Int, Int>) {
        binding.pbTrack.progress = pairProgressDuration.first
        binding.pbTrack.max = pairProgressDuration.second

    }
}