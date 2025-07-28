package com.berlin.repository.datasource.remote.response

import com.berlin.repository.datasource.remote.dto.TVShowDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TVShowResponse(
    @SerialName("results")
    val results: List<TVShowDto?>? = null,
)
