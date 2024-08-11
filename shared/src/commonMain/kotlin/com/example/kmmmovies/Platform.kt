package com.example.kmmmovies

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform