package com.cineflix.movies.Controllers;

import com.cineflix.movies.Models.Movie;
import com.cineflix.movies.Services.MovieService;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movies")
@CrossOrigin(origins = "http://localhost:5173")
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

    @PostMapping("/index")
    public Map<String, Integer> indexMovies() throws IOException {
        int indexedMovies = movieService.indexMovies();

        return Map.of("indexedMovies", indexedMovies);
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() throws IOException {
        return ResponseEntity.ok(movieService.getAllMovies());
    }
}
