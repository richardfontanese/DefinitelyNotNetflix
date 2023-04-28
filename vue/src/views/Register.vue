<template>
  <div id="register" class="text-center">
    <form @submit.prevent="register">
      <h1>Create Account</h1>
      <div role="alert" v-if="registrationErrors">
        {{ registrationErrorMsg }}
      </div>
      <div class="form-input-group">
        <label for="username">Username</label>
        <input type="text" id="username" v-model="user.username" required autofocus />
      </div>
      <div class="form-input-group">
        <label for="password">Password</label>
        <input type="password" id="password" v-model="user.password" required />
      </div>
      <div class="form-input-group">
        <label for="confirmPassword">Confirm Password</label>
        <input type="password" id="confirmPassword" v-model="user.confirmPassword" required />
      </div>
      <button type="submit">Create Account</button>
      <p id="signInButton" ><router-link :to="{ name: 'login' }">Already have an account? Log in.</router-link></p>
    </form>
  </div>
</template>

<script>
import authService from '../services/AuthService';

export default {
  name: 'register',
  data() {
    return {
      user: {
        username: '',
        password: '',
        confirmPassword: '',
        role: 'user',
      },
      registrationErrors: false,
      registrationErrorMsg: 'There were problems registering this user.',
    };
  },
  methods: {
    register() {
      if (this.user.password != this.user.confirmPassword) {
        this.registrationErrors = true;
        this.registrationErrorMsg = 'Password & Confirm Password do not match.';
      } else {
        authService
          .register(this.user)
          .then((response) => {
            if (response.status == 201) {
              this.$router.push({
                path: '/login',
                query: { registration: 'success' },
              });
            }
          })
          .catch((error) => {
            const response = error.response;
            this.registrationErrors = true;
            if (response.status === 400) {
              this.registrationErrorMsg = 'Bad Request: Validation Errors';
            }
          });
      }
    },
    clearErrors() {
      this.registrationErrors = false;
      this.registrationErrorMsg = 'There were problems registering this user.';
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
#register {
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
#signInButton {
  font-size: 20px;
  margin: 10px;
  border-width: 2px;
  border-style: solid;
  border-radius: 10px;
  background-color: rgba(226, 43, 43, 0.678);
  border-color: rgba(0, 0, 0, 0.829);
}
#register {
  display: flexbox;
  width: auto;
  height: auto;
  align-items: center;
  justify-content: center;
}
</style>
