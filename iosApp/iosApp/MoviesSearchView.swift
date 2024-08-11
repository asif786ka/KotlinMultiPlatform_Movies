import SwiftUI
import shared

struct MovieSearchView: View {
    @ObservedObject var viewModel = MoviesViewModel()
    @State private var query: String = ""

    var body: some View {
        NavigationView {
            ZStack {
                VStack {
                    TextField("Search Movies", text: $query)
                        .textFieldStyle(RoundedBorderTextFieldStyle())
                        .padding()

                    Button("Search") {
                        // Trigger the search when the button is tapped
                        viewModel.searchMovies(query: query)
                    }
                    .padding()
                    .frame(maxWidth: .infinity)
                    .background(Color.blue)
                    .foregroundColor(.white)
                    .cornerRadius(10)

                    List(viewModel.movies, id: \.imdbID) { movie in
                        NavigationLink(destination: MovieDetailView(imdbID: movie.imdbID ?? "")) {
                            MovieRow(movie: movie)
                        }
                    }
                }
                .navigationTitle("Movie Search")

                // Display loading indicator if loading
                if viewModel.isLoading {
                    Color.black.opacity(0.4)
                        .edgesIgnoringSafeArea(.all)
                    ProgressView()
                        .progressViewStyle(CircularProgressViewStyle())
                        .scaleEffect(2)
                }
            }
        }
    }
}
