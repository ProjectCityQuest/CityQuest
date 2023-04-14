<template>
  <h1>Anfrage</h1>
  <form @submit.prevent>
    <!-- Email -->
    <label>E-Mail</label>
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

      const response = await fetch(`http://${window.location.hostname}:8080/api/forgotpassword`, {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          email: this.form.email
        })
      });

      if (response.ok) {
        this.setSuccessMessage(`Eine Anfrage zur Passwortänderung wurde an ${this.email} gesendet.`)
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
.submit {
  &.pending {
    background: #00AAFF;
  }
}
</style>