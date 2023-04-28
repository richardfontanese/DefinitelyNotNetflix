package com.techelevator.controller;

import com.techelevator.dao.PersonDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.Person;
import com.techelevator.model.PersonCountDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
public class PersonController {

    private PersonDao personDao;
    private UserDao userDao;

    public PersonController(PersonDao personDao,UserDao userDao) {
        this.personDao = personDao;
        this.userDao=userDao;
    }

    @GetMapping(path="/api/people/top_actors")
    public List<PersonCountDto> getTopActors(Principal principal){
        int id = userDao.findIdByUsername(principal.getName());
        return personDao.getTopActors(id);
    }

    @GetMapping(path="/api/people/top_directors")
    public List<PersonCountDto> getTopDirectors(Principal principal){
        int id = userDao.findIdByUsername(principal.getName());
        return personDao.getTopDirectors(id);
    }

    @GetMapping(path = "/api/people/{id}")
    public Person getPersonById(@PathVariable int id) {
        return personDao.getPersonById(id);
    }

    @GetMapping(path = "/api/directors/{movieId}")
    public List<Person> getDirectorsByMovie(@PathVariable int movieId){
        return personDao.getDirectorsByMovie(movieId);
    }

    @GetMapping(path = "/api/actors/{movieId}")
    public List<Person> getActorsByMovie(@PathVariable int movieId){
        return personDao.getActorsByMovie(movieId);
    }
}
