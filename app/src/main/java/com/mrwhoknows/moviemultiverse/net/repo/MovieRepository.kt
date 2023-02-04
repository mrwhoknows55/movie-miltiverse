package com.mrwhoknows.moviemultiverse.net.repo

import com.mrwhoknows.moviemultiverse.model.MovieDetails
import com.mrwhoknows.moviemultiverse.net.dto.MovieCreditsResponse


interface MovieRepository {
    suspend fun getMovieDetails(movieId: Int): MovieDetails
    suspend fun getMovieCredits(movieId: Int): MovieCreditsResponse
}