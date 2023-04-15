<template>
  <div class="view-container">
    <h1>Passwort Vergessen</h1>
    <ForgotPasswordRequest v-if="status === 'request'" :email-prop="email"></ForgotPasswordRequest>
    <ForgotPasswordChange v-if="status === 'success'" :email="email" :verification-key="verificationKey"></ForgotPasswordChange>
  </div>
</template>

<script>
import ForgotPasswordRequest from "@/components/ForgotPasswordRequest.vue";
import ForgotPasswordChange from "@/components/ForgotPasswordChange.vue";

export default {
  name: "PasswortView",
  components: {ForgotPasswordRequest, ForgotPasswordChange},
  props: {
    email: String,
    verificationKey: String
  },
  data() {
    return {
      status: 'request'
    }
  },
  mounted() {
    if (this.email && this.verificationKey) {
      this.status = 'success';
    } else {
      this.status = 'request';
    }
    console.log(this.$route.name, this.email, this.verificationKey);
  }
}
</script>

<style scoped lang="scss">
.view-container {
  h1 {
    text-align: center;
    font-size: 32px;
    padding-top: 1rem;
    margin-bottom: 1rem;
  }

  min-height: 100vh;
  background-image: url("../assets/background.png");
}
</style>