package com.example.spotifylike.main.playlist.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.spotifylike.R
import com.example.spotifylike.common.model.Track
import com.example.spotifylike.common.utils.ObjectCallback
import com.example.spotifylike.common.utils.Utils
import com.example.spotifylike.databinding.FragmentBottomPlayerBinding
import com.example.spotifylike.main.MainActivity

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
        binding.root.setOnClickListener(this)

        if (track != null) {
            binding.tTitle.text = track.name
            binding.tArtist.text = Utils.artistsNames(track.artists!!)
        }

        // Met un callback pour changer le bouton à la fin du track
        MainActivity.mainService.playerService.onCompleteCallback = callBackTrackEnd

        // Met un callback pour recevoir la progression de la musique
        MainActivity.mainService.playerService.trackBottomProgressionCallback = callBackUpdateProgress

        updateProgressFromService()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.ibPlay -> {
                clickPlayPause()
            }
            R.id.layout -> {
                showPlayerScreenFragment()
            }
        }
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

    private val callBackTrackEnd : ObjectCallback<Boolean> = object : ObjectCallback<Boolean> {
        override fun callbackObject(res: Boolean) {
            doMusicEnded()
        }
    }

    // Appelé lorsque la musique est terminé, remet le bouton play
    fun doMusicEnded() {
        binding.ibPlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
    }

    private val callBackUpdateProgress : ObjectCallback<Pair<Int, Int>> = object : ObjectCallback<Pair<Int, Int>> {
        override fun callbackObject(res: Pair<Int, Int>) {
            updateProgress(res)
        }
    }

    fun updateProgress(pairProgressDuration: Pair<Int, Int>) {
        binding.pbTrack.progress = pairProgressDuration.first
        binding.pbTrack.max = pairProgressDuration.second
    }

    // Update le progress avec la derniere valeur retenue par le service
    private fun updateProgressFromService() {
        updateProgress(Pair(MainActivity.mainService.playerService.currentPosition, MainActivity.mainService.playerService.currentTrackDuration))

        if (MainActivity.mainService.playerService.isPaused) {
            binding.ibPlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
        } else {
            binding.ibPlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
        }
    }

    private fun showPlayerScreenFragment() {
        val fragment = PlayerScreenFragment(track!!)
        parentFragmentManager.beginTransaction()
            .detach(this)
            .setCustomAnimations(
                R.anim.fragment_slide_in_bottom,
                R.anim.fragment_fade_out,
                R.anim.fragment_fade_in,
                R.anim.fragment_slide_out_bottom)
            .addToBackStack("Player")
            .add(R.id.container, fragment)
            .commit()
    }
}