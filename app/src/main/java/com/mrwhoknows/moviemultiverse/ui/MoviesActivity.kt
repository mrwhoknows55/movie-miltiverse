package com.mrwhoknows.moviemultiverse.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mrwhoknows.moviemultiverse.databinding.ActivityMoviesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesActivity : AppCompatActivity() {
    private val binding: ActivityMoviesBinding by lazy {
        ActivityMoviesBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}