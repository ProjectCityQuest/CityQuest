<template>
  <div class="view-container">
    <div class="change-password-wrapper">
      <h1>Passwort ändern</h1>

      <div class="explanation-container">
        <p>Dein Passwort sollte folgende Bedingungen erfüllen:</p>
        <ul>
          <li>12 Zeichen lang</li>
          <li>Groß- und Kleinbuchstaben enthalten</li>
        </ul>
      </div>

      <form @submit.prevent>
        <label>Altes Passwort</label>
        <input class="password input-field" type="password"
               v-model="v$.form.oldPassword.$model"
               :class="status(v$.form.oldPassword)">
        <!-- error message -->
        <div class="input-errors" v-for="(error, index) of v$.form.oldPassword.$errors" :key="index">
          <div class="error-msg">{{ error.$message }}</div>
        </div>

        <label>Neues Passwort</label>
        <input class="password input-field" type="password"
               v-model="v$.form.newPassword.$model"
               :class="status(v$.form.newPassword)">
        <!-- error message -->
        <div class="input-errors" v-for="(error, index) of v$.form.newPassword.$errors" :key="index">
          <div class="error-msg">{{ error.$message }}</div>
        </div>

        <label>Passwort wiederholen</label>
        <input class="password input-field" type="password"
               v-model="v$.form.confirmPassword.$model"
               :class="status(v$.form.confirmPassword)">
        <!-- error message -->
        <div class="input-errors" v-for="(error, index) of v$.form.confirmPassword.$errors" :key="index">
          <div class="error-msg">{{ error.$message }}</div>
        </div>

        <p class="feedback-message" :class="feedback.status">{{ feedback.message }}</p>

        <button :disabled="v$.form.$invalid" @click="requestChangePassword()">Speichern</button>
      </form>

      <router-link class="router-link" to="/account">zurück zum Account</router-link>
    </div>
  </div>
</template>

<script>
import useVuelidate from "@vuelidate/core";
import {passwordMinLength$, passwordSameAs$, required$} from "@/validators";

export default {
  name: "ChangePasswordView",
  setup() {
    return {v$: useVuelidate()}
  },
  data() {
    return {
      form: {
        oldPassword: "",
        newPassword: "",
        confirmPassword: ""
      },
      error: false,
      feedback: {
        status: "",
        message: ""
      }
    }
  },
  validations() {
    return {
      form: {
        oldPassword: {
          required$,
          min: passwordMinLength$(8)
        },
        newPassword: {
          required$,
          min: passwordMinLength$(8)
        },
        confirmPassword: {
          required$,
          sameAsPassword: passwordSameAs$(this.form.newPassword)
        },
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
    getCookie(cname) {
      let name = cname + "=";
      let decodedCookie = decodeURIComponent(document.cookie);
      let ca = decodedCookie.split(';');
      for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') {
          c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
          return c.substring(name.length, c.length);
        }
      }
      return false;
    },
    async requestChangePassword() {
      this.resetFeedback();

      const response = await fetch(`http://${window.location.hostname}/api/changepasswordauth`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
          sessionKey: this.getCookie('sessionKey'),
        },
        withCredentials: true,
        credentials: 'same-origin',
        body: JSON.stringify({
          old_password: this.form.oldPassword,
          new_password: this.form.newPassword
        })
      })

      if (response.ok) {
        this.setSuccessMessage('Dein Passwort wurde geändert.')
        this.form.oldPassword = "";
        this.form.newPassword = "";
        this.form.confirmPassword = "";
        this.v$.$reset()
      } else {
        if (response.status === 401) {
          this.setErrorMessage('Das alte Passwort ist falsch.')
        } else {
          this.setErrorMessage('Das hat nicht geklappt :(')
        }
      }
    },
    resetFeedback() {
      this.request = {
        status: "",
        message: ""
      }
    },
    setErrorMessage(errorMessage) {
      this.feedback.status = "error";
      this.feedback.message = errorMessage;
    },
    setSuccessMessage(successMessage) {
      this.feedback.status = "success";
      this.feedback.message = successMessage;
    },
  }
}
</script>

<style scoped lang="scss">
@import "src/assets/form";
@import "src/assets/media_query";

.view-container {
  .change-password-wrapper {
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    background-image: url('/src/assets/background.png');

    h1 {
      font-size: 32px;
      margin: 1rem 1rem 1.5rem 1rem;
    }

    .explanation-container {
      width: 60%;

      p {
        text-align: center;
        margin-bottom: 1rem;
      }

      ul {
        margin-bottom: 1rem;

        li {
          margin-bottom: 0.5rem;
        }
      }
    }

    form {
      &:last-child {
        margin-bottom: 2rem;
      }
    }

    .feedback-message {
      margin-top: 1rem;
      text-align: center;

      &.success {
        color: $dark_green;
      }

      &.error {
        color: $red;
      }
    }

    .router-link {
      margin: 2rem 0 2rem 0;
    }
  }
}

@include media-query() {
  .change-password-wrapper {
    .explanation-container {
      width: 40%;
    }
  }
}
</style>