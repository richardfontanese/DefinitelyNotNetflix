<template>
  <div id="mainContainer">
    <div class="browseHeading">A complete list of your favorites</div>
    <movieBrowser v-bind:movieProp="this.$store.state.likedMovies" />
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