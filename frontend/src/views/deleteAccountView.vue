<template>
  <div class="view-container">
    <h1>Mein Account</h1>
    <h2>Account löschen</h2>
    <p>Wir finden es sehr schade, du deinen Account löschen möchtest.</p>
    <p>Bitte nimm dir noch kurz Zeit und hilf uns, die App für andere User zu verbessern.</p>
    <div class="rating-container">
      <div class="rating">
        <p>Design</p>
        <Rating></Rating>
      </div>
      <div class="rating">
        <p>Navigation</p>
        <Rating></Rating>
      </div>
      <div class="rating">
        <p>Puzzle</p>
        <Rating></Rating>
      </div>
      <div class="rating">
        <p>Sammelbuch</p>
        <Rating></Rating>
      </div>
    </div>
  </div>
</template>


<script>
import Rating from "@/components/Rating.vue";
export default {
  name: "deleteAccountView",
  components: {Rating},
  data() {
    return {
      logOutOverlayVisible: false,
      deleteAccountOverlayVisible: false,
      userData: {},
      errors: {}
    }
  },
  methods: {
    async requestAccountDelete() {
      const response = await fetch(`http://${window.location.hostname}:8080/api/deleteusers`, {
        method: 'DELETE',
        headers: {
          sessionKey: this.getCookie('sessionKey')
        },
        withCredentials: true,
        credentials: 'same-origin'
      });

      if (response.ok) {
        this.$router.push("/")
        // deletes cookie
        document.cookie = "sessionKey= ; expires = Thu, 01 Jan 1970 00:00:00 GMT"
      } else {
        this.deleteAccountError(response)
      }
    },
    deleteAccountError(response) {
      if (response.ok) return
      if (response.status === 400) {
        response.json().then(data => this.errors.deleteAccount = data.error)
      } else {
        this.errors.deleteAccount = 'Das hat nicht geklappt ):'
      }
    },
    getCookie(cname) {
      let name = cname + "=";
      let decodedCookie = decodeURIComponent(document.cookie);
      let ca = decodedCookie.split(';');
      for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') {
          c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
          return c.substring(name.length, c.length);
        }
      }
      return false;
    }
  }
}
</script>

<style scoped lang="scss">
.view-container {
  min-height: 100vh;
  background-image: url("../assets/background.png");
  background-position: center;

  h1 {
    font-size: 32px;
    padding: 1rem 1rem;
    text-align: center;
  }
}
</style>