package com.baka3k.architecture.data.repository

import com.baka3k.architecture.data.service.movie.entity.ActorsResult
import com.baka3k.architecture.data.service.movie.entity.MovieDetailResult
import com.baka3k.architecture.domain.model.Actor
import com.baka3k.architecture.domain.model.MovieDetail

fun com.baka3k.architecture.data.service.movie.entity.Movie.toDomainEntity() =
    com.baka3k.architecture.domain.model.Movie(
        adult = adult,
        backdropPath = backdropPath,
        genreIDS = genreIDS ?: emptyList(),
        id = id,
        originalLanguage = originalLanguage?.name,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = "https://image.tmdb.org/t/p/w300${posterPath}",
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount
    )

fun MovieDetailResult.toDomainEntity(): MovieDetail {
    return MovieDetail(
        cast = "",
        title = title ?: "",
        content = overview ?: "",
        time = "${releaseDate ?: ""} | $runtime ",
        languge = originalLanguage ?: "",
        rating = "5/10",
        category = "$genres",
        cover = "https://image.tmdb.org/t/p/w300${backdropPath}",
    )
}

fun ActorsResult.toDomainEntity(): List<Actor> {
    val list = mutableListOf<Actor>()
    crew.forEach {
        if (!it.profilePath.isNullOrEmpty() && it.profilePath.endsWith(".jpg")) {
            list.add(Actor(it.name, "https://image.tmdb.org/t/p/w185${it.profilePath}"))
        }
    }
    cast.forEach {
        if (!it.profilePath.isNullOrEmpty() && it.profilePath.endsWith(".jpg")) {
            list.add(Actor(it.name, "https://image.tmdb.org/t/p/w185${it.profilePath}"))
        }
    }
    list.distinctBy { it.name }
    return list
}
