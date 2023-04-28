<template>
  <div id="mainContainer">
    <div class="browseHeading">Browse our entire catalogue, if you dare...</div>
    <movieBrowser v-bind:movieProp="this.$store.state.allMovies" />
  </div>
</template>

<script>
import movieBrowser from "../components/MovieBrowser.vue";
export default {
  components: {
    movieBrowser,
  },
};
</script>

<style>
.browseHeading {
  position: fixed;
  margin-top: 50px;
  margin-left: 100px;
}
</style>