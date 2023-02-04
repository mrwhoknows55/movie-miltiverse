package com.mrwhoknows.moviemultiverse.net.repo

import com.mrwhoknows.moviemultiverse.model.MovieDetails
import com.mrwhoknows.moviemultiverse.net.MovieApiService
import com.mrwhoknows.moviemultiverse.net.dto.MovieCreditsResponse
import com.mrwhoknows.moviemultiverse.net.dto.toMovieDetailsModel
import com.mrwhoknows.moviemultiverse.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class MovieApiRepository @Inject constructor(
    private val movieApiService: MovieApiService,
) : MovieRepository {
    private val apiKey: String = Constants.TMDB_API_KEY
    override suspend fun getMovieDetails(movieId: Int): MovieDetails = withContext(Dispatchers.IO) {
        movieApiService.getMovieDetails(movieId, apiKey).toMovieDetailsModel()
    }

    override suspend fun getMovieCredits(movieId: Int): MovieCreditsResponse =
        withContext(Dispatchers.IO) {
            movieApiService.getMovieCredits(movieId, apiKey)
        }
}

