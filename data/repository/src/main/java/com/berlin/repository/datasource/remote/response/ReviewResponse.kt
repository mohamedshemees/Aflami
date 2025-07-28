package com.berlin.repository.datasource.remote.response

import com.berlin.repository.datasource.remote.dto.ReviewDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewResponse(
    @SerialName("results")
    val results: List<ReviewDto?>? = null
)