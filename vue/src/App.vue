<template>
  <div id="app">
    <div id="nav" v-if="isLoginOrRegisterPage">
      <div class="nav-left">
        <router-link v-bind:to="{ name: 'home' }">Home</router-link>
      </div>
      <div class="nav-middle">
        <h1 class="welcome">Welcome, {{ username }}</h1>
      </div>
      <div class="search-container">
      <input
        id="search-movie"
        placeholder="Enter Movie Title"
        v-model="titleToSearchMovie"
        list="movie-titles"
      />
      <button
        class="clear-search"
        v-if="titleToSearchMovie"
        @click="clearSearch"
      >
        &#x2716;
      </button>
      <button
        id="searchButton"
        class="scared"
        @click="navigateToSearchMovie()"
      >
        Search
      </button>
    </div>
      <div class="nav-right">
        <router-link v-bind:to="{ name: 'userprofile' }">
          My Profile
        </router-link>
        <p>-----</p>
        <router-link
          v-bind:to="{ name: 'logout' }"
          v-if="$store.state.token != ''"
          >Logout</router-link
        >
      </div>
    </div>
    <div
      id="mainContent"
      v-bind:class="{
        marginNav: isLoginOrRegisterPage,
        verticallyCentered: !isLoginOrRegisterPage,
      }"
    >
      <router-view />
    </div>
    <div id="footer">
      The universe is a strange and terrifying place, indeed.
    </div>
  </div>
</template>

<script>
import "animate.css";
export default {

  data() {
    return {
        titleToSearchMovie: "",

     }
  },

  methods: {
    navigateToSearchMovie() {
      this.$router.push({ name: 'usersearch', params: { term: this.titleToSearchMovie } })
    },
    
    clearSearch() {
      this.titleToSearchMovie = "";
      this.$router.push({ name: 'home' });
    },
   },


  computed: {
    username() {
      return this.$store.state.user.username;
    },

    isLoginOrRegisterPage() {
      return this.$route.name !== "login" && this.$route.name !== "register";
    },
  },
};
</script>

<style>
@import url("https://fonts.googleapis.com/css2?family=Cantora+One&display=swap");
@import "animate.css";
/* @import url("https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css"); */
@font-face {
  font-family: "BloodyTerror";
  src: url("../BloodyTerror-GOW9Z.ttf");
}

#app {
  margin: 0%;
  padding: 0%;
  background-image: url("pittsburgheitherofliving.jpg");
  background-position: center;
  background-size: cover;
  background-attachment: fixed;
}
#mainContent {
  display: flex;
  width: auto;
  min-height: 100vh;
  align-items: flex-start;
  justify-content: center;
}
#mainContent.verticallyCentered {
  align-items: center;
}
.marginNav {
  margin-top: 60px; /* offset from nav */
}
#nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #333;
  height: 60px;
  padding: 0 20px;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1;
}
.nav-left {
  flex: 1;
  text-align: left;
}

.nav-middle {
  flex: 1;
  text-align: center;
  font-size: 55px;
  background-color: 333;
}
.nav-right {
  flex: 1;
  text-align: right;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-right: 20px;
}
.welcome {
  font-family: nosifer;
  animation: drip 3s infinite alternate;
}
.browseHeading{
  font-family: BloodyTerror;
  color: red;
  font-size: 100px;
  -webkit-text-stroke-width: 2px;
  -webkit-text-stroke-color: black;
  margin-top:80px;
}
@keyframes drip {
  to {
    text-shadow: 4px 4px 1px #300000, 4px 6px 1px #400000, 4px 8px 1px #500000,
      4px 10px 1px #600000, 4px 12px 1px #700000, 4px 14px 1px #800000,
      4px 16px 1px #900000, 4px 18px 1px #a00000, 4px 20px 1px #b00000,
      4px 22px 1px #c00000, 4px 24px 1px #d00000, 4px 26px 1px #e00000,
      4px 28px 1px #f00000, 4px 30px 1px #fa0000, 4px 32px 1px #fb0000,
      4px 34px 1px #fc0000, 4px 36px 1px #fd0000, 4px 38px 1px #fe0000,
      4px 40px 2px #ff0000;
  }
}
#footer {
  background-color: rgba(0, 0, 0, 0.534);
  font-family: BloodyTerror;
  font-size: 20px;
  color: rgba(0, 189, 0, 0.822);
  -webkit-text-stroke-width: 1px;
  -webkit-text-stroke-color: black;
  text-align: center;
  width: 100vw;
  position: fixed;
  bottom: 0;
}
.clear-search {
  margin-left: -50px;
}

.search-container {
  position: absolute;
  top: 9%;
  right: 250px;
  display: flex;
  align-items: center;
  padding-top: 10px;
}

#search-movie {
  margin-right: 0px;
}

#searchButton {
  border-radius: 50%;
  font-size: 20px;
  width: 40px;
  height: 40px;
  background-color: white;
  border: 2px solid black;
  margin-right: 180px;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: all 0.3s ease;
}

#searchButton.scared:hover {
  background-color: rgb(12, 11, 11);
  color: red;
  transform: rotate(30deg);
  box-shadow: 2px 2px 2px rgba(0, 0, 0, 0.5);
}

#searchButton.scared:active {
  transform: rotate(0deg);
  box-shadow: none;
}

.scroller-container {
  margin-top: 100px;
}
.scroller-heading {
  font-family: BloodyTerror;
  color: red;
  font-size: 100px;
  -webkit-text-stroke-width: 2px;
  -webkit-text-stroke-color: black;
  margin-bottom: -100px;
  margin-left: 50px;
}
.scroller-container > p {
  color: red;
  text-align: center;
  margin-top: -100px;
  font-size: 30px;
  font-weight: bold;
  width: 80vw;
  margin-left: 10vw;
  margin-right: 10vw;
  -webkit-text-stroke-width: 1px;
  -webkit-text-stroke-color: black;
}
.link {
  color: rgba(163, 0, 192, 0.876);
  font-family: BloodyTerror;
  -webkit-text-stroke-width: 1px;
  -webkit-text-stroke-color: black;
}
#bottom-space{
  width:100vw;
  height:10vh;
}
</style>