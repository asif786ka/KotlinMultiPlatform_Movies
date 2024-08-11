package com.example.kmmmovies.android

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.kmmmovies.Movie

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "search") {
                composable("search") { MovieSearchScreen(navController) }
                composable("details/{imdbID}") { backStackEntry ->
                    val imdbID = backStackEntry.arguments?.getString("imdbID")
                    imdbID?.let { MovieDetailScreen(navController, it) }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieSearchScreen(navController: NavHostController) {
    val viewModel: MainViewModel = viewModel()
    var query by remember { mutableStateOf("") }
    val movies = viewModel.movies.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("KMM MovieS Search") })
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Search Movies") },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { viewModel.searchMovies(query) },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text("Search")
                }
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    movies.value?.let { movieList ->
                        items(movieList.size) { index ->
                            MovieItem(movieList[index]) {
                                navController.navigate("details/${movieList[index].imdbID}")
                            }
                        }
                    }
                }
            }

            if (isLoading.value) {
                Dialog(onDismissRequest = {}) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(4.dp, shape = MaterialTheme.shapes.medium)
            .clickable { onClick() },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (!movie.Poster.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(movie.Poster),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(bottom = 8.dp)
                )
            }
            Text(text = movie.Title ?: "Unknown Title", style = MaterialTheme.typography.titleLarge)
            Text(text = "Year: ${movie.Year ?: "Unknown"}", style = MaterialTheme.typography.bodyMedium)
            Text(text = movie.Plot ?: "No description available", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(navController: NavHostController, imdbID: String) {
    val viewModel: MainViewModel = viewModel()
    var movieDetails by remember { mutableStateOf<Movie?>(null) }

    LaunchedEffect(imdbID) {
        viewModel.getMovieDetails(imdbID).let {
            movieDetails = it
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(movieDetails?.Title ?: "Loading...") })
        }
    ) {
        if (movieDetails != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (!movieDetails!!.Poster.isNullOrEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(movieDetails!!.Poster),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Title: ${movieDetails!!.Title}", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Year: ${movieDetails!!.Year}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Plot: ${movieDetails!!.Plot}", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

