<template>
  <div class="forgot-password-wrapper">
    <form @submit.prevent>
      <label>Neues Passwort</label>
      <input class="password input-field" type="password"
             v-model="v$.form.newPassword.$model"
             :class="status(v$.form.newPassword)">
      <!-- error message -->
      <div class="input-errors" v-for="(error, index) of v$.form.newPassword.$errors" :key="index">
        <div class="error-msg">{{ error.$message }}</div>
      </div>

      <label>Neues Passwort wiederholen</label>
      <input class="password input-field" type="password"
             v-model="v$.form.passwordRepeat.$model"
             :class="status(v$.form.passwordRepeat)">
      <!-- error message -->
      <div class="input-errors" v-for="(error, index) of v$.form.passwordRepeat.$errors" :key="index">
        <div class="error-msg">{{ error.$message }}</div>
      </div>

      <button class="submit" :disabled="v$.form.$invalid || request.status === 'pending'"
              :class="request.status" @click="this.requestChangePassword">
        Passwort ändern
      </button>

      <p class="feedback-message" :class="request.status">{{ request.message }}</p>

      <router-link to="/login" class="router-link">Login</router-link>
    </form>
  </div>
</template>

<script>
import useVuelidate from "@vuelidate/core";
import {required$, passwordMinLength$, passwordSameAs$} from "@/validators";

export default {
  name: "ForgotPasswordChange",
  emits: ["error"],
  props: {
    email: String,
    verificationKey: String
  },
  setup() {
    return {v$: useVuelidate()}
  },
  data() {
    return {
      form: {
        newPassword: "",
        passwordRepeat: ""
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
        newPassword: {
          required$,
          min: passwordMinLength$(8)
        },
        passwordRepeat: {
          required$,
          sameAsPassword: passwordSameAs$(this.form.newPassword)
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
    async requestChangePassword() {
      this.resetFeedback();
      this.request.status = 'pending';

      const response = await fetch(`https://${window.location.hostname}/api/changepassword`, {
        method: 'PATCH',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          key: this.verificationKey,
          email: this.email,
          new_password: this.form.newPassword
        })
      });

      if (response.ok) {
        this.setSuccessMessage('Dein Passwort wurde geändert.')
      } else {
        this.$emit('error')
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
@import "src/assets/form";

.forgot-password-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;

  &:last-child {
    margin-bottom: 2rem;
  }


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

    .router-link {
      display: block;
      margin-top: 1rem;
    }
  }
}
</style>