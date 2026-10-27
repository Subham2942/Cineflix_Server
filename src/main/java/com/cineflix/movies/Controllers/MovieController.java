package com.cineflix.movies.Controllers;

import com.cineflix.movies.Models.Movie;
import com.cineflix.movies.Services.MovieService;
import org.springframework.ai.document.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private MovieService movieService;

    MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Document>> search(@RequestParam String userQuery) {
        List<Document> movies = movieService.search(userQuery);

        return ResponseEntity.ok(movies);
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() throws IOException {
        return ResponseEntity.ok(movieService.getAllMovies());
    }
}
