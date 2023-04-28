<template>
  <div class="search-container">
      <input
        id="search-movie"
        placeholder="Enter Movie Title"
        v-model="titleToSearchMovie"
        list="movie-titles"
      />

      <div class="scroller-container">
      <h2 class="scroller-heading"> Movies You Search</h2>
      <movieScroller v-bind:movies="this.$store.state.allMovies" />
      <!-- <p>
        We take pride in our collection of only the foulest, most terrifying
        pieces of cinema ever created. Do you think you have the guts to sit
        through one of our films?
         <router-link class="link" v-bind:to="{ name: 'all-movies' }"
          >See More</router-link
        > 
      </p> -->
    </div>
    </div>
</template>

<script>
import movieScroller from "../components/MovieScroller.vue"; 
export default {
    components: {
    movieScroller,
  },

  data() {
    return {
        titleToSearchMovie: "",
     }
  },

  created() {
      this.searchMovie();
  },

  methods: {
    searchMovie() {
      const allMovies = this.$store.state.allMovies;
      const titleToSearchMovie = this.$route.params.term.toLowerCase();

      if (!titleToSearchMovie) {
        this.movieService.getAllMovies().then((response) => {
          this.$store.state.allMovies = response.data;
        });
      } else {
        const filteredMovies = allMovies.filter((movie) =>
          movie.title.toLowerCase().includes(titleToSearchMovie)
        );
        this.$store.state.allMovies = filteredMovies;
      }
    },

    clearSearch() {
      this.$router.push('/');
    },
  },

}
</script>

<style>
#container {
  background-color: rgba(0, 0, 0, 0.685);
  border-radius: 20px;
  margin: 10px;
  padding: 20px;
  overflow-x: scroll;
  overflow-y: hidden;
  width: 95vw;
  height: 580px;
  white-space: nowrap;
}

::-webkit-scrollbar {
  height: 8px;
}

::-webkit-scrollbar-thumb {
  background-color: rgb(49, 49, 49);
  outline: 1px solid rgb(65, 4, 73);
  border-radius: 5px;
}

</style>
