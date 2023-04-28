<template>
  <div id="mainContainer">
    <div class="browseHeading">These spine-tingling titles are sure to please...</div>
    <movieBrowser v-bind:movieProp="this.$store.state.suggestedMovies" />
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