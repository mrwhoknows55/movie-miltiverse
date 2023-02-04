package com.mrwhoknows.moviemultiverse.di

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.mrwhoknows.moviemultiverse.MoviesApp
import com.mrwhoknows.moviemultiverse.net.MovieApiService
import com.mrwhoknows.moviemultiverse.net.repo.MovieApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesMoviesApp(@ApplicationContext context: Context) =
        (context.applicationContext as MoviesApp)

    @Provides
    @Singleton
    fun providesImageLoader(@ApplicationContext context: Context): ImageLoader =
        ImageLoader.Builder(context).memoryCache {
            MemoryCache.Builder(context)
                .maxSizePercent(0.25)
                .build()
        }.diskCache {
            DiskCache.Builder()
                .directory(context.cacheDir.resolve("image_cache"))
                .maxSizePercent(0.02)
                .build()
        }.build()

    @Provides
    @Singleton
    fun providesIODispatcher() = Dispatchers.IO

    @Provides
    @Singleton
    fun providesMovieApiRepo(apiService: MovieApiService, dispatcher: CoroutineDispatcher) =
        MovieApiRepository(apiService, dispatcher)
}