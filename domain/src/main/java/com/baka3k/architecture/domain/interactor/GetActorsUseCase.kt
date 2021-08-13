package com.baka3k.architecture.domain.interactor

import com.baka3k.architecture.domain.Result
import com.baka3k.architecture.domain.SingleUseCaseWithParameter
import com.baka3k.architecture.domain.model.Actor
import com.baka3k.architecture.domain.repository.MovieInforRepository

class GetActorsUseCase(private val movieInforRepository: MovieInforRepository) :
    SingleUseCaseWithParameter<Long, Result<List<Actor>>> {
    override suspend fun execute(movieId: Long): Result<List<Actor>> {
        return movieInforRepository.getActors(movieId)
    }
}