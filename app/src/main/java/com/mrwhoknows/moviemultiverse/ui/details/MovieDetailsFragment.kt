package com.mrwhoknows.moviemultiverse.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.google.android.material.snackbar.Snackbar
import com.mrwhoknows.moviemultiverse.databinding.FragmentMovieDetailsBinding
import com.mrwhoknows.moviemultiverse.model.Credits
import com.mrwhoknows.moviemultiverse.model.MovieDetails
import com.mrwhoknows.moviemultiverse.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailsBinding
    private val viewModel: MovieDetailsViewModel by viewModels()
    private val castsAdapter: MovieCreditsRVAdapter by lazy { MovieCreditsRVAdapter() }
    private val crewAdapter: MovieCreditsRVAdapter by lazy { MovieCreditsRVAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()

        viewModel.getMovieDetails(453395)
        viewModel.getMovieCredits(453395)

        viewModel.movieDetails.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    Timber.e("movieDetails: ${resource.message}")
                    showError(resource.message)
                }
                is Resource.Loading -> {
                    Timber.d("movieDetails: loading")
                    updateLoadingUI(true)
                }
                is Resource.Success -> {
                    Timber.d("movieDetails: ${resource.data}")
                    resource.data?.run {
                        bindMovieDetailsUI(this)
                    }
                }
            }
        }

        viewModel.castList.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    Timber.e("castsList: ${resource.message}")
                    showError(resource.message)
                }
                is Resource.Loading -> {
                    Timber.d("castsList: loading")
                    updateLoadingUI(true)
                }
                is Resource.Success -> {
                    Timber.d("castsList: ${resource.data}")
                    resource.data?.run {
                        bindCastListUI(this)
                    }
                }
            }
        }
        viewModel.crewList.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    Timber.e("crewList: ${resource.message}")
                    showError(resource.message)
                }
                is Resource.Loading -> {
                    Timber.d("crewList: loading")
                    updateLoadingUI(true)
                }
                is Resource.Success -> {
                    Timber.d("crewList: ${resource.data}")
                    resource.data?.run {
                        bindCrewListUI(this)
                    }
                }
            }
        }
    }

    private fun setupRecyclerViews() = binding.run {
        rvCastList.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = castsAdapter
        }
        rvCrewList.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = crewAdapter
        }
    }

    private fun bindCastListUI(credits: List<Credits>) {
        updateLoadingUI(false)
        castsAdapter.submitList(credits)
    }

    private fun bindCrewListUI(credits: List<Credits>) {
        updateLoadingUI(false)
        crewAdapter.submitList(credits)
    }

    @SuppressLint("SetTextI18n")
    private fun bindMovieDetailsUI(movieDetails: MovieDetails) = binding.run {
        updateLoadingUI(false)
        // todo add text wrap/expand
        tvTitle.text = movieDetails.title
        tvRating.text = "${movieDetails.voteAvg}/10 (${movieDetails.voteCount} votes)"
        ivBackdrop.load(movieDetails.backdropLink) {
            crossfade(true)
//            placeholder() todo add animated shimmer and failure
            scale(Scale.FILL)
        }
        tvOverview.text = movieDetails.overview
        chipGenres.text = movieDetails.genres.joinToString(separator = ", ")
        chipLanguages.text = movieDetails.languages.joinToString(separator = ", ")
    }

    private fun updateLoadingUI(isLoading: Boolean) = binding.run {
        if (isLoading) {
            parent.visibility = View.GONE
            shimmerLayout.root.visibility = View.VISIBLE
            shimmerLayout.parent.showShimmer(true)
        } else {
            shimmerLayout.parent.showShimmer(false)
            shimmerLayout.root.visibility = View.GONE
            parent.visibility = View.VISIBLE
        }
    }

    private fun showError(message: String?) {
        updateLoadingUI(false)
        if (!message.isNullOrBlank()) {
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
        }
    }

}
