package com.baka3k.architecture.clean.movie.viewmodel

import androidx.lifecycle.viewModelScope
import com.baka3k.architecture.clean.base.viewmodel.BaseViewModel
import com.baka3k.architecture.clean.movie.viewstate.MovieDetailViewState
import com.baka3k.architecture.domain.Result
import com.baka3k.architecture.domain.interactor.ShowMovieDetailUseCase
import com.baka3k.architecture.domain.model.MovieDetail

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val showMovieDetailUseCase: ShowMovieDetailUseCase
) : BaseViewModel<MovieDetailViewState>() {
    private var job: Job? = null
    fun loadMovie(movieID: Long) {
        job?.cancel() // cancel all previous job
        dispatchState(state = store.state.copy(loading = true))
        job = viewModelScope.launch(handlerException) {
            // run parallel tasks
            val result = showMovieDetailUseCase.execute(movieID)
            dispatchState(state = store.state.copy(loading = false))
            handleResultUsecase(result)
        }
    }

    private fun handleResultUsecase(
        result: Result<MovieDetail>
    ) {
        if (result is Result.Success) {
            val movieDetail = result.data
            dispatchState(state = currentState.copy(movie = movieDetail))
        } else if (result is Result.Error) {
            val error = result.exception
            dispatchError(error)
        }
    }

    override fun initState(): MovieDetailViewState {
        return MovieDetailViewState(
            loading = false,
            movie = MovieDetail(),
        )
    }
}