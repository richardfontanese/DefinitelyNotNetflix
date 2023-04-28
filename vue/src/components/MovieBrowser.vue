<template>
  <div id="outer">
    <!-- <div id="sorting">
      <p>Sort Results:
        <button class="sortButton" @click="sortByTitle()">By Title</button>
      </p>
    </div> -->
    <div id="grid">
      <div
        class="individualMovie"
        v-on:click="showDetails(movie)"
        v-for="movie in this.movieProp"
        v-bind:key="movie.id"
      >
        <p>
          {{ movie.title }}
        </p>
        <img
          v-bind:src="movie.poster"
          class="posterImage"
          alt="No poster available."
        />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "movieBrowser",
  props: {
    movieProp: [],
  },
  data(){
    return{
      movies:[],
      ascending:true,
    }
  },
  methods: {
    showDetails(movie) {
      this.$router.push({
        path: `/moviedetails/${movie.imdbId}`,
      });
    },
    sortByTitle(){
      
    }
  },
  created(){
    // console.log(this.$route);
  }
};
</script>

<style>
#outer {
  padding-top: 230px;
}
#sorting{
  position:fixed;
  margin-left:100px;
  margin-top: -50px;
  background-color: rgba(0, 0, 0, 0.829);
  border-radius:10px;
}
#grid {
  display: grid;
  grid-template-columns: repeat(5, 20vw);
  align-items: center;
  justify-content: center;
}
.individualMovie {
  margin-top: 30px;
  margin-bottom: 30px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.sortButton{
  border-radius:5px;
}
</style>