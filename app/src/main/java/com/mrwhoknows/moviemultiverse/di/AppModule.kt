package com.mrwhoknows.moviemultiverse.di

import android.content.Context
import com.mrwhoknows.moviemultiverse.MoviesApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesMoviesApp(@ApplicationContext context: Context) =
        (context.applicationContext as MoviesApp)
}