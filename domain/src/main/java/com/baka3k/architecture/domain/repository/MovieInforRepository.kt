package com.baka3k.architecture.domain.repository

import com.baka3k.architecture.domain.Result
import com.baka3k.architecture.domain.model.Actor
import com.baka3k.architecture.domain.model.Movie
import com.baka3k.architecture.domain.model.MovieDetail
import kotlinx.coroutines.flow.Flow

interface MovieInforRepository {
    suspend fun getMovieInfor(page:Int): Flow<Result<List<Movie>>>
    suspend fun getMovieInfor(): Result<List<Movie>>
    suspend fun getPopular(): Result<List<Movie>>
    suspend fun getMovieDetailByMovieID(movieId: Long): Result<MovieDetail>
    suspend fun getActors(movieId: Long): Result<List<Actor>>
}