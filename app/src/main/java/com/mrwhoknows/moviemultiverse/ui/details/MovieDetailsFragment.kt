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
import com.facebook.shimmer.ShimmerFrameLayout
import com.mrwhoknows.moviemultiverse.databinding.FragmentMovieDetailsBinding
import com.mrwhoknows.moviemultiverse.model.Credits
import com.mrwhoknows.moviemultiverse.model.MovieDetails
import com.mrwhoknows.moviemultiverse.util.Resource
import com.mrwhoknows.moviemultiverse.util.startFadeIn
import com.mrwhoknows.moviemultiverse.util.startFadeOut
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
        getDataFromApi()
        setupObservables()
    }


    private fun getDataFromApi() = viewModel.run {
        getMovieDetails(453395)
        getMovieCredits(453395)
        Unit
    }

    private fun setupObservables() {
        viewModel.movieDetails.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    Timber.e("movieDetails: ${resource.message}")
                    updateErrorUi(resource.message ?: "Something Went Wrong")
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
                    updateErrorUi(resource.message ?: "Something Went Wrong")
                }
                is Resource.Loading -> {
                    Timber.d("castsList: loading")
                    updateCastLoadingUI(true)
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
                    updateErrorUi(resource.message ?: "Something Went Wrong")
                }
                is Resource.Loading -> {
                    Timber.d("crewList: loading")
                    updateCrewLoadingUI(true)
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

    private fun setupRecyclerViews() {
        binding.apply {
            rvCastList.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                adapter = castsAdapter
            }
            rvCrewList.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                adapter = crewAdapter
            }
        }
    }

    private fun bindCastListUI(credits: List<Credits>) {
        updateCastLoadingUI(false)
        castsAdapter.submitList(credits)
    }

    private fun bindCrewListUI(credits: List<Credits>) {
        updateCrewLoadingUI(false)
        crewAdapter.submitList(credits)
    }

    @SuppressLint("SetTextI18n")
    private fun bindMovieDetailsUI(movieDetails: MovieDetails) = with(binding) {
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

    private fun updateLoadingUI(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                errorLayout.root.apply {
                    startFadeOut()
                    visibility = View.GONE
                }
                parent.apply {
                    startFadeOut()
                    visibility = View.GONE
                }
                shimmerLayout.apply {
                    root.startFadeIn()
                    root.visibility = View.VISIBLE
                    parent.showShimmer(true)
                }
            } else {
                shimmerLayout.apply {
                    root.startFadeOut()
                    root.visibility = View.GONE
                    parent.showShimmer(false)
                }
                parent.apply {
                    startFadeIn()
                    visibility = View.VISIBLE
                }
            }
        }
    }

    private fun updateShimmerState(
        isLoading: Boolean, list: RecyclerView, dummyList: View, shimmerLayout: ShimmerFrameLayout
    ) {
        if (isLoading) {
            list.apply {
                startFadeOut()
                visibility = View.GONE
            }
            dummyList.apply {
                startFadeIn()
                visibility = View.VISIBLE
            }
            shimmerLayout.showShimmer(true)
        } else {
            dummyList.apply {
                startFadeOut()
                visibility = View.GONE
            }
            list.apply {
                startFadeIn()
                visibility = View.VISIBLE
            }
            shimmerLayout.hideShimmer()
        }
    }

    private fun updateCrewLoadingUI(isLoading: Boolean) {
        binding.apply {
            updateShimmerState(isLoading, rvCrewList, rvCrewDummyList.root, rvCrewListWrapper)
        }
    }

    private fun updateCastLoadingUI(isLoading: Boolean) {
        binding.apply {
            updateShimmerState(isLoading, rvCastList, rvCastDummyList.root, rvCastListWrapper)
        }
    }

    private fun updateErrorUi(message: String) {
        updateLoadingUI(false)
        binding.parent.apply {
            startFadeOut()
            visibility = View.GONE
        }
        binding.errorLayout.apply {
            root.startFadeIn()
            root.visibility = View.VISIBLE
            tvMessage.text = message
            btnRetry.setOnClickListener {
                getDataFromApi()
            }
        }
    }

}
