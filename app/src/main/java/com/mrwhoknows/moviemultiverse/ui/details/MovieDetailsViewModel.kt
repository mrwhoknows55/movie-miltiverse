package com.mrwhoknows.moviemultiverse.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrwhoknows.moviemultiverse.model.Credits
import com.mrwhoknows.moviemultiverse.model.MovieDetails
import com.mrwhoknows.moviemultiverse.net.dto.toCreditsModel
import com.mrwhoknows.moviemultiverse.net.repo.MovieRepository
import com.mrwhoknows.moviemultiverse.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel
@Inject constructor(
    private val moviesRepository: MovieRepository
) : ViewModel() {
    private val _movieDetails = MutableLiveData<Resource<MovieDetails>>()
    val movieDetails: LiveData<Resource<MovieDetails>> = _movieDetails

    private val _castList = MutableLiveData<Resource<List<Credits>>>()
    val castList: LiveData<Resource<List<Credits>>> = _castList

    private val _crewList = MutableLiveData<Resource<List<Credits>>>()
    val crewList: LiveData<Resource<List<Credits>>> = _crewList

    init {
        getDataFromApi()
    }

    private fun getMovieDetails(id: Int) = viewModelScope.launch {
        _movieDetails.postValue(Resource.Loading())
        try {
            moviesRepository.getMovieDetails(id).let { movie ->
                _movieDetails.postValue(Resource.Success(movie))
                Timber.d("getMovieDetails: $movie")
            }
        } catch (exception: Exception) {
            Timber.e(exception)
            _movieDetails.postValue(Resource.Error(exception.localizedMessage))
        }
    }

    private fun getMovieCredits(id: Int) = viewModelScope.launch {
        _castList.postValue(Resource.Loading())
        _crewList.postValue(Resource.Loading())
        try {
            val credits = moviesRepository.getMovieCredits(id)
            Timber.d("getMovieCredits: ${credits.cast}, ${credits.crew}")
            credits.apply {
                val castList = cast.map { it.toCreditsModel() }
                _castList.postValue(Resource.Success(castList))
                val crewList = crew.map { it.toCreditsModel() }
                _crewList.postValue(Resource.Success(crewList))
            }
        } catch (exception: Exception) {
            Timber.e(exception)
            _castList.postValue(Resource.Error(exception.localizedMessage))
            _crewList.postValue(Resource.Error(exception.localizedMessage))
        }
    }

    // todo: shouldn't be hardcoded
    private fun getDataFromApi(id: Int = 453395) {
        getMovieDetails(id)
        getMovieCredits(id)
    }

    // todo: shouldn't be hardcoded
    fun retry(id: Int = 453395) = getDataFromApi(id)
}
