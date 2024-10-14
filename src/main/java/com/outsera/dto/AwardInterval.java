package com.outsera.dto;

public record AwardInterval(
        String producer,
        int interval,
        int firstWinData,
        String firstWinMovie,
        int lastWinData,
        String lastWinMovie) {
}