package com.baka3k.architecture.clean.movie.viewstate

import com.baka3k.architecture.domain.model.MovieDetail


data class MovieDetailViewState(
    val loading: Boolean,
    val movie: MovieDetail,
)

