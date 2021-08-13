package com.baka3k.architecture.data.service.movie.entity

import com.google.gson.annotations.SerializedName

data class ActorsResult(
    @field:SerializedName("id") val id: Long,
    @field:SerializedName("cast") val cast: List<Cast>,
    @field:SerializedName("crew") val crew: List<Cast>
)

data class Cast(
    @field:SerializedName("adult") val adult: Boolean,
    @field:SerializedName("gender") val gender: Long,
    @field:SerializedName("id") val id: Long,
    @field:SerializedName("known_for_department") val knownForDepartment: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("original_name") val originalName: String,
    @field:SerializedName("popularity") val popularity: Double,
    @field:SerializedName("profile_path") val profilePath: String? = null,
    @field:SerializedName("cast_id") val castID: Long? = null,
    @field:SerializedName("character") val character: String? = null,
    @field:SerializedName("credit_id") val creditID: String,
    @field:SerializedName("order") val order: Long? = null,
    @field:SerializedName("department") val department: String? = null,
    @field:SerializedName("job") val job: String? = null
)
