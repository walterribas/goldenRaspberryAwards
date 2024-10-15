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
                //Verifica se a linha tem o número correto de campos obrigatórios
                if (fields.length < 4) {
                    throw new IllegalArgumentException("Campos obrigatórios não podem ser nulos ou vazios. Linha: " + line);
                }
                //Verifica se os campos obrigatórios estão nulos ou vazios exceto o campo 'winner'
                for (int i = 0; i < 4; i++) {
                    if (isNullOrEmpty(fields[i])) {
                        throw new IllegalArgumentException("Campos obrigatórios não podem ser nulos ou vazios. Linha: " + line);
                    }
                }
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
            throw new RuntimeException("Erro ao ler o arquivo CSV.", e);
        }
        return movies;
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
