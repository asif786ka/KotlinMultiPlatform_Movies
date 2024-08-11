package com.example.kmmmovies.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kmmmovies.Movie
import com.example.kmmmovies.MoviesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val moviesRepository: MoviesRepository = MoviesRepository()
) : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>?>(null)
    val movies: StateFlow<List<Movie>?> = _movies.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun searchMovies(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val fetchedMovies = moviesRepository.searchMovies(query)
            _movies.value = fetchedMovies
            _isLoading.value = false
        }
    }

    fun getMovieDetails(imdbID: String): Movie? {
        var movie: Movie? = null
        viewModelScope.launch {
            _isLoading.value = true
            movie = moviesRepository.getMovieDetails(imdbID)
            _isLoading.value = false
        }
        return movie
    }

}
