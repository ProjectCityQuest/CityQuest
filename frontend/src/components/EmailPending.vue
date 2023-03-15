<template>
  <div class="pending-container">
    <p>Wir haben ein E-Mail an</p>
    <p id="email">{{ this.email }}</p>
    <p>geschickt.</p>
    <p>Bitte klicke auf den Link in der E-Mail, um die Adresse zu verifizieren.</p>
  </div>
</template>

<script>
export default {
  name: "EmailPending",
  props: {
    email: String
  }
}
</script>

<style scoped lang="scss">
@import "src/assets/colors.scss";

.pending-container {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  width: 80%;
  margin: 0 auto 0 auto;

  p {
    line-height: 1.2rem;
  }

  #email {
    color: $blue;
  }
}
</style>