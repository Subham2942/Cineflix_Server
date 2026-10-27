package com.cineflix.movies.Controllers;

import com.cineflix.movies.Services.MovieService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/movies")
@ConditionalOnProperty(name = "app.indexing.enabled", havingValue = "true")
public class MovieIndexController {
    private final MovieService movieService;

    MovieIndexController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/index")
    public Map<String, Integer> indexMovies() throws IOException {
        int indexedMovies = movieService.indexMovies();

        return Map.of("indexedMovies", indexedMovies);
    }
}
