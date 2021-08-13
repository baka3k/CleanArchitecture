package com.baka3k.architecture.domain.interactor

import com.baka3k.architecture.domain.Result
import com.baka3k.architecture.domain.SingleUseCaseWithParameter
import com.baka3k.architecture.domain.model.MovieDetail
import com.baka3k.architecture.domain.repository.MovieInforRepository

class GetMovieDetailUseCase(private val movieInforRepository: MovieInforRepository) :
    SingleUseCaseWithParameter<Long, Result<MovieDetail>> {
    override suspend fun execute(movieId: Long): Result<MovieDetail> {
        return movieInforRepository.getMovieDetailByMovieID(movieId)
    }
}