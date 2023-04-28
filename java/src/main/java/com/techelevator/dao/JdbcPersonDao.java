package com.techelevator.dao;

import com.techelevator.model.MovieFromOutsideApiDto;
import com.techelevator.model.Person;
import com.techelevator.model.PersonCountDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcPersonDao implements PersonDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPersonDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Integer> createDirectors(MovieFromOutsideApiDto movie, int movieId) {
        //empty list to fill and return
        List<Integer> directorIdList = new ArrayList<>();

        //list of director names from the movie passed in
        String[] directorNames = movie.getDirector().split(", ");

        //loop through each director name in the list
        for (String directorName : directorNames) {

            //get a person_id number, based on the persons name
            String sql = "SELECT person_id FROM people WHERE name = ?";
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, directorName);

            //if that person's name is not found in the database
            if (!results.next()) {

                //insert them into the people table, and link them to the movie in the directors table
                String sql1 = "INSERT into people VALUES (default, ? ) RETURNING person_id";
                try {
                    int directorId = jdbcTemplate.queryForObject(sql1, Integer.class, directorName);
                    directorIdList.add(directorId);

                    String sql2 = "INSERT into directors VALUES ( ? , ? )";
                    jdbcTemplate.update(sql2, directorId, movieId);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                //if that person's name is in the database, only insert them into the directors table
            } else {
                int directorId = results.getInt("person_id");
                String sql3 = "INSERT into directors VALUES ( ? , ? )";
                jdbcTemplate.update(sql3, directorId, movieId);
            }
        }
        return directorIdList;
    }


    public List<Integer> createActors(MovieFromOutsideApiDto movie, int movieId) {
        List<Integer> actorIdList = new ArrayList<Integer>();
        String[] actorNames = movie.getActors().split(", ");

        String sql = "SELECT person_id FROM people WHERE name = ?";
        for (String actorName : actorNames) {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, actorName);
            if (!results.next()) {
                String sql1 = "INSERT into people VALUES (default, ? ) RETURNING person_id";
                try {
                    int actorId = (jdbcTemplate.queryForObject(sql1, Integer.class, actorName));
                    actorIdList.add(actorId);

                    String sql2 = "INSERT into actors VALUES ( ? , ? )";
                    jdbcTemplate.update(sql2, actorId, movieId);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else {
                int actorId = results.getInt("person_id");
                String sql3 = "INSERT into actors VALUES ( ? , ? )";
                jdbcTemplate.update(sql3, actorId, movieId);
            }
        }
        return actorIdList;
    }


    public List<Person> getDirectorsByMovie(int movieId) {
        String sql = "SELECT people.person_id, name " +
                "FROM people JOIN directors on people.person_id = directors.person_id " +
                "JOIN movies on directors.movie_id = movies.movie_id " +
                "WHERE movies.movie_id = ?;";
        List<Person> directorsList = new ArrayList<>();
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, movieId);
            while (rowSet.next()) {
                Person person = new Person();
                person.setId(rowSet.getInt("person_id"));
                person.setName(rowSet.getString("name"));
                directorsList.add(person);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return directorsList;
    }


    public List<Person> getActorsByMovie(int movieId) {
        String sql = "SELECT people.person_id, name " +
                "FROM people JOIN actors on people.person_id = actors.person_id " +
                "JOIN movies on actors.movie_id = movies.movie_id " +
                "WHERE movies.movie_id = ?;";
        List<Person> actorsList = new ArrayList<>();
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, movieId);
            while (rowSet.next()) {
                Person person = new Person();
                person.setId(rowSet.getInt("person_id"));
                person.setName(rowSet.getString("name"));
                actorsList.add(person);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return actorsList;
    }

    @Override
    public List<PersonCountDto> getTopActors(int userId) {
        String sql = "select people.name, count(people.name) from people " +
                "join actors on people.person_id = actors.person_id " +
                "join movies on actors.movie_id = movies.movie_id " +
                "join user_movie on movies.movie_id = user_movie.movie_id " +
                "where user_movie.user_id = ? " +
                "group by people.name " +
                "order by count desc;";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);

        List<PersonCountDto> output = new ArrayList<>();
        while (rowSet.next()) {
            output.add(new PersonCountDto(rowSet.getString("name"), rowSet.getInt("count")));
        }
        return output;
    }

    @Override
    public List<PersonCountDto> getTopDirectors(int userId) {
        String sql = "select people.name, count(people.name) from people " +
                "join directors on people.person_id = directors.person_id " +
                "join movies on directors.movie_id = movies.movie_id " +
                "join user_movie on movies.movie_id = user_movie.movie_id " +
                "where user_movie.user_id = ? " +
                "group by people.name " +
                "order by count desc;";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);

        List<PersonCountDto> output = new ArrayList<>();
        while (rowSet.next()) {
            output.add(new PersonCountDto(rowSet.getString("name"), rowSet.getInt("count")));
        }
        return output;
    }

    //Returns a person from our database, searching by ID
    public Person getPersonById(int id) {
        Person person = null;
        String sql = "SELECT person_id, name " +
                "FROM people " +
                "where person_id = " + id;
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        if (results.next()) {
            person = mapRowToPerson(results);
        }
        return person;
    }

    //Helper method
    private Person mapRowToPerson(SqlRowSet rowSet) {
        Person person = new Person();
        person.setId(rowSet.getInt("person_id"));
        person.setName(rowSet.getString("name"));
        return person;
    }
}
