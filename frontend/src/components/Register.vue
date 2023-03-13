<template>
  <div class="register-wrapper">
    <form @submit.prevent>
      <!-- Firstname -->
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
      <label>Passwort Wiederholen</label>
      <input class="password input-field" type="password"
             v-model="v$.form.confirmPassword.$model"
             :class="status(v$.form.confirmPassword)">
      <!-- error message -->
      <div class="input-errors" v-for="(error, index) of v$.form.confirmPassword.$errors" :key="index">
        <div class="error-msg">{{ error.$message }}</div>
      </div>

      <div class="hr"></div>

      <button :disabled="v$.form.$invalid">Registrieren</button>
    </form>
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
      }
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
    }
  }
}
</script>

<style scoped lang="scss">
@import "src/assets/colors.scss";

.register-wrapper {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: $background;

  form {
    width: 284px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    margin-top: 5vh;

    label {
      width: 100%;
      text-align: left;
      margin-top: 0.5rem;
    }

    input {
      width: 100%;
      height: 3rem;
      border: 2px solid $border;
      border-radius: 12px;
      background: white;
      margin-bottom: 0.5rem;
      padding-left: 0.5rem;

      &.dirty {
        border-color: #5A5;

        &:focus {
          outline-color: #8E8;
        }
      }

      &.error {
        border-color: red;

        &:focus {
          outline-color: #F99;
        }
      }
    }

    button {
      width: 100%;
      height: 3rem;
      border: 2px solid $border;
      color: white;
      font-weight: bolder;
      background: $primary;
      border-radius: 12px;

      &:disabled {
        background: $primary-disabled;
      }
    }

    .input-errors {
      width: 100%;
      margin-bottom: 0.5rem;
    }

    .error-msg {
      color: red;
      text-align: left;
    }
  }
}
</style>