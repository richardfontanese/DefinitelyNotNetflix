<template>
  <div>
    <img :src="this.movie.Poster" />
    <p>
      {{ this.movie.Title }}<br>
      {{ this.movie.Year }}<br />
      {{ this.movie.Plot }}<br />
      Starring: {{ this.movie.Actors }}<br />
      Directed by: {{ this.movie.Director }}<br />
      Genres: {{ this.movie.Genre }}<br />
      Rated {{ this.movie.Rated }}<br />
      Runtime {{ this.movie.Runtime }}<br />
    </p>
    <div v-for="rating in this.movie.Ratings" v-bind:key="rating">
      <p>Rating: {{ rating.Value }}, Source: {{ rating.Source }}</p>
    </div>
  </div>
</template>

<script>
import movieService from "../services/MovieService.js";
export default {
  name: "moviedetails",
  data() {
    return {
      movie: {},
    };
  },
  methods: {},
  created() {
    movieService
      .searchExternalMovieImdbId(this.$route.params.imdbId)
      .then((response) => {
        this.movie = response.data;
      });
  },
};
</script>

<style>
#headspace {
  margin-top: 100px;
}
p {
  color: white;
  font-size: 30px;
  justify-content: center;
  align-items: center;
}
p.rating{
  color: red;
}
img{
  justify-content: center;
  position: fixed;
}
</style>