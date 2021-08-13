package com.baka3k.architecture.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.baka3k.architecture.data.repository.data.MoviePagingSource
import com.baka3k.architecture.data.service.movie.MovieInfoService
import com.baka3k.architecture.domain.Result
import com.baka3k.architecture.domain.model.Actor
import com.baka3k.architecture.domain.model.Movie
import com.baka3k.architecture.domain.model.MovieDetail
import com.baka3k.architecture.domain.repository.MovieInforRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class MovieInforRepositoryImpl(
    private val dataSource: MovieInfoService,
    private val dispatcher: CoroutineDispatcher
) : MovieInforRepository {
    companion object {
        const val TAG = "MovieInforRepository"
    }

    override suspend fun getMovieInfor(page: Int): Flow<Result<List<Movie>>> =
        withContext(dispatcher) {
            channelFlow {
                val page = Pager(
                    config = PagingConfig(enablePlaceholders = false, pageSize = 25),
                    pagingSourceFactory = { MoviePagingSource(service = dataSource) }
                )
                page.flow.collectLatest {
                    val movies = mutableListOf<Movie>()
                    it.map {
                        movies.add(it)
                    }
                    send(Result.Success(movies))
                }
            }
        }

    override suspend fun getMovieInfor(): Result<List<Movie>> = withContext(dispatcher) {
        try {
            val dataResponse = dataSource.getNowPlaying()
            val movies = ArrayList<Movie>()
            dataResponse?.results.forEach {
                movies.add(it.toDomainEntity())
            }
            Result.Success(movies)
        } catch (e: Exception) {
            Result.Error(e)
        }

    }

    override suspend fun getPopular(): Result<List<Movie>> = withContext(dispatcher) {
        try {
            val dataResponse = dataSource.getPopular()
            val movies = ArrayList<Movie>()
            dataResponse?.results.forEach {
                movies.add(it.toDomainEntity())
            }
            Result.Success(movies)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getMovieDetailByMovieID(movieId: Long): Result<MovieDetail> =
        withContext(dispatcher)
        {
            try {
                val dataResponse = dataSource.getMovieDetail(movieId)
                val movieDetail = dataResponse.toDomainEntity()
                Result.Success(movieDetail)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun getActors(movieId: Long): Result<List<Actor>> = withContext(dispatcher)
    {
        try {
            val dataResponse = dataSource.getActors(movieId)
            Log.d(TAG, "getActors() $dataResponse")
            val actors = dataResponse.toDomainEntity()
            Result.Success(actors.toList())
        } catch (e: Exception) {
            Result.Error(e)
        }

    }
}




