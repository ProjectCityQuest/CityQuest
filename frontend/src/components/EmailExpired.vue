<template>
  <div class="expired-container">
    <p>Der Link ist abgelaufen.</p>
    <p> Um einen neuen Link zu erhalten muss du leider nochmal deine Daten angeben.</p>
    <router-link class="router-link" to="/registrierung">zurück zur Registrierung</router-link>
  </div>
</template>

<script>
export default {
  name: "EmailExpired"
}
</script>

<style scoped lang="scss">
@import "src/assets/colors.scss";

.expired-container {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  width: 80%;
  margin: 0 auto 0 auto;

  .router-link {
    width: 80%;
    height: 2.5rem;
    margin: 2.5rem auto 0 auto;
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