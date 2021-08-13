package com.baka3k.architecture.domain.model

data class MovieList(
    val popularList: List<Movie>,
    val nowplayingList: List<Movie>
)