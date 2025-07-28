package com.berlin.repository.datasource.remote.response

import com.berlin.repository.datasource.remote.dto.MovieDto
import com.berlin.repository.datasource.remote.dto.TVShowDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopRatedMoviesResponse(
    @SerialName("results")
    val topRatedMovies: List<MovieDto>,
    @SerialName("page")
    val page: Int,
)

@Serializable
data class TopRatedSeriesResponse(
    @SerialName("results")
    val topRatedSeries: List<TVShowDto>,
    @SerialName("page")
    val page: Int,
)