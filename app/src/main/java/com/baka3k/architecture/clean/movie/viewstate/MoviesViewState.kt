package com.baka3k.architecture.clean.movie.viewstate

import com.baka3k.architecture.domain.model.Movie

data class MoviesViewState(
    val loading: Boolean,
    val movies: List<MovieView>
)

data class MovieView(
    val type: Int = 0,
    val popularList: List<Movie>? = null,
    val movie: Movie? = null
)