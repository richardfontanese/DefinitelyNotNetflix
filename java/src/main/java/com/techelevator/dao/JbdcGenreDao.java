package com.techelevator.dao;

import com.techelevator.model.GenreCountDTO;
import com.techelevator.model.MovieFromOutsideApiDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JbdcGenreDao implements GenreDao {

    private final JdbcTemplate jdbcTemplate;
    private final JdbcUserDao userDao;

    public JbdcGenreDao(JdbcTemplate jdbcTemplate, JdbcUserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
    }

    public List<Integer> createGenre(MovieFromOutsideApiDto movie, int movieId) {

        List<Integer> genreIdList = new ArrayList<>();

        String[] genreNames = movie.getGenre().split(", ");

        String sql = "SELECT genre_id FROM genres WHERE genre_name = ?";
        for (String genreName : genreNames) {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, genreName);
            if (!results.next()) {
                String sql1 = "INSERT into genres VALUES (default, ? ) RETURNING genre_id";
                try {
                    int genreId = jdbcTemplate.queryForObject(sql1, Integer.class, genreName);
                    genreIdList.add(genreId);

                    String sql2 = "INSERT into genre_movie VALUES ( ? , ? )";
                    jdbcTemplate.update(sql2, genreId, movieId);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else {
                int genreId = results.getInt("genre_id");
                String sql3 = "INSERT into genre_movie VALUES ( ? , ? )";
                jdbcTemplate.update(sql3, genreId, movieId);
            }
        }
        return genreIdList;
    }

    @Override
    public List<Integer> getGenreIdsByMovieId(int movieId) {
        List<Integer> output = new ArrayList<>();

        // This is commented out because I used a different string, so that the genre 'Horror' will not be included,
        // since every movie we are including is a horror movie
//        String sql = "SELECT * FROM genres " +
//                "JOIN genre_movie on genres.genre_id = genre_movie.genre_id " +
//                "WHERE movie_id = ? ;";

        String sql = "SELECT * FROM genres " +
                "JOIN genre_movie on genres.genre_id = genre_movie.genre_id " +
                "WHERE movie_id = ? and genre_name != 'Horror';";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, movieId);
            while (rowSet.next()) {
                output.add(rowSet.getInt("genre_id"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return output;
    }

    public List<GenreCountDTO> getTopGenresForUser(Principal principal) {

        String sql = "SELECT genre_name, count (genre_name) as count from genres\n" +
                "join genre_movie on genres.genre_id = genre_movie.genre_id\n" +
                "join user_movie on genre_movie.movie_id = user_movie.movie_id\n" +
                "join users on user_movie.user_id = users.user_id\n" +
                "where users.user_id = ?\n" +
                "group by genre_name\n" +
                "order by count desc;";

        int userId = userDao.findIdByUsername(principal.getName());

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);

        List<GenreCountDTO> genreList = new ArrayList<>();
        while (rowSet.next()) {
            genreList.add(new GenreCountDTO(rowSet.getString("genre_name"), rowSet.getInt("count")));
        }

        return genreList;
    }
}
