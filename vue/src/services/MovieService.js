import axios from 'axios';

const movieAPI = axios.create();

export default {
    searchExternalMovieImdbId(imdbId) {
        delete movieAPI.defaults.headers.common["Authorization"];
        return movieAPI.get(`http://www.omdbapi.com/?i=${imdbId}&type=movie&apikey=fadb8457`);
    },
    searchExternalMovie(title, page) {
        delete movieAPI.defaults.headers.common["Authorization"];

        return movieAPI.get(`http://www.omdbapi.com/?s=${title}&type=movie&apikey=fadb8457&page=${page}`);
    },

    getTopActors() {
        return axios.get("/api/people/top_actors");
    },

    getTopDirectors() {
        return axios.get("/api/people/top_directors");
    },

    getTopGenres() {
        return axios.get("/api/top_genres");
    },

    getAllMovies() {
        return axios.get("/api/movies");
    },

    getLikedMovies() {
        return axios.get("/api/movies/liked");
    },

    getDislikedMovies() {
        return axios.get("/api/movies/disliked");
    },

    getSuggestedMovies() {
        return axios.get("/api/movies/suggested");
    },

    setLiked(movieId) {
        return axios.post(`/api/movies/add_like/${movieId}`);
    },
    setNoLiked(movieId) {
        return axios.post(`/api/movies/dislike/${movieId}`);
    },
    addMovie(requestBody) {
        return axios.post(`/api/movies/create_from_external/`, requestBody);
    }
}