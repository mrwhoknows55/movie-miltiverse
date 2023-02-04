package com.mrwhoknows.moviemultiverse.net.repo

import com.mrwhoknows.moviemultiverse.model.MovieDetails
import com.mrwhoknows.moviemultiverse.net.MovieApiService
import com.mrwhoknows.moviemultiverse.net.dto.MovieCreditsResponse
import com.mrwhoknows.moviemultiverse.net.dto.toMovieDetailsModel
import com.mrwhoknows.moviemultiverse.util.Constants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieApiRepository @Inject constructor(
    private val movieApiService: MovieApiService,
    private val dispatcher: CoroutineDispatcher,
    private val apiKey: String = Constants.TMDB_API_KEY
) : MovieRepository {

    override suspend fun getMovieDetails(movieId: Int): MovieDetails = withContext(dispatcher) {
        movieApiService.getMovieDetails(movieId, apiKey).toMovieDetailsModel()
    }

    override suspend fun getMovieCredits(movieId: Int): MovieCreditsResponse =
        withContext(dispatcher) {
            movieApiService.getMovieCredits(movieId, apiKey)
        }
}

