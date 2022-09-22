package com.example.spotifylike.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.spotifylike.R
import com.example.spotifylike.common.model.Playlist
import com.example.spotifylike.common.model.Track
import com.example.spotifylike.common.services.MainService
import com.example.spotifylike.databinding.ActivityMainBinding
import com.example.spotifylike.main.playlist.list.PlaylistsListFragment
import com.example.spotifylike.main.playlist.player.BottomPlayerFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        lateinit var mainService: MainService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init les services
        mainService = MainService(this@MainActivity)

        setFragmentPlaylist()
    }

    private fun setFragmentPlaylist() {
        val fragment = PlaylistsListFragment()
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.fragment_slide_in_bottom,
                R.anim.fragment_fade_out,
                R.anim.fragment_fade_in,
                R.anim.fragment_slide_out_bottom)
            .add(R.id.container, fragment)
            .commit()
    }

    fun showBottomFragment(track: Track) {
        val fragment = BottomPlayerFragment(this, track)
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.fragment_slide_in_bottom,
                R.anim.fragment_fade_out,
                R.anim.fragment_fade_in,
                R.anim.fragment_slide_out_bottom)
            .replace(R.id.bottomContainer, fragment)
            .commit()
    }
}