<template>
  <div id="outer">
    <div id="login">
      <form @submit.prevent="login">
        <h1>Please Sign In</h1>
        <div role="alert" v-if="invalidCredentials">
          Invalid username and password!
        </div>
        <div role="alert" v-if="this.$route.query.registration">
          Thank you for registering, please sign in.
        </div>
        <div class="form-input-group">
          <label for="username">Username</label>
          <input
            type="text"
            id="username"
            v-model="user.username"
            required
            autofocus
          />
        </div>
        <div class="form-input-group">
          <label for="password">Password</label>
          <input
            type="password"
            id="password"
            v-model="user.password"
            required
          />
        </div>
        <button type="submit">Sign in</button>
        <p id="signUpButton">
          <router-link :to="{ name: 'register' }"
            >Need an account? Sign up.</router-link
          >
        </p>
      </form>
    </div>
  </div>
</template>

<script>
import authService from "../services/AuthService";

export default {
  name: "login",
  components: {},
  data() {
    return {
      user: {
        username: "",
        password: "",
      },
      invalidCredentials: false,
    };
  },
  methods: {
    login() {
      authService
        .login(this.user)
        .then((response) => {
          if (response.status == 200) {
            this.$store.commit("SET_AUTH_TOKEN", response.data.token);
            this.$store.commit("SET_USER", response.data.user);
            if (this.user.username == "admin") {
              this.$router.push("/admin");
            } else {
              this.$router.push("/");
            }
          }
        })
        .catch((error) => {
          const response = error.response;

          if (response.status === 401) {
            this.invalidCredentials = true;
          }
        });
    },
  },
};
</script>

<style scoped>
.form-input-group {
  margin-bottom: 2rem;
}
label {
  margin: 10px;
  color: rgb(58, 53, 45);
}
h1 {
  margin: 20px;
  color: rgb(58, 53, 45);
}
button {
  width: 200px;
  margin: 10px;
  border-radius: 10px;
  background-color: rgba(226, 43, 43, 0.678);
  font-size: 20px;
  font-family: "cantora one";
}
input {
  font-family: "cantora one";
  border-radius: 10px;
}
#login {
  background-color: rgba(218, 208, 157, 0.671);
  border-width: 5px;
  border-style: solid;
  border-radius: 25px;
  border-color: rgba(255, 0, 0, 0.363);
  height: 60vh;
  width: 20vw;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  font-size: 20px;
  font-family: "cantora one";
  padding: 20px; 
}
a {
  color: rgb(8, 8, 8);
}
#signUpButton {
  font-size: 20px;
  margin: 10px;
  border-width: 2px;
  border-style: solid;
  border-radius: 10px;
  background-color: rgba(226, 43, 43, 0.678);
  border-color: rgba(0, 0, 0, 0.829);
}
#login {
  display: flexbox;
  width: auto;
  /* height: 50vh; */
  height: auto;
  align-items: center;
  justify-content: center;
}
</style>