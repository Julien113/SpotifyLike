package com.example.spotifylike.main.playlist.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.spotifylike.common.model.APIPlaylist
import com.example.spotifylike.common.model.Playlist
import com.example.spotifylike.common.utils.ObjectCallback
import com.example.spotifylike.databinding.FragmentPlaylistsListBinding
import com.example.spotifylike.main.MainActivity
import java.util.*

class PlaylistsListFragment : Fragment() {
    private var _binding: FragmentPlaylistsListBinding? = null
    private val binding get() = _binding!!

    private lateinit var playlistsListViewAdapter: PlaylistsListViewAdapter
    private val featuredPlaylists = LinkedList<Playlist>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        playlistsListViewAdapter = PlaylistsListViewAdapter(featuredPlaylists, this@PlaylistsListFragment)
        binding.rvPlaylist.adapter = playlistsListViewAdapter
        val gridLayoutManager = GridLayoutManager(context, 2)
        binding.rvPlaylist.layoutManager = gridLayoutManager

        searchForFeaturedPlaylists()
    }

    private fun searchForFeaturedPlaylists() {
        //TODO Loading Animation

        MainActivity.mainService.playlistService.getListOfFeaturedPlaylists(object :
            ObjectCallback<APIPlaylist> {
            @SuppressLint("NotifyDataSetChanged")
            override fun callbackObject(res: APIPlaylist) {
                //TODO Close loading Animation

                // Update Adapter
                featuredPlaylists.clear()
                featuredPlaylists.addAll(res.playlists.items)
                playlistsListViewAdapter.notifyDataSetChanged()
                return
            }
        })
    }
}