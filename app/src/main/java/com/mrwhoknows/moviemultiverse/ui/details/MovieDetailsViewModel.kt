package com.mrwhoknows.moviemultiverse.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrwhoknows.moviemultiverse.model.Credits
import com.mrwhoknows.moviemultiverse.model.MovieDetails
import com.mrwhoknows.moviemultiverse.net.dto.toCreditsModel
import com.mrwhoknows.moviemultiverse.net.repo.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel
@Inject constructor(
    private val moviesRepository: MovieRepository
) : ViewModel() {
    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> = _movieDetails

    private val _castList = MutableLiveData<List<Credits>>()
    val castList: LiveData<List<Credits>> = _castList

    private val _crewList = MutableLiveData<List<Credits>>()
    val crewList: LiveData<List<Credits>> = _crewList

    fun getMovieDetails(id: Int) = viewModelScope.launch {
        try {
            val movie = moviesRepository.getMovieDetails(id)
            Timber.d("getMovieDetails: $movie")
            _movieDetails.postValue(movie)
        } catch (networkError: IOException) {
            networkError.printStackTrace()
        }
    }

    fun getMovieCredits(id: Int) = viewModelScope.launch {
        try {
            val credits = moviesRepository.getMovieCredits(id)
            Timber.d("getMovieCredits: ${credits.cast}, ${credits.crew}")
            credits.apply {
                _castList.postValue(cast.map { it.toCreditsModel() })
                _crewList.postValue(crew.map { it.toCreditsModel() })
            }
        } catch (networkError: IOException) {
            networkError.printStackTrace()
        }
    }
}