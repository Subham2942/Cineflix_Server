package com.cineflix.movies.Services;

import com.cineflix.movies.Models.Movie;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

@Service
public class MovieService {
    private final VectorStore vectorStore;
    private final JsonMapper jsonMapper;

    private static final int PINECONE_BATCH_SIZE = 100;

    public MovieService(VectorStore vectorStore,  JsonMapper jsonMapper) {
        this.vectorStore = vectorStore;
        this.jsonMapper = jsonMapper;
    }

    public int indexMovies() throws IOException {
        ClassPathResource moviesPath = new ClassPathResource("movies.json");

        List<Movie> movies;

        try(InputStream inputStreams = moviesPath.getInputStream()){
            movies = jsonMapper.readValue(inputStreams, new TypeReference<List<Movie>>(){});
        }

        List<Document> documents = movies.stream()
                .map(movie -> Document.builder()
                        .id(toId(movie.name()))
                        .text(movie.description())
                        .metadata("name", movie.name())
                        .build())
                .toList();

        for (int start = 0; start < documents.size(); start += PINECONE_BATCH_SIZE) {
            int end = Math.min(start + PINECONE_BATCH_SIZE, documents.size());

            List<Document> batch = documents.subList(start, end);
            vectorStore.add(batch);
        }

        return documents.size();

    }

    public String toId(String name){
        return name
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");
    }

    public List<Document> search(String userQuery)
    {
        return vectorStore
                .similaritySearch(
                        SearchRequest.builder()
                                .query(userQuery)
                                .topK(5)
                                .build()
                );
    }

    public List<Movie> getAllMovies() throws IOException {
        var resource = new ClassPathResource("movies.json");

        try (var inputStream = resource.getInputStream()) {
            return jsonMapper.readValue(
                    inputStream,
                    new TypeReference<List<Movie>>() {}
            );
        }
    }

}
