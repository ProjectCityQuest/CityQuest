<template>
  <div class="register-wrapper">
    <p v-if="error" id="response-error">{{ errorMessage }}</p>
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

      <CQButton b-style="register" :status="state" @click="sendRegistration()">Registrieren</CQButton>
    </form>
    <div id="spacer"></div>
    <router-link class="router-link" to="/login">Login</router-link>
  </div>
</template>

<script>
import useVuelidate from '@vuelidate/core'
import {required$, email$, passwordMinLength$, passwordSameAs$} from "@/validators";
import CQButton from "@/components/CQButton.vue";

export default {
  name: "Register",
  components: {CQButton},
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
      error: false,
      errorMessage: "",
      state: "inactive"
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
    async sendRegistration() {
      this.resetError();
      this.state = "waiting";

      await fetch(`http://${window.location.hostname}:8080/api/register`, {
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
      }).then(response => {
        if (response.status === 201) {
          this.$router.push('/email-verifizierung/' + (this.form.email.replaceAll('.', '-')))
        } else {
          this.setError("Der Benutzername/die E-Mail-Adresse existieren bereits. Bitte wähle einen/eine andern/andere.")
        }
      }).catch((err) => {
        this.state = "active";
        if (err.message === 'Failed to fetch') {
          this.setError("Verbindung konnte nicht hergestellt werden.");
        } else {
          this.setError("Das hat nicht geklappt ):");
        }
      });

      this.state = "active";
    },
    resetError() {
      this.error = false;
      this.errorMessage = "";
    },
    setError(message) {
      this.error = true;
      this.errorMessage = message;
    }
  },
  watch: {
    'v$.form.$invalid'() {
      this.state = this.v$.form.$invalid ? 'inactive' : 'active'
    }
  }
}
</script>

<style scoped lang="scss">
@import "src/assets/colors.scss";
@import "src/assets/form";

.register-wrapper {
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  &:last-child {
    margin-bottom: 2rem;
  }

  #response-error {
    color: $red;
    text-align: center;
    margin: 1.5rem 0 1.5rem 0;
    width: 90%;
  }

  #spacer {
    height: 2rem;
  }
}
</style>