<template>
  <div class="forgot-password-wrapper">
    <form @submit.prevent>
      <!-- Altes Passwort -->
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

      <label>Neues Passwort wiederholen</label>
      <input class="password input-field" type="password"
             v-model="v$.form.passwordRepeat.$model"
             :class="status(v$.form.passwordRepeat)">
      <!-- error message -->
      <div class="input-errors" v-for="(error, index) of v$.form.passwordRepeat.$errors" :key="index">
        <div class="error-msg">{{ error.$message }}</div>
      </div>

      <button class="submit" :disabled="v$.form.$invalid || request.status === 'pending'"
              :class="request.status">
        Passwort ändern
      </button>
      <p class="feedback-message" :class="request.status">{{ request.message }}</p>
    </form>
  </div>
</template>

<script>
import useVuelidate from "@vuelidate/core";
import {required$, passwordMinLength$, passwordSameAs$} from "@/validators";

export default {
  name: "ForgotPasswordChange",
  setup() {
    return {v$: useVuelidate()}
  },
  data() {
    return {
      form: {
        oldPassword: "",
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
        oldPassword: {
          required$,
          min: passwordMinLength$(8)
        },
        newPassword: {
          required$,
          min: passwordMinLength$(8)
        },
        passwordRepeat: {
          required$,
          sameAsPassword: passwordSameAs$(this.form.passwordRepeat)
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
  }
}
</script>

<style scoped lang="scss">
@import "../assets/form";

.forgot-password-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
}
</style>