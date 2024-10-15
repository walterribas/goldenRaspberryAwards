package com.outsera.controller;

import com.outsera.model.Movie;
import com.outsera.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.greaterThan;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieService movieService;

    private List<Movie> movies;

    @BeforeEach
    void setUp() throws Exception {
        movies = movieService.loadMoviesFromCSV();
        movieService.saveAllMovies(movies);
    }

    @Test
    void shouldReturnAllMovies() throws Exception {
        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].title").isString())
                .andExpect(jsonPath("$[0].studios").isString());
    }

    @Test
    void shouldReturnMovieById() throws Exception {
        Movie movie = movies.get(0);

        mockMvc.perform(get("/movies/{id}", movie.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value(movie.getTitle()));
    }

    @Test
    void shouldSaveNewMovie() throws Exception {
        int initialSize = movieService.getAllMovies().size();

        Movie newMovie = new Movie();
        newMovie.setReleaseYear(2023);
        newMovie.setTitle("New Movie");
        newMovie.setStudios("New Studio");
        newMovie.setProducers("New Producer");
        newMovie.setWinner(false);

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newMovie)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value(newMovie.getTitle()));

        List<Movie> allMovies = movieService.getAllMovies();
        assertThat(allMovies).hasSize(initialSize + 1);
    }

    @Test
    void shouldDeleteMovie() throws Exception {
        Movie movie = movies.get(0);

        mockMvc.perform(delete("/movies/{id}", movie.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/movies/{id}", movie.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnProducersInterval() throws Exception {
        mockMvc.perform(get("/movies/producers-interval"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.min.producer").exists())
                .andExpect(jsonPath("$.max.producer").exists());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
