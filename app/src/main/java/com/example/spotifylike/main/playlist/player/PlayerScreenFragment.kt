package com.example.spotifylike.main.playlist.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.spotifylike.R
import com.example.spotifylike.common.model.Track
import com.example.spotifylike.common.utils.ObjectCallback
import com.example.spotifylike.common.utils.Utils
import com.example.spotifylike.databinding.FragmentPlayerScreenBinding
import com.example.spotifylike.main.MainActivity


class PlayerScreenFragment(private val track: Track) : Fragment(), View.OnClickListener {
    var _binding: FragmentPlayerScreenBinding? = null
    val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.ibPlay.setOnClickListener(this)
        binding.ibNext.setOnClickListener(this)
        binding.ibPrevious.setOnClickListener(this)

        binding.tTitle.text = track.name
        binding.tArtist.text = Utils.artistsNames(track.artists!!)
        // Track Image
        if (track.album !== null && track.album.images.isNotEmpty()) {
            Glide.with(requireContext()).load(track.album.images[0].url)
                .into(binding.ivTrackImage)
        } else {
            Glide.with(requireContext()).load(R.drawable.ic_baseline_disc_full_24)
                .into(binding.ivTrackImage)
        }
        binding.pbTrack.setOnSeekBarChangeListener(progressSeekBarChangeListener)

        // Ajoute un callback pour changer le bouton à la fin du track
        MainActivity.mainService.playerService.onCompleteCallback = callBackTrackEnd

        // Ajoute un callback pour recevoir la progression de la musique
        MainActivity.mainService.playerService.trackProgressionCallback = callBackUpdateProgress

        updateProgressFromService()
    }

    private val progressSeekBarChangeListener: SeekBar.OnSeekBarChangeListener =
        object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                MainActivity.mainService.playerService.goToTime(p0!!.progress)
            }
        }

    private val callBackTrackEnd: ObjectCallback<Boolean> = object : ObjectCallback<Boolean> {
        override fun callbackObject(res: Boolean) {
            doMusicEnded()
        }
    }

    // Appelé lorsque la musique est terminé, remet le bouton play
    fun doMusicEnded() {
        binding.ibPlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
    }

    private val callBackUpdateProgress: ObjectCallback<Pair<Int, Int>> =
        object : ObjectCallback<Pair<Int, Int>> {
            override fun callbackObject(res: Pair<Int, Int>) {
                updateProgress(res)
            }
        }

    fun updateProgress(pairProgressDuration: Pair<Int, Int>) {
        binding.pbTrack.progress = pairProgressDuration.first
        binding.pbTrack.max = pairProgressDuration.second
        binding.tCurrentTimePosition.text =
            Utils.millisecToMinSec(pairProgressDuration.first.toLong())
        binding.tTotalDuration.text = Utils.millisecToMinSec(pairProgressDuration.second.toLong())
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ibPlay -> {
                clickPlayPause()
            }
            R.id.ibPrevious -> {
                clickPreviousNext(true)
            }
            R.id.ibNext -> {
                clickPreviousNext(false)
            }
        }
    }

    private fun clickPreviousNext(isPrevious: Boolean) {
        MainActivity.mainService.playerService.restartTrack()
        binding.ibPlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)

        val animation = AnimationUtils.loadAnimation(context, R.anim.imagebutton_pressed)
        if (isPrevious)
            binding.ibPrevious.startAnimation(animation)
        else
            binding.ibNext.startAnimation(animation)
    }

    private fun clickPlayPause() {
        val isPaused = MainActivity.mainService.playerService.pauseResumeTrack()
        if (isPaused) {
            binding.ibPlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
        } else {
            binding.ibPlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
        }
        val animation = AnimationUtils.loadAnimation(context, R.anim.imagebutton_pressed)
        binding.ibPlay.startAnimation(animation)
    }

    // Update le progress avec la derniere valeur retenue par le service
    private fun updateProgressFromService() {
        updateProgress(
            Pair(
                MainActivity.mainService.playerService.currentPosition,
                MainActivity.mainService.playerService.currentTrackDuration
            )
        )

        if (MainActivity.mainService.playerService.isPaused) {
            binding.ibPlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
        } else {
            binding.ibPlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
        }
    }
}
