package com.techelevator.dao;

import com.techelevator.model.Movie;
import com.techelevator.model.MovieFromOutsideApiDto;
import com.techelevator.model.Person;
import com.techelevator.services.MovieFromOutsideApiService;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.*;

@Component
public class JdbcMovieDao implements MovieDao {

    private final JdbcTemplate jdbcTemplate;
    private final PersonDao personDao;
    private final GenreDao genreDao;

    public JdbcMovieDao(JdbcTemplate jdbcTemplate, PersonDao personDao, GenreDao genreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.personDao = personDao;
        this.genreDao = genreDao;
    }

    //doesn't actually get ALL movies anymore.  i changed it to get all movies except the ones that the user gave a thumbs down to.
    public List<Movie> getAllMovies() {
        List<Movie> allMovies = new ArrayList<>();
        String sql = "SELECT * FROM movies where movie_id not in (select movie_id from user_dislike_movie join users on user_dislike_movie.user_id = users.user_id);";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
            while (rowSet.next()) {
                Movie movie = mapRowToMovie(rowSet);
                allMovies.add(movie);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return allMovies;
    }

    public int createMovieFromExternalApi(String imdbId) {
        MovieFromOutsideApiService movieFromOutsideApiService = new MovieFromOutsideApiService();
        MovieFromOutsideApiDto newMovie = movieFromOutsideApiService.getMovieFromOutsideApiByImdbId(imdbId);

        return createMovie(newMovie);
    }

    public List<Movie> getLikedMovies(String name) {

        List<Movie> returnedList = new ArrayList<>();

        String getUserIdSql = "SELECT user_id FROM users WHERE username = ? ;";

        String getMoviesSql =
                "SELECT movies.movie_id, imdb_id, title, year, plot, poster, rated FROM movies JOIN user_movie  ON movies.movie_id = user_movie.movie_id WHERE user_movie.user_id = ?";
        try {
            int userId = jdbcTemplate.queryForObject(getUserIdSql, Integer.class, name);
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(getMoviesSql, userId);
            while (rowSet.next()) {
                Movie movie = mapRowToMovie(rowSet);
                returnedList.add(movie);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return returnedList;
    }

    public boolean addLikedMovie(String name, int movieId) {

        String getUserIdSql = "SELECT user_id FROM users WHERE username = ? ;";
        String insertMovieSql = "INSERT into user_movie values (?, ?) ;";
        String checkIfPresentSql = "SELECT * FROM user_movie WHERE movie_id = ? AND user_id = ?;";
        String deleteFromDislikedSql = "DELETE FROM user_dislike_movie WHERE user_id = ? AND movie_id = ?;";

        try {
            int userId = jdbcTemplate.queryForObject(getUserIdSql, Integer.class, name);
            SqlRowSet checkIfPresentRowSet = jdbcTemplate.queryForRowSet(checkIfPresentSql, movieId, userId);
            if (!checkIfPresentRowSet.next()) {
                jdbcTemplate.update(insertMovieSql, userId, movieId);
                jdbcTemplate.update(deleteFromDislikedSql, userId, movieId);
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to add movie to your liked movie list, " +
                "probably because you've already liked it, ya dingus.");
    }

    //Takes the data from the OMDB API and picks out what we need, then inserts a new row in the Movies table
    public int createMovie(MovieFromOutsideApiDto movie) {

        String sql = "SELECT * FROM movies WHERE imdb_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, movie.getImdbId());
        int returnedId = -1;

        if (!results.next()) {
            sql = "INSERT into Movies " +
                    "VALUES (default, ?, ?, ?, ?, ?, ?) " +
                    "RETURNING movie_id;";

            try {
                returnedId = jdbcTemplate.queryForObject(sql, Integer.class, movie.getImdbId(), movie.getTitle(), movie.getYear(), movie.getPlot(), movie.getPoster(), movie.getRated());

                personDao.createDirectors(movie, returnedId);
                personDao.createActors(movie, returnedId);

                genreDao.createGenre(movie, returnedId);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return returnedId;
    }

    public List<Movie> getMoviesByDirector(int directorId) {
        List<Movie> result = new ArrayList<>();
        String sql = "select * from movies join directors on movies.movie_id = directors.movie_id where person_id = ? ;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, directorId);
            while (rowSet.next()) {
                result.add(mapRowToMovie(rowSet));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public List<Movie> getMoviesByActor(int actorId) {
        List<Movie> result = new ArrayList<>();
        String sql = "select * from movies join actors on movies.movie_id = actors.movie_id where person_id = ? ;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, actorId);
            while (rowSet.next()) {
                result.add(mapRowToMovie(rowSet));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    //Selects a movie from our database, based on its id
    public Movie getMovieByID(int id) {
        Movie movie = null;

        String sql = "SELECT * FROM movies WHERE movie_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                movie = mapRowToMovie(results);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return movie;
    }

    public List<Movie> getMoviesByGenre(int genreId) {
        List<Movie> output = new ArrayList<>();
        String sql = "SELECT * FROM movies join genre_movie " +
                "on movies.movie_id = genre_movie.movie_id " +
                "WHERE genre_movie.genre_id = ? ;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, genreId);
            while (rowSet.next()) {
                Movie movie = mapRowToMovie(rowSet);
                output.add(movie);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return output;
    }

    @Override
    public boolean dislikeMovie(String name, int movieId) {

        String getUserIdSql = "SELECT user_id FROM users WHERE username = ? ;";
        String insertMovieSql = "INSERT into user_dislike_movie values (?, ?) ;";
        String checkIfPresentSql = "SELECT * FROM user_dislike_movie WHERE movie_id = ? AND user_id = ?;";
        String deleteFromLikedSql = "DELETE FROM user_movie WHERE user_id = ? AND movie_id = ?;";

        try {
            int userId = jdbcTemplate.queryForObject(getUserIdSql, Integer.class, name);
            SqlRowSet checkIfPresentRowSet = jdbcTemplate.queryForRowSet(checkIfPresentSql, movieId, userId);
            if (!checkIfPresentRowSet.next()) {
                jdbcTemplate.update(insertMovieSql, userId, movieId);
                jdbcTemplate.update(deleteFromLikedSql, userId, movieId);
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to add movie to your disliked movie list, " +
                "probably because you've already disliked it, ya dingus.");

    }

    @Override
    public List<Movie> getDislikedMovies(String name) {
        List<Movie> output = new ArrayList<>();
        String sql = "select * from movies join user_dislike_movie\n" +
                "on movies.movie_id = user_dislike_movie.movie_id\n" +
                "join users on user_dislike_movie.user_id = users.user_id\n" +
                "where username = ? ;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, name);
            while (rowSet.next()) {
                output.add(mapRowToMovie(rowSet));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return output;
    }


    //holy crap this is long and complicated.  might want to do something about that.
    @Override
    public List<Movie> searchMoviesByTitle(String title) {
        List<Movie> output = new ArrayList<>();
        title = title.replace('+', ' ');
        title = title.replace("%20", " ");
        String sql = "SELECT * FROM movies WHERE title ILIKE '%" + title + "%';";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
            while (rowSet.next()) {
                output.add(mapRowToMovie(rowSet));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return output;
    }


    //holy crap.  this is really long, lol.  Whoops!
    public List<Movie> getSuggestedMovies(Principal principal) {

        HashMap<Integer, Integer> movieHeatMap = new HashMap<>();
        HashMap<Integer, Integer> personHeatMap = new HashMap<>();
        HashMap<Integer, Integer> genreHeatMap = new HashMap<>();

        //get a list of liked movie IDs
        List<Movie> likedMovies = getLikedMovies(principal.getName());
        List<Integer> likedMovieIds = new ArrayList<>();
        for (Movie movie : likedMovies) {
            likedMovieIds.add(movie.getId());
        }

        //add people to the personHeatMap
        List<Person> peopleInEachMovie = new ArrayList<>();
        for (Movie movie : likedMovies) {
            peopleInEachMovie = personDao.getDirectorsByMovie(movie.getId());
            for (Person person : peopleInEachMovie) {
                if (!personHeatMap.containsKey(person.getId())) {
                    personHeatMap.put(person.getId(), 1);
                } else {
                    personHeatMap.put(person.getId(), personHeatMap.get(person.getId()) + 1);
                }
            }
            peopleInEachMovie = personDao.getActorsByMovie(movie.getId());
            for (Person person : peopleInEachMovie) {
                if (!personHeatMap.containsKey(person.getId())) {
                    personHeatMap.put(person.getId(), 1);
                } else {
                    personHeatMap.put(person.getId(), personHeatMap.get(person.getId()) + 1);
                }
            }
        }

        //loop through each person, find other movies they are associated with, and add to the movieHeatMap
        for (int personId : personHeatMap.keySet()) {
            for (Movie movie : getMoviesByDirector(personId)) {
                if (!likedMovieIds.contains(movie.getId())) {
                    if (movieHeatMap.containsKey(movie.getId())) {
                        movieHeatMap.put(movie.getId(), movieHeatMap.get(movie.getId()) + personHeatMap.get(personId));
                    } else {
                        movieHeatMap.put(movie.getId(), personHeatMap.get(personId));
                    }
                }
            }
            for (Movie movie : getMoviesByActor(personId)) {
                if (!likedMovieIds.contains(movie.getId())) {
                    if (movieHeatMap.containsKey(movie.getId())) {
                        movieHeatMap.put(movie.getId(), movieHeatMap.get(movie.getId()) + personHeatMap.get(personId));
                    } else {
                        movieHeatMap.put(movie.getId(), personHeatMap.get(personId));
                    }
                }
            }
        }


        //add genre Ids to the genreHeatMap
        for (Movie movie : likedMovies) {
            List<Integer> genresByMovie = (genreDao.getGenreIdsByMovieId(movie.getId()));
            for (int genreId : genresByMovie) {
                if (!genreHeatMap.keySet().contains(genreId)) {
                    genreHeatMap.put(genreId, 1);
                } else {
                    genreHeatMap.put(genreId, genreHeatMap.get(genreId) + 1);
                }
            }
        }

        //add movies with matching genres to the movieHeatMap
        for (int genreId : genreHeatMap.keySet()) {
            for (Movie movie : getMoviesByGenre(genreId)) {
                if (!likedMovieIds.contains(movie.getId())) {
                    if (movieHeatMap.containsKey(movie.getId())) {
                        movieHeatMap.put(movie.getId(), movieHeatMap.get(movie.getId()) + genreHeatMap.get(genreId));
                    } else {
                        movieHeatMap.put(movie.getId(), genreHeatMap.get(genreId));
                    }
                }
            }
        }

        //make a list of IDs, sorted by the values in the heatMap
        List<Integer> sortedMovieIds = new ArrayList<>();
        sortedMovieIds.add(-1);
        for (int key : movieHeatMap.keySet()) {
            for (int i = 0; i < movieHeatMap.keySet().size(); i++) {
                if (movieHeatMap.get(key) > sortedMovieIds.get(i)) {
                    sortedMovieIds.add(i, key);
                    break;
                }
            }
        }

        //remove disliked movies from the suggestions
        List<Movie> dislikedList = getDislikedMovies(principal.getName());
        for (int i = 0; i < sortedMovieIds.size(); i++) {
            for (Movie dislikedMovie : dislikedList) {
                if(sortedMovieIds.get(i)==dislikedMovie.getId()){
                    sortedMovieIds.remove(i);
                }
            }
        }

        //remove the last value in the sorted list.  trust me, it makes sense.
        sortedMovieIds.remove(sortedMovieIds.size() - 1);

        //make a list of Movies to return
        List<Movie> suggestedMovies = new ArrayList<>();
        for (int movieId : sortedMovieIds) {
            suggestedMovies.add(getMovieByID(movieId));
        }
        return suggestedMovies;
    }

    public void deleteMovie(int movieId) {
        String sql = "DELETE FROM actors WHERE movie_id = " + movieId + ";\n" +
                "DELETE FROM directors WHERE movie_id = " + movieId + ";\n" +
                "DELETE FROM genre_movie where movie_id = " + movieId + ";\n" +
                "DELETE FROM user_movie WHERE movie_id = " + movieId + ";\n" +
                "DELETE FROM user_dislike_movie WHERE movie_id = " + movieId + ";\n" +
                "DELETE FROM movies WHERE movie_id = " + movieId + ";";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Helper method
    private Movie mapRowToMovie(SqlRowSet rowSet) {
        Movie movie = new Movie();
        movie.setId(rowSet.getInt("movie_id"));
        movie.setImdbId(rowSet.getString("imdb_id"));
        movie.setTitle(rowSet.getString("title"));
        movie.setYear(rowSet.getInt("year"));
        movie.setPoster(rowSet.getString("poster"));
        return movie;
    }
}
