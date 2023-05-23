<template>
  <div class="view-container">
    <p>Welcome to CityQuest</p>
    <router-link to="/login">Login</router-link>
    <br>
    <router-link to="/registrierung">Registrieren</router-link>
  </div>
</template>

<script>
export default {
  name: "LandingView"
}
</script>

<style scoped lang="scss">
.view-container {
  background-image: url("../assets/background.png");
  min-height: 100vh;
}
</style>