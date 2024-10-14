package com.outsera.service;

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
        loadMoviesFromCSV();
    }

    @Transactional
    public void loadMoviesFromCSV() {
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

        AwardInterval minIntervalProducer = null;
        AwardInterval maxIntervalProducer = null;

        for (Map.Entry<String, List<Movie>> entry : moviesByProducer.entrySet()) {
            String producer = entry.getKey();
            List<Movie> producerMovies = entry.getValue();

            producerMovies.sort(Comparator.comparingInt(Movie::getReleaseYear));

            if (producerMovies.size() > 1) {
                for (int i = 1; i < producerMovies.size(); i++) {
                    int interval = producerMovies.get(i).getReleaseYear() - producerMovies.get(i - 1).getReleaseYear();
                    int previousWin = producerMovies.get(i - 1).getReleaseYear();
                    int followingWin = producerMovies.get(i).getReleaseYear();
                    String previousWinMovie = producerMovies.get(i - 1).getTitle();
                    String followingWinMovie = producerMovies.get(i).getTitle();

                    if (minIntervalProducer == null || interval < minIntervalProducer.getInterval()) {
                        minIntervalProducer = new AwardInterval(producer, interval, previousWin, followingWin, previousWinMovie, followingWinMovie);
                    }

                    if (maxIntervalProducer == null || interval > maxIntervalProducer.getInterval()) {
                        maxIntervalProducer = new AwardInterval(producer, interval, previousWin, followingWin, previousWinMovie, followingWinMovie);
                    }
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("min", minIntervalProducer);
        result.put("max", maxIntervalProducer);

        return result;
    }

    private List<String> extractProducers(String producers) {
        String[] splitProducers = producers.replace(" and ", ",").split(",");

        return Arrays.stream(splitProducers)
                .map(String::trim)
                .collect(Collectors.toList());
    }


    // Classe auxiliar para armazenar os intervalos de prêmios
    public class AwardInterval {
        private String producer;
        private int interval;
        private int previousWin;
        private int followingWin;
        private String previousWinMovie;
        private String followingWinMovie;

        // Construtor atualizado para incluir os novos campos
        public AwardInterval(String producer, int interval, int previousWin, int followingWin, String previousWinMovie, String followingWinMovie) {
            this.producer = producer;
            this.interval = interval;
            this.previousWin = previousWin;
            this.followingWin = followingWin;
            this.previousWinMovie = previousWinMovie;
            this.followingWinMovie = followingWinMovie;
        }

        // Getters e Setters para os novos campos
        public String getPreviousWinMovie() {
            return previousWinMovie;
        }

        public String getFollowingWinMovie() {
            return followingWinMovie;
        }

        // Outros getters e setters
        public String getProducer() {
            return producer;
        }

        public int getInterval() {
            return interval;
        }

        public int getPreviousWin() {
            return previousWin;
        }

        public int getFollowingWin() {
            return followingWin;
        }

        // Outros métodos, se necessário
    }

}
