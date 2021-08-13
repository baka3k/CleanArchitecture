package com.baka3k.architecture.domain.interactor

import com.baka3k.architecture.domain.Result
import com.baka3k.architecture.domain.SingleUseCase
import com.baka3k.architecture.domain.model.Movie
import com.baka3k.architecture.domain.repository.MovieInforRepository


class GetNowPlayingUseCase(
    private val mMovieInforRepository: MovieInforRepository
) : SingleUseCase<Result<List<Movie>>> {
    override suspend fun execute(): Result<List<Movie>> {
        return mMovieInforRepository.getMovieInfor()
    }
}