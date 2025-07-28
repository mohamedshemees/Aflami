package com.berlin.repository.datasource.remote

import com.berlin.repository.datasource.remote.response.TopRatedMoviesResponse
import com.berlin.repository.datasource.remote.response.TopRatedSeriesResponse

interface HomeRemoteDataSource {
    suspend fun getTopRatedMovies(page: Int): TopRatedMoviesResponse
    suspend fun getTopRatedSeries(page: Int): TopRatedSeriesResponse
}