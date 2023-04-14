<template>
  <h1>Anfrage wurde geschickt an {{ email }}</h1>
  <p>erneut senden</p>
</template>

<script>
export default {
  name: "ForgotPasswordPending",
  props: {
    email: String
  }
}
</script>

<style scoped>

</style>