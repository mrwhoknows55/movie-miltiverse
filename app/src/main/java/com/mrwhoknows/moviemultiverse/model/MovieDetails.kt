package com.mrwhoknows.moviemultiverse.model

data class MovieDetails(
    val backdropLink: String,
    val posterLink: String,
    val title: String,
    val imdbId: String,
    val id: Int,
    val overview: String,
    val budget: Long,
    val revenue: Long,
    val releaseDate: String,
    val homepageUrl: String,
    val tagLine: String,
    val genres: List<String>,
    val languages: List<String>,
    val voteAvg: Double,
    val voteCount: Long
)