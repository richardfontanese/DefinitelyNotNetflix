package com.techelevator.controller;

import com.techelevator.dao.MovieDao;
import com.techelevator.dao.PersonDao;
import com.techelevator.model.ImdbIdDto;
import com.techelevator.model.Movie;
import com.techelevator.model.MovieFromOutsideApiDto;


import com.techelevator.dao.*;
import com.techelevator.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
public class MovieController {

    private final MovieDao movieDao;
    private final GenreDao genreDao;

    public MovieController(MovieDao movieDao, GenreDao genreDao) {
        this.movieDao = movieDao;
        this.genreDao=genreDao;
    }

    @GetMapping(path="/api/top_genres")
    public List<GenreCountDTO> getTopGenresByUser (Principal principal){
        return genreDao.getTopGenresForUser(principal);
    }

    @GetMapping(path = "/api/movies")
    public List<Movie> getAllMovies() {
        return movieDao.getAllMovies();
    }

    @GetMapping(path = "/api/movies/liked")
    public List<Movie> getLikedMovies(Principal principal) {
        return movieDao.getLikedMovies(principal.getName());
    }

    @ResponseStatus(code = HttpStatus.CREATED, reason = "Successfully added movie to your favorites")
    @PostMapping(path = "/api/movies/add_like/{movieId}")
    public boolean addLikedMovie(Principal principal, @PathVariable int movieId) {
        return movieDao.addLikedMovie(principal.getName(), movieId);
    }

    @GetMapping(path = "/api/movies/{id}")
    public Movie getMovieById(@PathVariable int id) {
        return movieDao.getMovieByID(id);
    }

    @ResponseStatus(code = HttpStatus.CREATED, reason = "Successfully added movie to the database")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/api/movies/create")
    public int createMovie(@RequestBody MovieFromOutsideApiDto movie) {
        return movieDao.createMovie(movie);
    }

    @ResponseStatus(code = HttpStatus.CREATED, reason = "Successfully added movie to the database")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/api/movies/create_from_external")
    public int createMovieFromExternalApi(@RequestBody ImdbIdDto imdbId) {
        int returnedId = movieDao.createMovieFromExternalApi(imdbId.getImdbId());
        if (returnedId == -1) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "unable to add that movie to the database.  " +
                    "the movie already exists, or an error occurred in trying to create it.");
        }
        return returnedId;
    }

    @GetMapping(path = "/api/movies/directors/{directorId}")
    public List<Movie> getMoviesByDirector(@PathVariable int directorId) {
        return movieDao.getMoviesByDirector(directorId);
    }

    @GetMapping(path = "/api/movies/actors/{actorId}")
    public List<Movie> getMoviesByActor(@PathVariable int actorId) {
        return movieDao.getMoviesByActor(actorId);
    }

    @GetMapping(path = "/api/movies/search_title/{title}")
    public List<Movie> searchMoviesByTitle(@PathVariable String title) {
        return movieDao.searchMoviesByTitle(title);
    }

    @GetMapping(path = "/api/movies/suggested")
    public List<Movie> getSuggestedMovies(Principal principal) {
        return movieDao.getSuggestedMovies(principal);
    }


    @ResponseStatus(code = HttpStatus.CREATED, reason = "Successfully added movie to the list of movies that you absolutely cannot stand and never want to hear another word about for the rest of your life.")
    @PostMapping(path = "/api/movies/dislike/{movieId}")
    public boolean dislikeMovie(Principal principal, @PathVariable int movieId) {
        return movieDao.dislikeMovie(principal.getName(), movieId);
    }

    @GetMapping(path = "/api/movies/disliked")
    public List<Movie> getDislikedMovies(Principal principal) {
        return movieDao.getDislikedMovies(principal.getName());
    }

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/api/movies/delete/{movieId}")
    public void deleteMovie(@PathVariable int movieId) {
        movieDao.deleteMovie(movieId);
    }
}
