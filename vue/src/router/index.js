import Vue from 'vue'
import Router from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Logout from '../views/Logout.vue'
import Register from '../views/Register.vue'
import store from '../store/index'
import Admin from '../views/Admin.vue'
import MovieDetails from '../views/MovieDetails.vue'
 import UserSearch from '../views/UserSearch.vue'
import UserProfile from '../views/UserProfile.vue'
import AllMovies from '../views/AllMovies.vue'
import TopPicks from '../views/TopPicks.vue'
import LikedMovies from '../views/LikedMovies.vue'

Vue.use(Router)

/**
 * The Vue Router is used to "direct" the browser to render a specific view component
 * inside of App.vue depending on the URL.
 *
 * It also is used to detect whether or not a route requires the user to have first authenticated.
 * If the user has not yet authenticated (and needs to) they are redirected to /login
 * If they have (or don't need to) they're allowed to go about their way.
 */

const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: "/login",
      name: "login",
      component: Login,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/logout",
      name: "logout",
      component: Logout,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/register",
      name: "register",
      component: Register,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/admin",
      name: "admin",
      component: Admin,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: "/moviedetails/:imdbId",
      name: "moviedetails",
      component: MovieDetails,
      meta: {
        requiresAuth: true
      },
      props: true
    },
    {
      path: "/userprofile",
      name: "userprofile",
      component: UserProfile,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: "/allmovies",
      name: "allmovies",
      component: AllMovies,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: "/usersearch/:term",
      name: "usersearch",
      component: UserSearch,
    },
    {
      path: "/toppicks",
      name: "toppicks",
      component: TopPicks,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: "/likedmovies",
      name: "likedmovies",
      component: LikedMovies,
      meta: {
        requiresAuth: true
      }
    },
  ],
  scrollBehavior () {
    return { x: 0, y: 0 };
  }
})

router.beforeEach((to, from, next) => {
  // Determine if the route requires Authentication
  const requiresAuth = to.matched.some(x => x.meta.requiresAuth);

  // If it does and they are not logged in, send the user to "/login"
  if (requiresAuth && store.state.token === '') {
    next("/login");
  } else {
    // Else let them go to their next destination
    next();
  }
});

export default router;
