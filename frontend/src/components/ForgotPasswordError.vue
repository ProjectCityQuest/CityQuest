<template>
  <div class="error-container">
    <p class="error">Der Link zum ändern des Passworts ist abgelaufen oder ungültig.</p>
    <svg xmlns="http://www.w3.org/2000/svg" height="63" width="63" viewBox="0 0 34.26 34.26">
      <g id="be9c64bb-9a47-44ed-b912-c4ef3e50443a" data-name="Ebene 2">
        <path
            d="M17.11,0A17.13,17.13,0,1,0,34.24,17.13,17.13,17.13,0,0,0,17.11,0ZM25,22.86,22.84,25l-5.73-5.73L11.38,25,9.24,22.86,15,17.13,9.24,11.4l2.14-2.14L17.11,15l5.73-5.73L25,11.4l-5.73,5.73Z"
            transform="translate(0.02)" style="fill:#ed3624"/>
      </g>
    </svg>
    <router-link class="router-link" to="/passwort-vergessen">Nochmal versuchen</router-link>
    <router-link to="/login">zurück zum Login</router-link>
  </div>
</template>

<script>
export default {
  name: "ForgotPasswordExpired",
  props: {
    errorMessage: String
  }
}
</script>

<style scoped lang="scss">
@import "src/assets/colors.scss";

.error-container {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  width: 80%;
  margin: 0 auto 0 auto;

  .error {
    color: $red;
  }

  svg {
    fill: $green;
    margin: 1rem auto 1rem auto;
  }

  .router-link {
    font-family: 'Berlin Sans FB', sans-serif;
    width: 80%;
    height: 2.5rem;
    margin: 0 auto 1rem auto;
    border: 2px solid transparent;
    color: white;
    text-decoration: none;
    background: $blue;
    border-radius: 10px;

    display: flex;
    justify-content: center;
    align-items: center;
  }
}
</style>