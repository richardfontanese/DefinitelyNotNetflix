
//This needs to be styled and display the information
<template>
  <div class="movie-card animate__animated" ref="movieCard">
    <h2 class="movie-title">{{ movie.title }}</h2>
    <h3 class="movie-year">{{ movie.year }}</h3>
    <img class="movie-poster" :src="movie.poster" v-on:click="showDetails()" />
    <div class="left-button-container">
      <button
        class="button-dislike"
        v-on:click.prevent="hingeAnimation()"
        v-if="!movie.liked"
      >
        <span class="icon">
          <i class="fas fa-thumbs-down"></i>
        </span>
        NO
      </button>
    </div>
    <div class="right-button-container">
      <button
        class="button-dislike"
        v-on:click.prevent="heartbeatAnimation()"
        v-if="!this.liked"
      >
        <span class="icon">
          <i class="fas fa-thumbs-up"></i>
        </span>
        LIKE
      </button>
      
    </div>
  </div>
</template>

<script>
import MovieService from "../services/MovieService.js";

export default {
  name: "moviecard",

  props: {
    movie: Object,
    enableAdd: {
      type: Boolean,
      default: false,
    },
    
  },
   computed: {
    liked() {
      for (let i = 0; i < this.$store.state.likedMovies.length; i++) {
        if (this.$store.state.likedMovies[i].id == this.movie.id) {
          return true;
        }
      }
      return false;
    },
    
  },
  methods: {
    showDetails() {
      this.$router.push({
        path:`/moviedetails/${this.movie.imdbId}`
      });
    },
    setSuggested() {
      MovieService.getSuggestedMovies().then((response) => {
        this.$store.state.suggestedMovies = response.data;
      });
    },
     
    heartbeatAnimation() {
      this.$refs.movieCard.classList.add("animate__bounceIn");
      setTimeout(() => this.setLiked(), 2000);
      //  this.setLiked();
    },

    setLiked() {
      MovieService.setLiked(this.movie.id).then((response) => {
        if (response.status == 201) {
          this.$store.commit("LIKE_MOVIE", this.movie);
          this.setSuggested();
        }
      });
    },
    hingeAnimation() {
      this.$refs.movieCard.classList.add("animate__hinge");
      setTimeout(() => this.setNoLiked(), 2000);
    },
    setNoLiked() {
      MovieService.setNoLiked(this.movie.id).then((response) => {
        if (response.status === 201) {
          this.$store.commit("NOT_LIKE_MOVIE", this.movie);
          this.setSuggested();
          this.$emit('fadeInRight');
        }
      });
    },
    // setNoLiked(){
    // MovieService.setNoLiked(this.movie.id).then((response) => {
    //     if (response.status == 200) {
    //       this.$store.commit("NOT_LIKE_MOVIE", this.movie);
    //     }
    //   });
    // },
  },
};
</script>

<style>
/* .movie-card {
  display: grid;
  border: 2px solid black;
  border-radius: 10px;
  width: 250px;
  height: 55vh;
  margin: 20px;
  overflow-x: scroll;
  overflow-y: hidden;
  background-color: black;
  text-align: center;
    border: 2px solid black;
    border-radius: 10px;
    width: 250px;
    height: 380px;
    margin: 20px;
    position: relative;
    text-align: center;
} */

/* .movie-card.read {
  background-color: rgba(83, 1, 1, 0.904);
  position: relative;
  text-align: center;
  background-color: rgba(0, 0, 0, 0);
} */
/* .movie-title {
  color: red;
  font-size: 15px;
}
.movie-year {
  color: red;
  font-size: 20px;
} */
.movie-poster {
  margin: 0px;
  position: relative; 
   /* border-radius: 5px; */
    /* cursor: pointer;  *
    height: 250px;
  /*  object-fit: fill;*/
    /* width: 100%; */
    /* margin-left: -20px;   */
}
.icon {
  color: rgb(241, 15, 64);
}
.left-button-container{
  text-align: left;
   margin-bottom: 10px;
}
.right-button-container {
  text-align: right;
   margin-top: -40px;
}
.movie-card {
  display: inline-block;
  padding-left: 20px;
  border-radius: 5px;
  width: 200px;
  height: 350px;
  margin: 20px;
  overflow-x: scroll;
  overflow-y: hidden;
  background-color: black;
  text-align: center;
}

.movie-title {
  color: red;
  font-size: 20px;
  width: 175px;
  display: inline-block;
  max-width: 125px;
  word-wrap: break-word;
  }

.movie-year {
  color: red;
  font-size: 20px;
}

.button-container {
  border-radius: auto;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease-in-out;
}

.movie-poster {
    height: 250px;
    object-fit: fill;
    width: 200px;
    margin: 0px;   
}
</style>