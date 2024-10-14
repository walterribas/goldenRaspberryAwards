package com.outsera.util;

import com.outsera.model.Movie;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVReaderUtil {

    public List<Movie> readMoviesFromCSV() {
        List<Movie> movies = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/movielist.csv")))) {

            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                Movie movie = new Movie();
                movie.setReleaseYear(Integer.parseInt(fields[0]));
                movie.setTitle(fields[1]);
                movie.setStudios(fields[2]);
                movie.setProducers(fields[3]);

                movie.setWinner(fields.length > 4 && "yes".equals(fields[4].trim()));

                movies.add(movie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }
}
