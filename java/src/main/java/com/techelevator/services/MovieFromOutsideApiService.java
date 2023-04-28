package com.techelevator.services;

import com.techelevator.model.MovieFromOutsideApiDto;
import org.springframework.web.client.RestTemplate;

public class MovieFromOutsideApiService {

    private static final String API_BASE_URL = "http://www.omdbapi.com/";
    private static final String API_KEY = "&apikey=fadb8457";
    private final RestTemplate restTemplate = new RestTemplate();

    public MovieFromOutsideApiDto getMovieFromOutsideApiByImdbId(String imdbId) {
        MovieFromOutsideApiDto output = null;

        try {
            output = restTemplate.getForObject(API_BASE_URL + "?i=" + imdbId + API_KEY, MovieFromOutsideApiDto.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return output;
    }
}
