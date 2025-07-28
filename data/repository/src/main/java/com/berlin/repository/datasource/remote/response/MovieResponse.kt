package com.berlin.repository.datasource.remote.response

import com.berlin.repository.datasource.remote.dto.MovieDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
    @SerialName("results")
    val results: List<MovieDto?>? = null,
)
