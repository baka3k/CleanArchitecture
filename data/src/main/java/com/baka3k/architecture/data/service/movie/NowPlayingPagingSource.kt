package com.baka3k.architecture.data.service.movie

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.baka3k.architecture.data.repository.MovieInforRepositoryImpl
import com.baka3k.architecture.data.repository.toDomainEntity
import com.baka3k.architecture.domain.model.Movie

private const val NOW_PLAYING_STARTING_PAGE_INDEX = 1

class NowPlayingPagingSource(
    private val movieInfoService: MovieInfoService
) : PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: NOW_PLAYING_STARTING_PAGE_INDEX
        return try {
            val response = movieInfoService.getNowPlaying(page = page)
            Log.d(MovieInforRepositoryImpl.TAG, "getMovieInfor() $response")
            val movies = ArrayList<Movie>()
            response.results.forEach {
                movies.add(it.toDomainEntity())
            }
            LoadResult.Page(
                data = movies,
                prevKey = if (page == NOW_PLAYING_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page.toLong() == response.totalPages) null else page + 1
            )
        } catch (e: Exception) {
            Log.e("NowPlayingPagingSource", "#load() error $e")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        TODO("Not yet implemented")
    }
}