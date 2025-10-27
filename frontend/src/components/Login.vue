<template>
  <div class="register-wrapper">
    <p v-if="error" id="response-error">{{ errorMessage }}</p>
    <div v-else id="spacer"></div>
    <form @submit.prevent>
      <!-- Benutzername -->
      <label>Benutzername</label>
      <input class="username input-field" type="text"
             v-model="v$.form.username.$model"
             :class="status(v$.form.username)">
      <!-- error message -->
      <div class="input-errors" v-for="(error, index) of v$.form.username.$errors" :key="index">
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

      <CQButton b-style="login" :status="state" @click="sendLogin()">Login</CQButton>
    </form>
    <div id="spacer"></div>
    <router-link class="router-link" to="/passwort-vergessen">Passwort vergessen?</router-link>
    <div id="spacer2"></div>
    <router-link class="router-link" to="/registrierung">Noch keinen Account?</router-link>
  </div>
</template>

<script>
import useVuelidate from '@vuelidate/core'
import {required$, passwordMinLength$} from "@/validators";
import CQButton from "@/components/CQButton.vue";

export default {
  name: "Login",
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
        confirmPassword: "",
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
        password: {
          required$,
          min: passwordMinLength$(8)
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
    async sendLogin() {
      this.resetError();
      this.state = "waiting";

      const response = await fetch(`http://${window.location.hostname}/api/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        withCredentials: true,
        credentials: 'same-origin',
        body: JSON.stringify({
              'username': this.form.username,
              'password': this.form.password
            }
        )
      }).catch((err) => {
        this.state = "active";
        if (err.message === 'Failed to fetch') {
          this.setError("Verbindung konnte nicht hergestellt werden.");
        } else {
          this.setError("Das hat nicht geklappt ):");
        }
      })

      this.state = "active";

      if (!response) {
        return;
      }

      if (response.status === 200) {
        let data = await response.json();
        document.cookie = "sessionKey= ; expires = Thu, 01 Jan 1970 00:00:00 GMT";
        document.cookie = "sessionKey=" + data.sessionKey + "; path=/";
        if(data.firstLogin){
          this.$router.push("/einfuehrung");
        } else{
          this.$router.push("/karte");
        }
      } else {
        this.setError("Der angegebene Benutzername oder das Passwort sind falsch. Bitte versuche es erneut")
      }
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

  #spacer2 {
    height: 1rem;
  }
}
</style>