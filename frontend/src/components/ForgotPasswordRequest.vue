<template>
  <div class="forgot-password-wrapper">
    <form @submit.prevent>
      <!-- Email -->
      <label>E-Mail-Adresse</label>
      <input class="email input-field" type="email"
             v-model="v$.form.email.$model"
             :class="status(v$.form.email)">
      <!-- error message -->
      <div class="input-errors" v-for="(error, index) of v$.form.email.$errors" :key="index">
        <div class="error-msg">{{ error.$message }}</div>
      </div>

      <button class="submit" :disabled="v$.form.$invalid || request.status === 'pending'" @click="requestForgotPassword"
              :class="request.status">Anfrage
      </button>
      <p class="feedback-message" :class="request.status">{{ request.message }}</p>
    </form>
  </div>
</template>

<script>
import useVuelidate from "@vuelidate/core";
import {required$, email$} from "@/validators";

export default {
  name: "ForgotPasswordRequest",
  setup() {
    return {v$: useVuelidate()}
  },
  data() {
    return {
      form: {
        email: "",
      },
      request: {
        status: "",
        message: ""
      }
    }
  },
  validations() {
    return {
      form: {
        email: {
          required$,
          email$
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
    async requestForgotPassword() {
      this.resetFeedback();
      this.request.status = 'pending';

      const response = await fetch(`http://${window.location.hostname}:8080/api/forgotpassword/${this.form.email}`, {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        }
      });

      if (response.ok) {
        this.setSuccessMessage(`Eine Anfrage zur Passwortänderung wurde an ${this.form.email} gesendet.`)
      } else {
        if (response.status === 400) {
          response.json().then(data => this.setErrorMessage(data.error))
        } else {
          this.setErrorMessage('Das hat nicht geklappt ):');
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
      this.request.status = "error";
      this.request.message = errorMessage;
    },
    setSuccessMessage(successMessage) {
      this.request.status = "success";
      this.request.message = successMessage;
    },
  }
}
</script>

<style scoped lang="scss">
@import "../assets/form";
@import "src/assets/colors";

.forgot-password-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;

  form {
    .submit {
      &.pending {
        background: none;
        border: 2px $blue solid;
        color: $blue;
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
  }
}

</style>