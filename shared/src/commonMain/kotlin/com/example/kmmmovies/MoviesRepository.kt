package com.example.kmmmovies

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import co.touchlab.kermit.Logger


class MoviesRepository {
    private val client = HttpClient()
    private val json = Json { ignoreUnknownKeys = true }
    private val logger = Logger.withTag("MoviesRepository")

    suspend fun searchMovies(query: String): List<Movie>? {
        return try {
            val response: HttpResponse = client.get("https://www.omdbapi.com/?s=$query&apikey=73e89049")
            val rawResponse = response.bodyAsText()
            logger.d { "Raw response: $rawResponse" }

            if (rawResponse.isBlank()) {
                logger.e { "Received an empty response from the API" }
                null
            } else {
                val movieSearchResponse = json.decodeFromString<MovieSearchResponse>(rawResponse)
                logger.d { "Parsed response: $movieSearchResponse" }
                movieSearchResponse.Search
            }
        } catch (e: Exception) {
            logger.e(e) { "Error parsing JSON response" }
            null
        }
    }

    suspend fun getMovieDetails(imdbID: String): Movie? {
        return try {
            val response: HttpResponse = client.get("https://www.omdbapi.com/?i=$imdbID&apikey=73e89049")
            val rawResponse = response.bodyAsText()
            logger.d { "Raw response: $rawResponse" }

            if (rawResponse.isBlank()) {
                logger.e { "Received an empty response from the API" }
                null
            } else {
                json.decodeFromString<Movie>(rawResponse)
            }
        } catch (e: Exception) {
            logger.e(e) { "Error parsing JSON response" }
            null
        }
    }
}


