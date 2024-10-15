package com.outsera.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("year")
    private int releaseYear;

    private String title;
    private String studios;
    private String producers;

    @JsonProperty("winner")
    private boolean winner;

    public Movie() {}

    @JsonCreator
    public Movie(
            @JsonProperty("id") Long id,
            @JsonProperty("year") int releaseYear,
            @JsonProperty("title") String title,
            @JsonProperty("studios") String studios,
            @JsonProperty("producers") String producers,
            @JsonProperty("winner") String winnerString
    ) {
        this.id = id;
        this.releaseYear = releaseYear;
        this.title = title;
        this.studios = studios;
        this.producers = producers;
        this.setWinnerFromString(winnerString);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStudios() {
        return studios;
    }

    public void setStudios(String studios) {
        this.studios = studios;
    }

    public String getProducers() {
        return producers;
    }

    public void setProducers(String producers) {
        this.producers = producers;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public void setWinnerFromString(String winnerString) {
        this.winner = "yes".equalsIgnoreCase(winnerString);
    }
}
