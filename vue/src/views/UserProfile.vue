<template>
  <div>
    <div id="headspace"></div>
    <div id="info">
      <div id="favorites-title"> Here is a list of {{ username }} 's Top 5 Favorite</div>
      <div class="favorites-list">
        <h2>Actors:</h2>
        <div v-for="actor in this.topActors.slice(0, 5)" v-bind:key="actor.id">
          <p>
            {{ actor.name }}
          </p>
        </div>
      </div>

      <div class="favorites-list">
        <h2>Directors:</h2>
        <div v-for="director in this.topDirectors.slice(0, 5)" v-bind:key="director.id">
          <p>
            {{ director.name }}
          </p>
        </div>
      </div>

      <div class="favorites-list">
        <h2>Genres:</h2>
        <div v-for="genre in this.topGenres.slice(0, 5)" v-bind:key="genre.id">
          <p>
            {{ genre.genre_name }}
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import movieService from "../services/MovieService.js";
export default {
  name: "userprofile",
  data() {
    return {
      topActors: {},
      topDirectors: {},
      topGenres: {},
    };
  },
  methods: {},
  created() {
    movieService.getTopActors().then((response) => {
      this.topActors = response.data;
    });
    movieService.getTopDirectors().then((response) => {
      this.topDirectors = response.data;
    });
    movieService.getTopGenres().then((response) => {
      this.topGenres = response.data;
    });
  },
  computed: {
      username() {
      return this.$store.state.user.username;
    },
  }
};
</script>

<style>
#headspace {
  margin-top: 100px;
}
.favorites-list {
  display: inline-block;
}
p {
  color: white;
  font-size: 30px;
  padding-left: 10px;
  padding-right: 15px;
 
}
#favorites-title{
  color: red;
  font-family:  'Segoe UI';
  font-size: 40px;
  justify-content: center;
  position: sticky;
}
h2{
  color: lime;

}
</style>