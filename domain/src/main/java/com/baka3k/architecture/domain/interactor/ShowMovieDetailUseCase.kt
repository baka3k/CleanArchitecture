package com.baka3k.architecture.domain.interactor

import com.baka3k.architecture.domain.Result
import com.baka3k.architecture.domain.SingleUseCaseWithParameter
import com.baka3k.architecture.domain.model.MovieDetail
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class ShowMovieDetailUseCase(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getActorsUseCase: GetActorsUseCase
) :
    SingleUseCaseWithParameter<Long, Result<MovieDetail>> {
    override suspend fun execute(movieId: Long): Result<MovieDetail> {
        var result: Result<MovieDetail> = Result.Success(MovieDetail())
        coroutineScope {
            //run parallel task at the same time - using async to sync/combine state of tasks
            val movieDetailUseCase = async {
                getMovieDetailUseCase.execute(movieId)
            }
            val actorsUseCase = async {
                getActorsUseCase.execute(movieId)
            }
            // get return data
            val movieDetailUseCaseResult = movieDetailUseCase.await()
            val actorsUseCaseResult = actorsUseCase.await()
            if (movieDetailUseCaseResult is Result.Error) {
                result = movieDetailUseCaseResult
                return@coroutineScope
            } else if (actorsUseCaseResult is Result.Error) {
                result = actorsUseCaseResult
                return@coroutineScope
            }
            var returnData = MovieDetail()
            if (movieDetailUseCaseResult is Result.Success) {
                val movieDetail = movieDetailUseCaseResult.data
                returnData.category = movieDetail.category
                returnData.cast = movieDetail.cast
                returnData.content = movieDetail.content
                returnData.cover = movieDetail.cover
                returnData.languge = movieDetail.languge
                returnData.rating = movieDetail.rating
                returnData.time = movieDetail.time
                returnData.title = movieDetail.title
            }
            if (actorsUseCaseResult is Result.Success) {
                val actors = actorsUseCaseResult.data
                returnData.actors.addAll(actors)
            }
            result = Result.Success(returnData)
        }
        return result
    }
}