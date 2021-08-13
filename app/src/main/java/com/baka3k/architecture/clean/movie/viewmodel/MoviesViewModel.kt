package com.baka3k.architecture.clean.movie.viewmodel

import androidx.lifecycle.viewModelScope
import com.baka3k.architecture.clean.base.viewmodel.BaseViewModel
import com.baka3k.architecture.clean.movie.viewstate.MovieView
import com.baka3k.architecture.clean.movie.viewstate.MoviesViewState
import com.baka3k.architecture.domain.Result
import com.baka3k.architecture.domain.interactor.ShowMovieListUseCase
import com.baka3k.architecture.domain.model.MovieList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val showMovieListUseCase: ShowMovieListUseCase
) : BaseViewModel<MoviesViewState>() {
    private var job: Job? = null
    fun loadMovies() {
        job?.cancel() // cancel all previous job
        dispatchState(state = store.state.copy(loading = true))

        job = viewModelScope.launch(handlerException) {
            // run parallel tasks
            delay(1000)
            val result = showMovieListUseCase.execute()
            dispatchState(state = store.state.copy(loading = false))
            handleResultUsecase(result)
        }
    }

    private fun handleResultUsecase(
        result: Result<MovieList>
    ) {
        if (result is Result.Success) {
            val movieViews = mutableListOf<MovieView>()
            val movieList = result.data
            val populaList = movieList.popularList.toMutableList()
            populaList.shuffle() // random for fun
            movieViews.add(MovieView(type = 0, popularList = populaList, movie = null))
            movieList.nowplayingList.forEach {
                movieViews.add(MovieView(type = 1, popularList = null, movie = it))
            }
            dispatchState(state = currentState.copy(movies = movieViews))
        } else if (result is Result.Error) {
            val error = result.exception
            dispatchError(error)
        }
    }

    override fun initState(): MoviesViewState {
        return MoviesViewState(
            loading = false,
            movies = emptyList(),
        )
    }
}
//package com.baka3k.architecture.clean.movie.nowplaying.viewmodel
//
//import androidx.lifecycle.viewModelScope
//import com.baka3k.architecture.clean.base.viewmodel.BaseViewModel
//import com.baka3k.architecture.domain.Result
//import com.baka3k.architecture.domain.interactor.GetMoviePopularUseCase
//import com.baka3k.architecture.domain.interactor.GetNowPlayingUseCase
//import com.baka3k.architecture.domain.model.Movie
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.async
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//
//class MoviesViewModel(
//    private val getNowPlayingUseCase: GetNowPlayingUseCase,
//    private val getMoviePopularUseCase: GetMoviePopularUseCase
//) : BaseViewModel<MoviesViewState>() {
//    private var job: Job? = null
//    fun loadNowPlaying() {
//        job?.cancel() // cancel all previous job
//        dispatchState(state = store.state.copy(loading = true))
//
//        job = viewModelScope.launch(handlerException) {
//            // run parallel tasks
//            delay(1000)
//            val nowPlayingUseCase = async {
//                getNowPlayingUseCase.execute()
//            }
//            val popularUseCase = async {
//                getMoviePopularUseCase.execute()
//            }
//            val nowPlayingResult = nowPlayingUseCase.await()
//            val popularResult = popularUseCase.await()
//            dispatchState(state = store.state.copy(loading = false))
//            handleResultUsecase(nowPlayingResult, popularResult)
//        }
//    }
//
//    private fun handleResultUsecase(
//        nowPlayingResult: Result<List<Movie>>,
//        popularResult: Result<List<Movie>>
//    ) {
//        val movieViews = mutableListOf<MovieView>()
//        if (popularResult is Result.Success) {
//            val movies = popularResult.data.toMutableList()
//            movies.shuffle()
//            val movieView = MovieView(type = 0, popularList = movies, movie = null)
//            movieViews.add(movieView)
//        } else if (popularResult is Result.Error) {
//            val error = popularResult.exception
//            dispatchError(error)
//            return
//        }
//        if (nowPlayingResult is Result.Success) {
//            val movies = nowPlayingResult.data
//            movies.forEach {
//                movieViews.add(MovieView(type = 1, popularList = null, movie = it))
//            }
//        } else if (nowPlayingResult is Result.Error) {
//            val error = nowPlayingResult.exception
//            dispatchError(error)
//            return
//        }
//        dispatchState(state = currentState.copy(movies = movieViews))
//    }
//
//    override fun initState(): MoviesViewState {
//        return MoviesViewState(
//            loading = false,
//            movies = emptyList(),
//        )
//    }
//}