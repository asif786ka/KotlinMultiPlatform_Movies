import SwiftUI
import shared

struct MovieDetailView: View {
    @ObservedObject var viewModel = MovieDetailViewModel()
    var imdbID: String

    var body: some View {
        VStack {
            if let movie = viewModel.movie {
                if let posterURL = movie.Poster, let url = URL(string: posterURL) {
                    if #available(iOS 15.0, *) {
                        AsyncImage(url: url) { image in
                            image
                                .resizable()
                                .aspectRatio(contentMode: .fill)
                                .frame(maxHeight: 300)
                                .clipped()
                        } placeholder: {
                            Color.gray
                                .frame(maxHeight: 300)
                        }
                    } else {
                        // Fallback on earlier versions
                    }
                }
                Text(movie.Title ?? "Unknown Title")
                    .font(.largeTitle)
                Text("Year: \(movie.Year ?? "Unknown")")
                    .font(.title2)
                Text(movie.Plot ?? "No description available")
                    .font(.body)
                    .padding()
            } else {
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle())
                    .scaleEffect(2)
            }
        }
        .onAppear {
            viewModel.getMovieDetails(imdbID: imdbID)
        }
        .navigationTitle(viewModel.movie?.Title ?? "Loading...")
    }
}

