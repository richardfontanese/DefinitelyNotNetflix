<template>
  <div id="main">
    <div id="search">
      <h2>Search for a movie to add to our database!</h2>
      <div id="buttonBox">
        <button
          id="previousPageButton"
          v-if="this.page > 1"
          @click="searchPreviousPage(titleToSearch)"
        >
          previous page
        </button>

        <input
          id="searchTerm"
          placeholder="Enter title to search"
          v-model="titleToSearch"
        />

        <button id="searchButton" @click="searchMovie(titleToSearch, 1)">
          Search
        </button>

        <button
          id="nextPageButton"
          v-if="this.page >= 1"
          @click="searchNextPage(titleToSearch)"
        >
          next page
        </button>
      </div>

      <div id="resultsWrapper">
        <div
          class="searchResultDiv"
          v-for="movie in this.searchResult.Search"
          v-bind:key="movie.id"
        >
          {{ movie.Title }}
          <img
            v-bind:src="movie.Poster"
            class="searchImage"
            alt="No poster available."
          />
          <button class="addMovieButton" @click="addMovie(movie.imdbID)">
            Add Movie!
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import movieService from "../services/MovieService.js";
export default {
  name: "adminSearch",
  data() {
    return {
      titleToSearch: "",
      searchResult: {},
      page: null,
    };
  },
  methods: {
    searchMovie(title, page) {
      this.page = 1;
      movieService.searchExternalMovie(title, page).then((response) => {
        this.searchResult = response.data;
      });
    },

    searchNextPage(title) {
      this.page += 1;
      movieService.searchExternalMovie(title, this.page).then((response) => {
        this.searchResult = response.data;
      });
    },

    searchPreviousPage(title) {
      this.page -= 1;
      movieService.searchExternalMovie(title, this.page).then((response) => {
        this.searchResult = response.data;
      });
    },

    addMovie(imdbId) {
      const requestBody = {
        imdbId: imdbId,
      };
      movieService.addMovie(requestBody);
    },
  },
  created() {
    this.page = 0;
  },
};
</script>

<style scoped>
#main {
  display: flex;
  flex-direction: column;
}
#search {
  background-color: rgba(0, 0, 0, 0.685);
  width: 95vw;
  height: 95vh;
  border-radius: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
#buttonBox {
  display: inline-flex;
}
#nextPageButton {
  position: absolute;
  right: 10vw;
}
#previousPageButton {
  position: absolute;
  left: 10vw;
}
h2 {
  color: rgb(223, 212, 196);
}
.searchResultDiv {
  font-size: 20px;
  color: rgb(223, 212, 196);
  width: 17vw;
  height:400px;
  display: flex;
  flex-direction: column;
}
#resultsWrapper {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: center;
  overflow-y: scroll;
}
.searchImage {
  width: 10vw;
  border-radius: 20px;
  display:fixed;
}
.addMovieButton {
  border-radius: 20px;
  background-color: rgba(255, 255, 255, 0.671);
  color: rgb(88, 6, 22);
}
</style>