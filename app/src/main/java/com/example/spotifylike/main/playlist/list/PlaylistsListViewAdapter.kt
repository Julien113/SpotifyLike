package com.example.spotifylike.main.playlist.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifylike.R
import com.example.spotifylike.common.model.Playlist
import com.example.spotifylike.databinding.AdapterPlaylistViewBinding
import com.example.spotifylike.main.playlist.info.PlaylistInfoFragment

class PlaylistsListViewAdapter(private val playlistData: List<Playlist>, private val fragment: Fragment) :
    RecyclerView.Adapter<PlaylistsListViewAdapter.ViewHolder>() {
    private var _binding: AdapterPlaylistViewBinding? = null


    class ViewHolder(view: View, binding: AdapterPlaylistViewBinding) :
        RecyclerView.ViewHolder(view) {
        var binding: AdapterPlaylistViewBinding = binding
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        _binding = AdapterPlaylistViewBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ViewHolder(_binding!!.root, _binding!!)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val playlist = playlistData[position]
        if (playlist.images !== null && playlist.images.isNotEmpty()) {
            Glide.with(viewHolder.itemView).load(playlist.images[0].url)
                .into(viewHolder.binding.ibPlaylist)
        }

        viewHolder.itemView.setOnClickListener {
            fragment.parentFragmentManager.beginTransaction()
                .addToBackStack("Informations")
                .add(R.id.container, PlaylistInfoFragment(playlist))
                .commit()
        }
    }

    override fun getItemCount() = playlistData.size
}