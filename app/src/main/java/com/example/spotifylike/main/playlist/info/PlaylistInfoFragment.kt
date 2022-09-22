package com.example.spotifylike.main.playlist.info

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
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
        binding.tDescription.text = playlist.description?.replace("<[^>]*>".toRegex(), "")
        binding.tFrom.text = "Playlist by ${playlist.owner?.display_name}"
        //binding.tNbFollowers.text = "${playlist}" // TODO Trouver le nb de follower



        tracksAdapter = PlaylistTrackAdapter(playlistTracks, this@PlaylistInfoFragment)
        binding.rvTracks.adapter = tracksAdapter
        val linearLayoutManager = LinearLayoutManager(context)
        binding.rvTracks.layoutManager = linearLayoutManager

        //getPlaylistInformations() //TODO A voir si c'est vraiment utile
        getPlaylistTracks()

    }

    fun getPlaylistInformations() {
        MainActivity.mainService.playlistService.getPlaylistInformations(playlist.id!!, object :
            ObjectCallback<Playlist> {
            override fun callbackObject(res: Playlist) {
                //TODO Close loading Animation
                Log.e("TAG", "moui??: $playlist", )

                // Update Adapter
                return
            }})
    }

    fun getPlaylistTracks() {
        MainActivity.mainService.playlistService.getPlaylistTracks(playlist.id!!, object :
            ObjectCallback<APITracksRes> {
            override fun callbackObject(res: APITracksRes) {
                //TODO Close loading Animation
                Log.e("TAG", "eh la???: $playlist", )
                val tracks = ArrayList<Track>(res.items.size)
                res.items.forEach { tracks.add(it.track!!) }
                playlistTracks.addAll(tracks)


                // Update Adapter
                tracksAdapter.notifyItemRangeInserted(0, tracks.size)
                return
            }})
    }
}