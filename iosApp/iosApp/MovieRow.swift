import SwiftUI
import shared

struct MovieRow: View {
    var movie: Movie

    var body: some View {
        VStack(alignment: .leading) {
            if let posterURL = movie.Poster, let url = URL(string: posterURL) {
                if #available(iOS 15.0, *) {
                    AsyncImage(url: url) { image in
                        image
                            .resizable()
                            .aspectRatio(contentMode: .fill)
                            .frame(maxHeight: 200)
                            .clipped()
                    } placeholder: {
                        Color.gray
                            .frame(maxHeight: 200)
                    }
                } else {
                    // Fallback for earlier iOS versions
                }
            }

            Text(movie.Title ?? "Unknown Title")
                .font(.headline)
            Text("Year: \(movie.Year ?? "Unknown")")
                .font(.subheadline)
            Text(movie.Plot ?? "No description available")
                .font(.body)
        }
        .padding()
        .background(Color.white)
        .cornerRadius(10)
        .shadow(radius: 4)
        .padding(.vertical, 8)
    }
}

