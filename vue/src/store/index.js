import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex)

/*
 * The authorization header is set for axios when you login but what happens when you come back or
 * the page is refreshed. When that happens you need to check for the token in local storage and if it
 * exists you should set the header so that it will be attached to each request
 */
const currentToken = localStorage.getItem('token')
const currentUser = JSON.parse(localStorage.getItem('user'));

if (currentToken != null) {
  axios.defaults.headers.common['Authorization'] = `Bearer ${currentToken}`;
}

export default new Vuex.Store({
  state: {
    allMovies: [],
    likedMovies: [],
    suggestedMovies: [],
    notLikedMovie: [],
    token: currentToken || '',
    user: currentUser || {}
  },
  mutations: {
    SET_AUTH_TOKEN(state, token) {
      state.token = token;
      localStorage.setItem('token', token);
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
    },
    SET_USER(state, user) {
      state.user = user;
      localStorage.setItem('user', JSON.stringify(user));
    },
    LOGOUT(state) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      state.token = '';
      state.user = {};
      axios.defaults.headers.common = {};
    },
    LIKE_MOVIE(state, movie) {
      state.likedMovies.push(movie);
      for (let i = 0; i < state.suggestedMovies.length; i++) {
        if (state.suggestedMovies[i].imdbId == movie.imdbId) {
          state.suggestedMovies.splice(i, 1);
          state.allMovies.splice(i, 1);
        }
      }
    },
    NOT_LIKE_MOVIE(state, movie) {
      state.notLikedMovie.push(movie);

      // Compare the disliked movie with all movies in the state
      state.allMovies.forEach(movieInAllMovies => {
        if (movieInAllMovies.imdbId == movie.imdbId) {
          state.allMovies.splice(state.allMovies.indexOf(movieInAllMovies), 1);
        }
      })
      state.likedMovies.forEach(movieInLikedMovies => {
        if (movieInLikedMovies.imdbId == movie.imdbId) {
          state.likedMovies.splice(state.likedMovies.indexOf(movieInLikedMovies), 1);
        }
      })
    },
  }
});
