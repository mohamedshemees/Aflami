package com.berlin.aflami.viewmodel.searchcountry

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.berlin.aflami.viewmodel.base.BasePagingSource
import com.berlin.aflami.viewmodel.base.BaseViewModel
import com.berlin.aflami.viewmodel.base.ErrorUiState
import com.berlin.aflami.viewmodel.mapper.toUIState
import com.berlin.aflami.viewmodel.shareduistate.MovieUIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import usecase.SearchByCountryUseCase
import java.util.Locale

class SearchByCountryViewModel(
    private val searchByCountry: SearchByCountryUseCase,
) : BaseViewModel<SearchByCountryScreenUiState, SearchByCountryEffect>(
    SearchByCountryScreenUiState()
), SearchByCountryInteractionListener {

    private val countriesNames = getCountriesNames()

    override fun onCountryNameChanged(countryName: CharSequence) {
        _screenState.update {
            it.copy(
                query = countryName.toString(),
                filteredCountries = filterCountriesByName(countryName.toString()),
                dropDownExpanded = countryName.isNotBlank()
                        && screenState.value.filteredCountries.isNotEmpty()
            )
        }
    }

    private fun filterCountriesByName(countryName: String): List<String> {
        return countriesNames.filter { country ->
            country.startsWith(countryName.trim(), ignoreCase = true)
        }
    }

    override fun onCountryClicked() {
        _screenState.update {
            it.copy(
                isLoading = true,
                isCountrySelected = true,
                dropDownExpanded = false,
                error = null
            )
        }

        tryToCall(
            call = {
                Pager(
                    config = PagingConfig(pageSize = 10, initialLoadSize = 10),
                    pagingSourceFactory = {
                        BasePagingSource(
                            call = { page ->
                                getCountryIsoCode(screenState.value.query)?.let {
                                    searchByCountry.invoke(query = it, page = page)
                                } ?: emptyList()
                            }
                        )
                    },
                ).flow
                    .map { it.map { it.toUIState() } }
                    .cachedIn(viewModelScope)
            },
            onSuccess = ::onSearchSuccess,
            onError = ::onSearchError
        )
    }

    private fun onSearchSuccess(movies: Flow<PagingData<MovieUIState>>) {
        _screenState.update { it.copy(isLoading = false, movies = movies) }
    }

    private fun onSearchError(error: ErrorUiState) {
        // TODO: Handle error
        _screenState.update { it.copy(error = error.message, isLoading = false) }
    }

    override fun onDismissDropDown() {
        _screenState.update { it.copy(dropDownExpanded = false) }
    }

    override fun onMovieClicked(movieId: Int) {
        sendNewEffect(SearchByCountryEffect.NavigatedToMovieDetailsScreen(movieId, "MOVIE"))
    }

    override fun onBackClicked() {
        sendNewEffect(SearchByCountryEffect.NavigatedBack)
    }

    private fun getCountriesNames(): List<String> {
        return countryNameToIsoMap.keys.sorted()
    }

    private fun getCountryIsoCode(countryName: String): String? {
        return countryNameToIsoMap[countryName]
    }

    private val countryNameToIsoMap: Map<String, String> by lazy {
        Locale.getISOCountries().associateBy(
            keySelector = { Locale("", it).displayCountry },
            valueTransform = { it }
        )
    }
}