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
    <div class="actions-container">
      <router-link class="router-link" :to="`account`">Abbruch</router-link>
      <button class="delete-account" @click="requestAccountDelete()">Account löschen</button>
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

      const rating = await fetch(`http://${window.location.hostname}:8080/api/submitrating`, {
        method: 'POST',
        headers: {
          sessionKey: this.getCookie('sessionKey')
        },
        body:{
          "design": 2,
          "navigation": 3,
          "puzzle": 5,
          "sammelbuch": 1
        },
        withCredentials: true,
        credentials: 'same-origin'
      });

      if (rating.ok) {
        console.log("DANI")
      } else {
        this.deleteAccountError(rating)
      }
      /*
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
      }*/
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
@import "../assets/colors";

.view-container {
  min-height: 100vh;
  background-image: url("../assets/background.png");
  background-position: center;

  h1 {
    font-size: 32px;
    padding: 1rem 1rem;
    text-align: center;
  }

  h2{
    font-size: 1.3rem;
    padding-bottom: 10px;
    text-align: center;
  }

  p{
    margin: 10px auto 10px auto;
    text-align: center;
    width: 60%;
  }

  .rating-container{
    padding-top: 10px;

    .rating{
      padding: 0;
      width: calc(2rem * 5 + 4px);
      margin: 0 auto 0 auto;

      p{
        font-size: 1.3rem;
        text-align: left;
        margin: 0;
      }
    }
  }

  .actions-container {
    margin: 1.5rem auto 0 auto;
    width: 60%;

    button, .router-link {
      font-family: 'Berlin Sans FB', sans-serif;
      display: flex;
      justify-content: center;
      align-items: center;
      text-decoration: none;
      width: 100%;
      height: 2.5rem;
      margin-top: 1rem;
      font-weight: normal;
      border-radius: 10px;
      background: $white;
      color: $dark_gray;
      border: 2px solid $gray;

      &:hover {
        background: $gray;
        color: $white;
      }
    }

    .delete-account {
      color: $dark_red;
      border-color: $dark_red;

      &:hover {
        background: $dark_red;
        color: $white;
      }
    }

  }
}
</style>