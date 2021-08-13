package com.baka3k.architecture.data.service.movie

import com.baka3k.architecture.data.BuildConfig
import com.baka3k.architecture.data.service.base.Service
import com.baka3k.architecture.data.service.movie.entity.ActorsResult
import com.baka3k.architecture.data.service.movie.entity.MovieDetailResult
import com.baka3k.architecture.data.service.movie.entity.MovieResult
import com.baka3k.architecture.data.utils.connectivity.NetworkConnection
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieInfoService {

    @GET("now_playing")
    suspend fun getNowPlaying(
        @Query("language") lang: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("api_key") clientId: String = BuildConfig.MOVIE_DB_ACCESS_KEY
    ): MovieResult

    @GET("popular")
    suspend fun getPopular(
        @Query("api_key") clientId: String = BuildConfig.MOVIE_DB_ACCESS_KEY
    ): MovieResult

    @GET("/3/movie/{movieID}")
    suspend fun getMovieDetail(
        @Path("movieID") movieID: Long,
        @Query("api_key") clientId: String = BuildConfig.MOVIE_DB_ACCESS_KEY
    ): MovieDetailResult
    @GET("/3/movie/{movieID}/credits")
    suspend fun getActors(
        @Path("movieID") movieID: Long,
        @Query("api_key") clientId: String = BuildConfig.MOVIE_DB_ACCESS_KEY
    ): ActorsResult

    companion object {
        private const val BASE_URL = "${MovieDBServer.URL_MOVIEDB}/3/movie/"
        fun create(networkConnection: NetworkConnection): MovieInfoService {
            return Service<MovieInfoService>(BASE_URL, networkConnection, null).create(
                MovieInfoService::class.java
            )
        }
    }

}