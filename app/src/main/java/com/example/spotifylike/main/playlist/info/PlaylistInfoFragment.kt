package com.example.spotifylike.main.playlist.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifylike.R
import com.example.spotifylike.common.model.APITracksRes
import com.example.spotifylike.common.model.Playlist
import com.example.spotifylike.common.model.Track
import com.example.spotifylike.common.utils.ObjectCallback
import com.example.spotifylike.databinding.FragmentInfoPlaylistBinding
import com.example.spotifylike.main.MainActivity
import java.util.*

class PlaylistInfoFragment(val playlist: Playlist) : Fragment() {
    private var _binding: FragmentInfoPlaylistBinding? = null
    private val binding get() = _binding!!

    private val playlistTracks = LinkedList<Track>()
    private lateinit var tracksAdapter: PlaylistTrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (playlist.images !== null && playlist.images.isNotEmpty()) {
            Glide.with(requireContext()).load(playlist.images[0].url)
                .into(binding.ivPlaylistImage)
        }

        binding.tTitle.text = playlist.name
        binding.tTitle.isSelected = true
        binding.tDescription.text = playlist.description?.replace("<[^>]*>".toRegex(), "")
        binding.tDescription.isSelected = true
        binding.tFrom.text = getString(R.string.fragment_info_playlist_playlist_by, playlist.owner?.display_name)
        //binding.tNbFollowers.text = "${playlist}" // TODO Trouver le nb de follower



        tracksAdapter = PlaylistTrackAdapter(playlistTracks, this@PlaylistInfoFragment)
        binding.rvTracks.adapter = tracksAdapter
        val linearLayoutManager = LinearLayoutManager(context)
        binding.rvTracks.layoutManager = linearLayoutManager


        // Desactive l'animation pour les playlists cachées
        binding.rvTracks.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    tracksAdapter.hasScrolled = true
                }
            }
        })

        getPlaylistTracks()

    }

    private fun getPlaylistTracks() {
        binding.pbLoadingTracks.isIndeterminate = true
        MainActivity.mainService.playlistService.getPlaylistTracks(playlist.id!!, object :
            ObjectCallback<APITracksRes> {
            override fun callbackObject(res: APITracksRes) {
                binding.pbLoadingTracks.isIndeterminate = false

                val tracks = ArrayList<Track>(res.items.size)
                res.items.forEach { tracks.add(it.track!!) }
                playlistTracks.addAll(tracks)

                // Update Adapter
                tracksAdapter.notifyItemRangeInserted(0, tracks.size)
                return
            }})
    }
}