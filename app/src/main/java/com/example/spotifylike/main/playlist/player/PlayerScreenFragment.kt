package com.example.spotifylike.main.playlist.player

import android.content.Context
import android.graphics.Matrix
import android.hardware.*
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
import com.example.spotifylike.common.utils.MatrixAnimation
import com.example.spotifylike.common.utils.ObjectCallback
import com.example.spotifylike.common.utils.Utils
import com.example.spotifylike.databinding.FragmentPlayerScreenBinding
import com.example.spotifylike.main.MainActivity
import kotlin.math.abs


class PlayerScreenFragment(private val track: Track) : Fragment(), View.OnClickListener,
    SensorEventListener2 {
    var _binding: FragmentPlayerScreenBinding? = null
    val binding get() = _binding!!

    var touchingTrackImage = false

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
        binding.ivTrackImage.setOnClickListener(this)

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
        initMotionSensor()
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
            R.id.ivTrackImage -> {
                touchingTrackImage = !touchingTrackImage
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





    // Tentative d'utilisation du motion sensor. Cliquez sur l'image du track pour activer
    var sensorManager: SensorManager? = null
    private fun initMotionSensor() {
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor: Sensor? = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager!!.registerListener(this, sensor!!, SensorManager.SENSOR_DELAY_GAME);
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (!touchingTrackImage)
            return
        val alpha: Float = 0.65f
        val gravity = arrayOf(0f, 0f, 0f)
        val linearAcceleration = arrayOf(0f, 0f, 0f)

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0]
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1]
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2]

        // Remove the gravity contribution with the high-pass filter.
        linearAcceleration[0] = event.values[0] - gravity[0]
        linearAcceleration[1] = event.values[1] - gravity[1]
        linearAcceleration[2] = event.values[2] - gravity[2]

        val matrixScale = Matrix()
        val matrixTranslate = Matrix()
        val matrix = Matrix()

        val scale = 1f + abs(gravity[2] / 100)
        matrixScale.setScale(
            scale,
            scale,
            binding.ivTrackImage.measuredWidth / 2.toFloat(),
            binding.ivTrackImage.measuredHeight / 2.toFloat()
        )
        matrixTranslate.setTranslate(gravity[0] * 8, gravity[1] * 8)
        matrix.postConcat(matrixScale)
        matrix.postConcat(matrixTranslate)

        val matrixAnimation = MatrixAnimation(matrix)
        matrixAnimation.duration = 0
        matrixAnimation.fillAfter = true
        binding.ivTrackImage.startAnimation(matrixAnimation)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onFlushCompleted(p0: Sensor?) {
    }
}
