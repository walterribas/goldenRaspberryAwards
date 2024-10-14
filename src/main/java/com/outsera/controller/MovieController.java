package com.outsera.controller;

import com.outsera.model.Movie;
import com.outsera.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/producers-interval")
    public Map<String, Object> getProducersInterval() {
        return movieService.getProducersWithAwardsInterval();
    }

    @GetMapping("/movies")
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/winners")
    public List<Movie> findWinners() {
        return movieService.findWinners();
    }

}
