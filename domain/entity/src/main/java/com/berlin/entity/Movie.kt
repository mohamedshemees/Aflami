package com.berlin.entity


data class Movie(
    val id: Long,
    val title: String,
    val rating: Double,
    val releaseDate: String,
    val posterURL: String,
    val screenShot: String?=null,
    val description: String?=null,
    val genres: List<Genre>,
    val duration: Int?=null,
    val hasVideo: Boolean?= null,
    val productionCompanies: List<ProductionCompany>? = null,
    val originCountry: String?=null,
    val galleryUrl:List<String>?=null
)
