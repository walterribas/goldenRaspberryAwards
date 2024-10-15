package com.outsera.service;

import com.outsera.dto.AwardInterval;
import com.outsera.model.Movie;
import com.outsera.repository.MovieRepository;
import com.outsera.util.CSVReaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CSVReaderUtil csvReaderUtil;

    @PostConstruct
    public void init() {
        loadAndSaveMoviesFromCSV();
    }

    @Transactional
    public List<Movie> loadMoviesFromCSV() {
       return csvReaderUtil.readMoviesFromCSV();
    }

    @Transactional
    public void loadAndSaveMoviesFromCSV() {
        List<Movie> movies = csvReaderUtil.readMoviesFromCSV();
        movieRepository.saveAll(movies);
    }

    @Transactional
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Transactional
    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    @Transactional
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Transactional
    public List<Movie> saveAllMovies(List<Movie> movies) {
        return movieRepository.saveAll(movies);
    }

    @Transactional
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    @Transactional
    public List<Movie> findWinners() {
        return movieRepository.findByWinner(true);
    }

    public Map<String, Object> getProducersWithAwardsInterval() {
        List<Movie> winners = movieRepository.findByWinner(true);

        Map<String, List<Movie>> moviesByProducer = winners.stream()
                .flatMap(movie -> extractProducers(movie.getProducers()).stream()
                        .map(producer -> new AbstractMap.SimpleEntry<>(producer, movie)))
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        List<AwardInterval> minIntervalProducers = new ArrayList<>();
        List<AwardInterval> maxIntervalProducers = new ArrayList<>();
        Integer minInterval = null;
        Integer maxInterval = null;

        for (Map.Entry<String, List<Movie>> entry : moviesByProducer.entrySet()) {
            String producer = entry.getKey();
            List<Movie> producerMovies = entry.getValue();

            producerMovies.sort(Comparator.comparingInt(Movie::getReleaseYear));

            if (producerMovies.size() > 1) {
                for (int i = 1; i < producerMovies.size(); i++) {
                    int interval = producerMovies.get(i).getReleaseYear() - producerMovies.get(i - 1).getReleaseYear();
                    int firstWinDate = producerMovies.get(i - 1).getReleaseYear();
                    String firstWinMovie = producerMovies.get(i - 1).getTitle();
                    int lastWinDate = producerMovies.get(i).getReleaseYear();
                    String lastWinMovie = producerMovies.get(i).getTitle();

                    AwardInterval currentInterval = new AwardInterval(producer, interval, firstWinDate, firstWinMovie, lastWinDate, lastWinMovie);

                    if (minInterval == null || interval < minInterval) {
                        minInterval = interval;
                        minIntervalProducers.clear();
                        minIntervalProducers.add(currentInterval);
                    } else if (interval == minInterval) {
                        minIntervalProducers.add(currentInterval);
                    }

                    if (maxInterval == null || interval > maxInterval) {
                        maxInterval = interval;
                        maxIntervalProducers.clear();
                        maxIntervalProducers.add(currentInterval);
                    } else if (interval == maxInterval) {
                        maxIntervalProducers.add(currentInterval);
                    }
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("min", minIntervalProducers);
        result.put("max", maxIntervalProducers);

        return result;
    }

    private List<String> extractProducers(String producers) {
        String[] splitProducers = producers.replace(" and ", ",").split(",");

        return Arrays.stream(splitProducers)
                .map(String::trim)
                .collect(Collectors.toList());
    }

}
