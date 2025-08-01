package com.berlin.aflami.viewmodel.home

import com.berlin.aflami.viewmodel.shareduistate.MediaType

interface HomeScreenInteractionListener {
    fun onSearchClicked()
    fun onAllContinueWatchingClicked()
    fun onAllTopRatingClicked()
    fun onMoodPickerClicked()
    fun onUpcomingMovieCardClicked(movieId: Long)
    fun onPopularMovieCardClicked(movieId: Long, mediaType: MediaType)
    fun onSelectUpcomingGenre(genreId: Int)
}