package com.techelevator.dao;

import com.techelevator.model.Movie;
import com.techelevator.model.MovieFromOutsideApiDto;

import java.security.Principal;
import java.util.List;

public interface MovieDao {

    Movie getMovieByID(int id);

    int createMovie(MovieFromOutsideApiDto movie);

    int createMovieFromExternalApi(String imdbId);

    List<Movie> getLikedMovies(String name);

    boolean addLikedMovie(String name, int movieId);

    List<Movie> getMoviesByDirector(int directorId);

    List<Movie> getMoviesByActor(int actorId);

    List<Movie> getAllMovies();

    List<Movie> getSuggestedMovies(Principal principal);

    List<Movie> getMoviesByGenre(int genreId);

    boolean dislikeMovie(String name, int movieId);

    List<Movie> getDislikedMovies(String name);

    List<Movie> searchMoviesByTitle(String title);

    void deleteMovie(int movieId);
}
