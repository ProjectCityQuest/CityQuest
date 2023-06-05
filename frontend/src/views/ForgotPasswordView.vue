<template>
  <div class="view-container">
    <svg class="logo" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 226.57 277.17">
      <g id="f8d16813-69cc-40c1-944c-750c609b7c90" data-name="Ebene 2">
        <path
            d="M42.8,185.22a1,1,0,0,0,0,1.52c5.93,4.95,38.5,32.7,34.93,41.94S57.18,232,50.24,239c-10.37,10.36-.35,22.76,5.22,26.64,17.79,12.4,23.46,2,24.91.11,3.4-4.5,2-14.22,13.69-21.76,11.07-7.14,43.83,27.44,46.35,30.14a.28.28,0,0,0,.35,0C144,272,187.43,242.3,181,229.83c-4.74-9.14-20.3-1-27.69-10.53-5.67-7.29,4.84-18.33,9.38-22.36,10.84-9.61,20.51-7.53,26.25-4.18,11.77,6.86.86,17,11.73,22.72,8.84,4.61,41.32-28.13,46.89-33.85a1,1,0,0,0-.1-1.56c-6-4.79-39.85-32.25-32.75-42.53,7.39-10.7,17-1.72,23.36-6,18.3-12.32,4.26-25.35-1.63-29.36-5.57-3.79-18.85-9.94-26.89,1.28-3.4,4.74-1,20.47-10.37,21.89C183.5,127.73,149.5,98,142.87,92a1.4,1.4,0,0,0-2.06.2c-6.76,8.88-48.63,62.87-63.43,55.94-10.09-4.73-3-17.07-13.73-24.76-4.72-3.4-14-5.52-23,3.5-12.47,12.48-12.74,18-9.94,23.21,7.87,14.63,18.25-1.46,26.52,12.54C60.94,168.93,46.6,181.94,42.8,185.22Z"
            transform="translate(-25.24 -1.02)" style="fill:none;stroke:#969696;stroke-miterlimit:10;stroke-width:8px"/>
      </g>
      <g id="a85a94cf-0ebe-46c7-bb29-2d7381f4baaf" data-name="Ebene 3">
        <path
            d="M134.71,5c-21.22-.3-62,14.13-62,58.05,0,45.08,62,126.31,62,126.31s59.58-80.16,59.58-125.29C194.29,17.34,155.69,5.31,134.71,5Zm-.39,84.16a26.93,26.93,0,1,1,26.93-26.93A26.93,26.93,0,0,1,134.32,89.18Z"
            transform="translate(-25.24 -1.02)"
            style="fill:#ed3524;stroke:#b41415;stroke-miterlimit:10;stroke-width:8px"/>
        <circle cx="109.08" cy="61.23" r="26.93"
                style="fill:none;stroke:#b41415;stroke-miterlimit:10;stroke-width:8px"/>
      </g>
    </svg>
    <h1>Passwort Vergessen</h1>
    <ForgotPasswordRequest v-if="status === 'request'" :email-prop="email"></ForgotPasswordRequest>
    <ForgotPasswordChange v-if="status === 'change'" :email="email"
                          :verification-key="verificationKey" @error="status = 'error'"></ForgotPasswordChange>
    <ForgotPasswordError v-if="status === 'error'" :error-message="'Der Link ist abgelaufen.'"></ForgotPasswordError>
    <ForgotPasswordSuccess v-if="status === 'success'"></ForgotPasswordSuccess>
  </div>
</template>

<script>
import ForgotPasswordRequest from "@/components/ForgotPasswordRequest.vue";
import ForgotPasswordChange from "@/components/ForgotPasswordChange.vue";
import ForgotPasswordSuccess from "@/components/ForgotPasswordSuccess.vue";
import ForgotPasswordError from "@/components/ForgotPasswordError.vue";

export default {
  name: "ForgotPasswordView",
  components: {ForgotPasswordError, ForgotPasswordSuccess, ForgotPasswordRequest, ForgotPasswordChange},
  props: {
    email: String,
    verificationKey: String
  },
  data() {
    return {
      status: 'request'
    }
  },
  methods: {
    async verifyKey() {
      const response = await fetch(`http://${window.location.hostname}:8080/api/verifyforgotpasswordkey`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
          key: this.verificationKey
        })
      })

      return response.ok
    }
  },
  async mounted() {
    if (this.email && this.verificationKey) {
      if (await this.verifyKey()) {
        this.status = 'change'
      } else {
        this.status = 'error'
      }
    } else {
      this.status = 'request';
    }
  },
  updated() {
    if (!this.email && !this.verificationKey) {
      this.status = 'request';
    }
  }
}
</script>

<style scoped lang="scss">
@import "src/assets/colors";
@import "src/assets/media_query";

.view-container {
  display: flex;
  flex-direction: column;
  text-align: center;
  align-items: center;
  min-height: 100vh;
  background-image: url("../assets/background.png");

  h1 {
    font-size: 1.75rem;
    padding-top: 3rem;
    padding-bottom: 2rem;
  }

  .logo {
    background-color: $white;
    width: 25%;
    margin: 2rem auto 0 auto;
  }
}

@include media-query();
</style>