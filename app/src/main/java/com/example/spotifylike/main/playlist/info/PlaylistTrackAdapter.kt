package com.example.spotifylike.main.playlist.info

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifylike.R
import com.example.spotifylike.common.model.Track
import com.example.spotifylike.databinding.AdapterPlaylistTracksBinding
import com.example.spotifylike.main.MainActivity
import java.lang.Integer.max

class PlaylistTrackAdapter(private val tracks: List<Track>, private val fragment: Fragment) :
    RecyclerView.Adapter<PlaylistTrackAdapter.ViewHolder>() {
    private var _binding: AdapterPlaylistTracksBinding? = null

    var positionLoaded = 0
    var hasScrolled = false

    class ViewHolder(view: View, binding: AdapterPlaylistTracksBinding) :
        RecyclerView.ViewHolder(view) {
        var binding: AdapterPlaylistTracksBinding = binding
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        _binding = AdapterPlaylistTracksBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ViewHolder(_binding!!.root, _binding!!)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val track = tracks[position]

        viewHolder.binding.tTitle.text = track.name
        viewHolder.binding.tArtist.text = track.artists?.get(0)?.name ?: ""

        if (track.preview_url.isNullOrEmpty()) {
            viewHolder.binding.tTitle.setTextColor(fragment.resources.getColor(R.color.dark_grey, null))
            viewHolder.binding.tArtist.setTextColor(fragment.resources.getColor(R.color.dark_grey, null))
        } else {
            viewHolder.binding.tTitle.setTextColor(fragment.resources.getColor(R.color.white, null))
            viewHolder.binding.tArtist.setTextColor(fragment.resources.getColor(R.color.light_grey, null))
            viewHolder.itemView.setOnClickListener {
                MainActivity.mainService.playerService.playTrack(track)
            }
        }

        // animate les tracks qui n'ont pas encore été chargés
        if (positionLoaded < position) {
            positionLoaded = max(position, positionLoaded)
            val showAnimation = AlphaAnimation(0f, 1f)
            showAnimation.interpolator = AccelerateDecelerateInterpolator()
            showAnimation.duration = 500
            if (!hasScrolled) {
                showAnimation.startOffset = (40 * position).toLong()
            }
            viewHolder.binding.root.startAnimation(showAnimation)
        }
    }

    override fun getItemCount() = tracks.size

}