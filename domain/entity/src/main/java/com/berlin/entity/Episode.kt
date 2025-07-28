package com.berlin.entity

import kotlinx.datetime.LocalDate

data class Episode(
    val airDate: LocalDate,
    val episodeNumber: Int,
    val episodeId: Long,
    val name: String,
    val description: String,
    val duration: Int,
    val tvShowId: Int,
    val rating: Double,
)
