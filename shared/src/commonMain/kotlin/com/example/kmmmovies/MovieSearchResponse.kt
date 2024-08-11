package com.example.kmmmovies

import kotlinx.serialization.Serializable

@Serializable
data class MovieSearchResponse(
    val Search: List<Movie>? = null,
    val totalResults: String? = null,
    val Response: String? = null
)
