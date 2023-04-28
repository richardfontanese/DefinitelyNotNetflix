
<template>
  <div class="home">
    
    <div class="scroller-container">
      <h2 class="scroller-heading">Our collection...</h2>
      <movieScroller v-bind:movies="this.$store.state.allMovies.slice(0,10)" />
      <p>
        We take pride in our collection of only the foulest, most terrifying
        pieces of cinema ever created. Do you think you have the guts to sit
        through one of our films?
        <router-link class="link" v-bind:to="{ name: 'allmovies' }"
          >See More</router-link
        >
      </p>
    </div>

    <div class="scroller-container">
      <h2 class="scroller-heading">Top picks, based on your history</h2>
      <movieScroller
        v-bind:movies="this.$store.state.suggestedMovies.slice(0,10)"
      />
      <p>
        A personalized list of some of our most terrifying and disturbing films,
        curated just for you by the ghouls and goblins in our lair.
        <router-link class="link" v-bind:to="{ name: 'toppicks' }"
          >See More</router-link
        >
      </p>
    </div>

    <div class="scroller-container">
      <h2 class="scroller-heading">
        Movies that you find devilishly delightful
      </h2>
      <movieScroller v-bind:movies="this.$store.state.likedMovies.slice(0,10)" />
      <p>
        Your taste in the sickening and maccabre is impeccable...
        <router-link class="link" v-bind:to="{ name: 'likedmovies' }"
          >See More</router-link
        >
      </p>
    </div>
    <div id="bottom-space">.</div>
  </div>
</template> 

<script>
import movieScroller from "../components/MovieScroller.vue";
import movieService from "../services/MovieService.js";
export default {
  name: "home",

  data() {
    return {
      allMovies: [],
      likedMovies: {},
      suggestedMovies: {},
      // titleToSearchMovie: "",
      removeMovie: {},
    };
  },

  components: {
    movieScroller,
  },

  created() {
    movieService.getAllMovies().then((response) => {
      this.$store.state.allMovies = response.data;
    });

    movieService.getLikedMovies().then((response) => {
      this.$store.state.likedMovies = response.data;
    });

    movieService.getSuggestedMovies().then((response) => {
      this.$store.state.suggestedMovies = response.data;
    });

    movieService.getDislikedMovies().then((response) => {
      this.$store.state.notLikedMovie = response.data;

      //call a mutation in the store that compares the disliked movies to all movies, and removes them form all movies
    });
  },
};
</script>

<style>
.home > h2 {
  text-align: center; /*center content (image and text) within flex-item */
  font-size: x-large;
  color: rgba(241, 13, 13, 0.904);
  border-radius: 0px;
  border-top-left-radius: 6px;
  border-top-right-radius: 6px;
  margin: 0px;
}

#bottom-space {
  width: 100vw;
  height: 10vh;
}
</style>