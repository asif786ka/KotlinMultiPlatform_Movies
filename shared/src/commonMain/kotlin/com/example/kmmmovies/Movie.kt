package com.example.kmmmovies

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val Title: String? = null,
    val Year: String? = null,
    val Poster: String? = null,
    val Plot: String? = null,
    val imdbID: String? = null
)

