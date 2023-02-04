package com.mrwhoknows.moviemultiverse.di

import com.mrwhoknows.moviemultiverse.net.repo.MovieApiRepository
import com.mrwhoknows.moviemultiverse.net.repo.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    abstract fun providesMovieApiRepo(
        movieApiRepository: MovieApiRepository
    ): MovieRepository
}
