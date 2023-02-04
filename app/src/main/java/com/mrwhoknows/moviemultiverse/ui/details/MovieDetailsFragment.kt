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
import com.mrwhoknows.moviemultiverse.databinding.FragmentMovieDetailsBinding
import com.mrwhoknows.moviemultiverse.model.Credits
import com.mrwhoknows.moviemultiverse.model.MovieDetails
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

        viewModel.getMovieDetails(453395)
        viewModel.getMovieCredits(453395)
        setupRecyclerViews()
        viewModel.movieDetails.observe(viewLifecycleOwner) { movieDetails ->
            Timber.d("onCreate movieDetails: $movieDetails")
            movieDetails?.run {
                bindMovieDetailsUI(this)
            }
        }

        viewModel.castList.observe(viewLifecycleOwner) { castList ->
            Timber.d("onCreate castsList: $castList")
            castList?.run {
                bindCastListUI(this)
            }
        }
        viewModel.crewList.observe(viewLifecycleOwner) { crewList ->
            Timber.d("onCreate crewList: $crewList")
            crewList?.run {
                bindCrewListUI(this)
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
        castsAdapter.submitList(credits)
    }

    private fun bindCrewListUI(credits: List<Credits>) {
        crewAdapter.submitList(credits)
    }

    @SuppressLint("SetTextI18n")
    private fun bindMovieDetailsUI(movieDetails: MovieDetails) = binding.run {
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
}