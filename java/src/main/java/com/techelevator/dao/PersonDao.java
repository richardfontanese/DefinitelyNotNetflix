package com.techelevator.dao;

import com.techelevator.model.MovieFromOutsideApiDto;
import com.techelevator.model.Person;
import com.techelevator.model.PersonCountDto;

import java.util.List;

public interface PersonDao {

    Person getPersonById(int id);

    List<Integer> createDirectors(MovieFromOutsideApiDto movie, int id);

    List<Integer> createActors(MovieFromOutsideApiDto movie, int id);

    public List<Person> getDirectorsByMovie(int movieId);

    public List<Person> getActorsByMovie(int movieId);

    List<PersonCountDto> getTopDirectors(int userId);

    List<PersonCountDto> getTopActors(int userId);
}
