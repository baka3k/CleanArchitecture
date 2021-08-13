package com.baka3k.architecture.domain.interactor

import com.baka3k.architecture.domain.Result
import com.baka3k.architecture.domain.SingleUseCase
import com.baka3k.architecture.domain.model.Movie
import com.baka3k.architecture.domain.model.MovieList
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class ShowMovieListUseCase(
    private val getMoviePopularUseCase: GetMoviePopularUseCase,
    private val getNowPlayingUseCase: GetNowPlayingUseCase
) : SingleUseCase<Result<MovieList>> {
    override suspend fun execute(): Result<MovieList> {
        var result: Result<MovieList> = Result.Success(MovieList(emptyList(), emptyList()))
        coroutineScope {
            val nowPlayingUseCase = async {
                getNowPlayingUseCase.execute()
            }
            val popularUseCase = async {
                getMoviePopularUseCase.execute()
            }
            val nowPlayingResult = nowPlayingUseCase.await()
            val popularResult = popularUseCase.await()
            if (nowPlayingResult is Result.Error) {
                result = nowPlayingResult
                return@coroutineScope
            } else if (popularResult is Result.Error) {
                result = popularResult
                return@coroutineScope
            }
            var nowPlayingList = emptyList<Movie>()
            var popularList = emptyList<Movie>()
            if (nowPlayingResult is Result.Success) {
                nowPlayingList = nowPlayingResult.data
            }
            if (popularResult is Result.Success) {
                popularList = popularResult.data
            }
            val movieList = MovieList(popularList = popularList, nowplayingList = nowPlayingList)
            result = Result.Success(movieList)
        }
        return result
    }
}