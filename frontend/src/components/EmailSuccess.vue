<template>
  <div class="success-container">
    <p>Deine E-Mail-Adresse wurde erfolgreich verifiziert.</p>
    <svg xmlns="http://www.w3.org/2000/svg" height="65" width="65" viewBox="0 0 48 48">
      <path
          d="m17.3 45-3.8-6.5-7.55-1.55.85-7.35L2 24l4.8-5.55-.85-7.35 7.55-1.55L17.3 3 24 6.1 30.7 3l3.85 6.55 7.5 1.55-.85 7.35L46 24l-4.8 5.6.85 7.35-7.5 1.55L30.7 45 24 41.9Zm4.55-14.35L33.2 19.4l-2.25-2.05-9.1 9-4.75-4.95-2.3 2.25Z"/>
    </svg>
    <router-link class="router-link" to="/login">zum Login</router-link>
  </div>
</template>

<script>
export default {
  name: "EmailSuccess"
}
</script>

<style scoped lang="scss">
@import "src/assets/colors.scss";

.success-container {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  width: 80%;
  margin: 0 auto 0 auto;

  svg{
    fill: $green;
    margin: 1rem auto 1rem auto;
  }

  .router-link {
    width: 80%;
    height: 2.5rem;
    margin: 0 auto 0 auto;
    border: 2px solid transparent;
    color: white;
    font-weight: bolder;
    font-family: sans-serif;
    text-decoration: none;
    background: $blue;
    border-radius: 10px;

    display: flex;
    justify-content: center;
    align-items: center;
  }
}
</style>