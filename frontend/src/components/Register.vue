<template>
  <div class="register-wrapper">
    <p v-if="error" id="response-error">Der Benutzername/die E-Mail-Adresse existieren bereits. Bitte wähle einen/eine andern/andere.</p>
    <div v-else id="spacer"></div>
    <form @submit.prevent>
      <!-- Username -->
      <label>Benutzername</label>
      <input class="username input-field" type="text"
             v-model="v$.form.username.$model"
             :class="status(v$.form.username)">
      <!-- error message -->
      <div class="input-errors" v-for="(error, index) of v$.form.username.$errors" :key="index">
        <div class="error-msg">{{ error.$message }}</div>
      </div>

      <!-- Email -->
      <label>E-Mail</label>
      <input class="email input-field" type="email"
             v-model="v$.form.email.$model"
             :class="status(v$.form.email)">
      <!-- error message -->
      <div class="input-errors" v-for="(error, index) of v$.form.email.$errors" :key="index">
        <div class="error-msg">{{ error.$message }}</div>
      </div>

      <!-- Password -->
      <label>Passwort</label>
      <input class="password input-field" type="password"
             v-model="v$.form.password.$model"
             :class="status(v$.form.password)">
      <!-- error message -->
      <div class="input-errors" v-for="(error, index) of v$.form.password.$errors" :key="index">
        <div class="error-msg">{{ error.$message }}</div>
      </div>

      <!-- Confirm Password -->
      <label>Passwort wiederholen</label>
      <input class="password input-field" type="password"
             v-model="v$.form.confirmPassword.$model"
             :class="status(v$.form.confirmPassword)">
      <!-- error message -->
      <div class="input-errors" v-for="(error, index) of v$.form.confirmPassword.$errors" :key="index">
        <div class="error-msg">{{ error.$message }}</div>
      </div>

      <button :disabled="v$.form.$invalid" @click="sendRegistration()">Registrieren</button>
    </form>
    <div id="spacer"></div>
    <router-link class="router-link" to="/login">Login</router-link>
  </div>
</template>

<script>
import useVuelidate from '@vuelidate/core'
import {required$, email$, passwordMinLength$, passwordSameAs$} from "@/validators";

export default {
  name: "Register",
  setup() {
    return {v$: useVuelidate()}
  },
  data() {
    return {
      form: {
        username: "",
        email: "",
        password: "",
        confirmPassword: ""
      },
      error: false
    }
  },
  validations() {
    return {
      form: {
        username: {
          required$
        },
        email: {
          required$, email$
        },
        password: {
          required$,
          min: passwordMinLength$(8)
        },
        confirmPassword: {
          required$,
          sameAsPassword: passwordSameAs$(this.form.password)
        }
      }
    }
  },
  methods: {
    status(validation) {
      return {
        error: validation.$error,
        dirty: validation.$dirty
      }
    },
    sendRegistration() {
      fetch(`http://${window.location.hostname}:8080/api/register`, {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
              'username': this.form.username,
              'password': this.form.password,
              'email': this.form.email
            }
        )
      })
          .then(response => {
            if (response.status === 201) {
              window.location.replace('http://localhost:5173/email-verifizierung/'+(this.form.email.replaceAll('.','-')))
            } else {
              this.error = true
            }
          })
    }
  }
}
</script>

<style scoped lang="scss">
@import "src/assets/colors.scss";

.register-wrapper {
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: $white;

  #response-error{
    color: $red;
    text-align: center;
    margin: 1.5rem 0 1.5rem 0;
  }

  #spacer{
    height: 2rem;
  }

  form {
    width: 60%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    label {
      width: 100%;
      text-align: left;
      margin-top: 1rem;
      margin-bottom: 5px;
    }

    input {
      width: 100%;
      height: 2.5rem;
      border: 1px solid $gray;
      border-radius: 10px;
      background: white;
      margin-bottom: 0.5rem;
      padding-left: 0.5rem;

      &.dirty {
        border-color: $green;

        &:focus {
          outline-color: $green;
        }
      }

      &.error {
        border-color: $red;

        &:focus {
          outline-color: $red;
        }
      }
    }

    button {
      width: 100%;
      height: 3rem;
      border: 2px solid transparent;
      color: white;
      margin-top: 2.5rem;
      font-weight: bolder;
      background: $blue;
      border-radius: 10px;

      &:disabled {
        background: $gray;
      }
    }

    .input-errors {
      width: 100%;
      margin-bottom: 0.5rem;
    }

    .error-msg {
      color: $red;
      text-align: left;
    }

    .router-link {
      color: $black;
    }
  }
}
</style>