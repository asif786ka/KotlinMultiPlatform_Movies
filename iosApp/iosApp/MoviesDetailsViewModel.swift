import Foundation
import shared

class MovieDetailViewModel: ObservableObject {
    @Published var movie: Movie? = nil
    private let repository: MoviesRepository

    init(repository: MoviesRepository = MoviesRepository()) {
        self.repository = repository
    }

    func getMovieDetails(imdbID: String) {
        repository.getMovieDetails(imdbID: imdbID) { result, error in
            if let result = result {
                DispatchQueue.main.async {
                    self.movie = result
                }
            } else if let error = error {
                print("Failed to fetch movie details: \(error)")
            }
        }
    }
}
