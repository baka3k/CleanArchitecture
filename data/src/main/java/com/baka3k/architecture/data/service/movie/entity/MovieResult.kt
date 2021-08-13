package com.baka3k.architecture.data.service.movie.entity

import com.google.gson.annotations.SerializedName


data class MovieResult(
    @field:SerializedName("dates") var dates: Dates? = null,
    @field:SerializedName("page") var page: Long = 0,
    @field:SerializedName("results") var results: List<Movie> = emptyList(),
    @field:SerializedName("total_pages") var totalPages: Long = 0,
    @field:SerializedName("total_results") var totalResults: Long = 0,
)

data class Dates(
    @field:SerializedName("maximum")val maximum: String,
    @field:SerializedName("minimum")val minimum: String
)

data class Movie(
    @field:SerializedName("adult") val adult: Boolean,
    @field:SerializedName("backdrop_path") val backdropPath: String,
    @field:SerializedName("genre_ids") val genreIDS: List<Long>,
    @field:SerializedName("id") val id: Long,
    @field:SerializedName("original_language") val originalLanguage: OriginalLanguage,
    @field:SerializedName("original_title") val originalTitle: String,
    @field:SerializedName("overview") val overview: String,
    @field:SerializedName("popularity") val popularity: Double,
    @field:SerializedName("poster_path") val posterPath: String,
    @field:SerializedName("release_date") val releaseDate: String,
    @field:SerializedName("title") val title: String,
    @field:SerializedName("video") val video: Boolean,
    @field:SerializedName("vote_average") val voteAverage: Double,
    @field:SerializedName("vote_count") val voteCount: Long
)

enum class OriginalLanguage {
    En,
    Es,
    Ja
}
