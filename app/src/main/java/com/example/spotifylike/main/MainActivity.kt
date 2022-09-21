package com.example.spotifylike.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.spotifylike.R
import com.example.spotifylike.common.services.MainService
import com.example.spotifylike.databinding.ActivityMainBinding
import com.example.spotifylike.main.previews.PlaylistFragment

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
        mainService = MainService()

        setFragmentPlaylist()
    }


    private fun setFragmentPlaylist() {
        val fragment = PlaylistFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.container, fragment)
            .commit()
    }
}