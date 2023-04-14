<template>
  <ForgotPasswordRequest v-if="status === 'request'"></ForgotPasswordRequest>
  <ForgotPasswordPending :email="email" v-if="status === 'pending'"></ForgotPasswordPending>
  <ForgotPasswordExpired v-if="status === 'expired'"></ForgotPasswordExpired>
  <ForgotPasswordChange v-if="status === 'success'"></ForgotPasswordChange>
</template>

<script>
import ForgotPasswordRequest from "@/components/ForgotPasswordRequest.vue";
import ForgotPasswordExpired from "@/components/ForgotPasswordExpired.vue";
import ForgotPasswordPending from "@/components/ForgotPasswordPending.vue";
import ForgotPasswordChange from "@/components/ForgotPasswordChange.vue";

export default {
  name: "PasswortView",
  components: {ForgotPasswordExpired, ForgotPasswordRequest, ForgotPasswordPending, ForgotPasswordChange},
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
    } else if (this.email) {
      this.status = 'pending';
    } else {
      this.status = 'request';
    }
    console.log(this.$route.name, this.email, this.verificationKey);
  }
}
</script>

<style scoped>

</style>