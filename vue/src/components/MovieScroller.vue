

<template>
  <div id="container">
    <movieCard
      @fadeInRight="fadeInRight()"
      class="card animate__animated"
      v-for="movie in this.movies"
      v-bind:key="movie.id"
      v-bind:movie="movie"
    />
  </div>
</template>

<script>
import movieCard from "../components/MovieCard.vue";
export default {
  props: {
    movies: [],
  },

  components: {
    movieCard,
  },

  methods: {
    fadeInRight() {
      const movieCards = document.getElementsByClassName("card");
      for (let i = 0; i < movieCards.length; i++) {
        movieCards[i].classList.add("animate__flipInX");
      }
      setTimeout(() => {
        for (let i = 0; i < movieCards.length; i++) {
          movieCards[i].classList.remove("animate__flipInX");
        }
      }, 1500);
    },
  },
};
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
  height: 600px;
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