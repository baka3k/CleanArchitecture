package com.baka3k.architecture.data.service.movie.entity

import com.google.gson.annotations.SerializedName


data class MovieDetailResult(
    @field:SerializedName("adult") val adult: Boolean = false,
    @field:SerializedName("backdrop_path") val backdropPath: String?,
    @field:SerializedName("belongs_to_collection") val belongsToCollection: BelongsToCollection?,
    @field:SerializedName("budget") val budget: Long = 0,
    @field:SerializedName("genres") val genres: List<Genre> = emptyList(),
    @field:SerializedName("homepage") val homepage: String?,
    @field:SerializedName("id") val id: Long = 0,
    @field:SerializedName("imdbID") val imdbID: String?,
    @field:SerializedName("original_language") val originalLanguage: String?,
    @field:SerializedName("original_title") val originalTitle: String?,
    @field:SerializedName("overview") val overview: String?,
    @field:SerializedName("popularity") val popularity: Double = 0.0,
    @field:SerializedName("posterPath") val posterPath: String?,
    @field:SerializedName("production_companies") val productionCompanies: List<ProductionCompany> = emptyList(),
    @field:SerializedName("production_countries") val productionCountries: List<ProductionCountry> = emptyList(),
    @field:SerializedName("release_date") val releaseDate: String?,
    @field:SerializedName("revenue") val revenue: Long = 0,
    @field:SerializedName("runtime") val runtime: Long = 0,
    @field:SerializedName("spoken_languages") val spokenLanguages: List<SpokenLanguage> = emptyList(),
    @field:SerializedName("status") val status: String?,
    @field:SerializedName("tagline") val tagline: String?,
    @field:SerializedName("title") val title: String?,
    @field:SerializedName("video") val video: Boolean = false,
    @field:SerializedName("vote_average") val voteAverage: Double = 0.0,
    @field:SerializedName("vote_count") val voteCount: Long = 0
)

data class BelongsToCollection(
    @field:SerializedName("id") val id: Long = 0,
    @field:SerializedName("name") val name: String?,
    @field:SerializedName("poster_path") val posterPath: String?,
    @field:SerializedName("backdrop_path") val backdropPath: String?
)

data class Genre(
    @field:SerializedName("id") val id: Long = 0,
    @field:SerializedName("name") val name: String?
)

data class ProductionCompany(
    @field:SerializedName("id") val id: Long = 0,
    @field:SerializedName("logo_path") val logoPath: String? = null,
    @field:SerializedName("name") val name: String?,
    @field:SerializedName("origin_country") val originCountry: String?
)

data class ProductionCountry(
    @field:SerializedName("iso_3166_1") val iso3166_1: String?,
    @field:SerializedName("name") val name: String?
)

data class SpokenLanguage(
    @field:SerializedName("english_name") val englishName: String?,
    @field:SerializedName("iso_639_1") val iso639_1: String?,
    @field:SerializedName("name") val name: String?
)
