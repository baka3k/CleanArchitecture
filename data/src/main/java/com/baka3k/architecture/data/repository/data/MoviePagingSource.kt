package com.baka3k.architecture.data.repository.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.baka3k.architecture.data.repository.toDomainEntity
import com.baka3k.architecture.data.service.movie.MovieInfoService
import com.baka3k.architecture.domain.model.Movie

private const val MOVIE_STARTING_PAGE_INDEX = 1

class MoviePagingSource(
    private val service: MovieInfoService,
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: MOVIE_STARTING_PAGE_INDEX
        return try {
            val response = service.getNowPlaying(page = page)
            val movies = ArrayList<Movie>()
            response?.results.forEach {
                movies.add(it.toDomainEntity())
            }
            LoadResult.Page(
                data = movies,
                prevKey = if (page == MOVIE_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page == response.totalPages.toInt()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return 0
    }

}


