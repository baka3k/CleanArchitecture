package com.baka3k.architecture.clean.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.baka3k.architecture.clean.movie.viewmodel.MovieDetailViewModel
import com.baka3k.architecture.clean.movie.viewmodel.MoviesViewModel
import com.baka3k.architecture.data.repository.MovieInforRepositoryImpl
import com.baka3k.architecture.data.service.movie.MovieInfoService
import com.baka3k.architecture.data.utils.connectivity.NetworkConnection
import com.baka3k.architecture.domain.interactor.GetActorsUseCase
import com.baka3k.architecture.domain.interactor.GetMovieDetailUseCase
import com.baka3k.architecture.domain.interactor.GetMoviePopularUseCase
import com.baka3k.architecture.domain.interactor.GetNowPlayingUseCase
import com.baka3k.architecture.domain.interactor.ShowMovieDetailUseCase
import com.baka3k.architecture.domain.interactor.ShowMovieListUseCase
import kotlinx.coroutines.Dispatchers

class ViewModelFactory(
    private val networkConnection: NetworkConnection,
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
     if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
            val serviceData = MovieInfoService.create(networkConnection)
            val repository = MovieInforRepositoryImpl(serviceData, Dispatchers.IO)
            val nowPlayingUseCase = GetNowPlayingUseCase(repository)
            val popularUseCase = GetMoviePopularUseCase(repository)
            val showMovieListUseCase = ShowMovieListUseCase(
                getMoviePopularUseCase = popularUseCase,
                getNowPlayingUseCase = nowPlayingUseCase
            )
            return MoviesViewModel(
                showMovieListUseCase
            ) as T
        } else if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
            val serviceData = MovieInfoService.create(networkConnection)
            val repository = MovieInforRepositoryImpl(serviceData, Dispatchers.IO)
            val getMovieDetailUseCase = GetMovieDetailUseCase(repository)
            val getActorsUseCase = GetActorsUseCase(repository)
            val showMovieDetailUseCase = ShowMovieDetailUseCase(
                getMovieDetailUseCase = getMovieDetailUseCase,
                getActorsUseCase = getActorsUseCase
            )
            return MovieDetailViewModel(
                showMovieDetailUseCase
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}