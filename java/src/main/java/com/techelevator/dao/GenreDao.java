package com.techelevator.dao;

import com.techelevator.model.GenreCountDTO;
import com.techelevator.model.MovieFromOutsideApiDto;

import java.security.Principal;
import java.util.List;

public interface GenreDao {

    List<Integer> createGenre(MovieFromOutsideApiDto movie, int movieId);

    List<Integer> getGenreIdsByMovieId(int movieId);

    List<GenreCountDTO> getTopGenresForUser(Principal principal);
}
