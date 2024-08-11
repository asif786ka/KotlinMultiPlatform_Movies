import Foundation
import shared

class MoviesViewModel: ObservableObject {
    @Published var movies: [Movie] = []
    @Published var isLoading: Bool = false
    private let repository: MoviesRepository

    init(repository: MoviesRepository = MoviesRepository()) {
        self.repository = repository
    }

    func searchMovies(query: String) {
        // Set loading state
        isLoading = true

        // Call the search function from the shared code
        repository.searchMovies(query: query) { result, error in
            DispatchQueue.main.async {
                // Reset loading state
                self.isLoading = false

                // Handle results or errors
                if let result = result {
                    self.movies = result
                } else if let error = error {
                    print("Failed to fetch movies: \(error)")
                }
            }
        }
    }
}


