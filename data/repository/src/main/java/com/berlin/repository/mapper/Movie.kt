package com.berlin.repository.mapper

import com.berlin.entity.Genre
import com.berlin.entity.Movie
import com.berlin.entity.ProductionCompany
import com.berlin.repository.datasource.local.dto.SearchingEntity
import com.berlin.repository.datasource.remote.dto.GenreDto
import com.berlin.repository.datasource.remote.dto.MovieDetailsDto
import com.berlin.repository.datasource.remote.dto.MovieDto
import com.berlin.repository.datasource.remote.dto.ProductionCompanyDto
import kotlinx.datetime.LocalDate
import java.time.Instant

fun SearchingEntity.toDomain(): Movie {
    return Movie(
        id = TODO(),
        title = TODO(),
        rating = TODO(),
        releaseDate = TODO(),
        posterURL = TODO(),
        screenShot = TODO(),
        description = TODO(),
        genres = TODO(),
        duration = TODO(),
        hasVideo = TODO(),
        productionCompanies = TODO(),
        originCountry = TODO(),
        galleryUrl = TODO()
    )
}

fun MovieDto.toLocal(query: String, type: String, page: Int, mediaType: String): SearchingEntity {
    return SearchingEntity(
        query = query,
        type = type,
        time = Instant.now().epochSecond,
        id = this.id?.toLong() ?: 0L,
        title = this.title ?: "",
        rating = this.voteAverage ?: 0.0,
        releaseYear = (releaseDate ?: ""),
        genre = this.genreIds?.filterNotNull() ?: emptyList(),
        poster = "$POSTER_PREFIX${this.posterPath.orEmpty()}",
        page = page,
        mediaType = mediaType
    )
}

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = this.id?.toLong() ?: 0L,
        title = this.title?:"Movie Title",
        rating = this.voteAverage ?: 0.0,
        releaseDate = this.releaseDate ?: "Unknown Release Date",
        posterURL = this.posterPath?.let { "$POSTER_PREFIX$it" } ?: "",
        description = this.overview ?: "No description available",
        genres = this.genreIds?.map { Genre( 0, "") } ?: emptyList(),
    )
}

fun MovieDetailsDto.toDomain(): Movie {
    return Movie(
        id = this.id?.toLong() ?: 0L,
        title = this.title.orEmpty(),
        description = this.overview.orEmpty(),
        posterURL = "$POSTER_PREFIX${this.posterPath.orEmpty()}",
        screenShot = "$BACKDROP_PREFIX${this.backdropPath.orEmpty()}",
        releaseDate = this.releaseDate.orEmpty(),
        rating = this.voteAverage ?: 0.0,
        duration = this.runtime ?: 0,
        genres = this.genres?.map {it.toDomain()  } ?: emptyList(),
        productionCompanies = this.productionCompanies?.map { it.toDomain() },
        hasVideo = this.video,
        originCountry = this.originCountry?.get(0),
    )
}


fun stringToLocalDate(dateString: String): LocalDate {
    return runCatching {
        LocalDate.parse(dateString)
    }.getOrElse { LocalDate.parse("1960-01-01") }
}

fun GenreDto.toEntity() = Genre(
    id = this.id ?: 0, name = this.name.orEmpty()
)

fun ProductionCompanyDto.toDomain() = ProductionCompany(
    id = this.id?: 0,
    name = this.name?: "Unknown Production Company",
    posterURL = this.logoPath ?: ("$POSTER_PREFIX/default_poster.png"),
    originCountry = this.originCountry?: "Unknown Country"
)

fun Int?.formatRuntime(): String? {
    if (this == null || this == 0) return null
    val hours = this / 60
    val remainingMinutes = this % 60
    return "${hours}h ${remainingMinutes}m"
}

const val POSTER_PREFIX = "https://image.tmdb.org/t/p/w500"
const val BACKDROP_PREFIX = "https://image.tmdb.org/t/p/original"
