<template>
  <div class="view-container">
    <h1>Account löschen</h1>
    <p>Wir finden es sehr schade, du deinen Account und alle damit verbunden Daten löschen möchtest.</p>
    <p>Bitte nimm dir noch kurz Zeit und hilf uns, die App für andere User zu verbessern.</p>
    <div class="rating-container">
      <div class="rating">
        <p>Design</p>
        <Rating @changedRating="x => updateRating(0,x)"></Rating>
      </div>
      <div class="rating">
        <p>Navigation</p>
        <Rating @changedRating="x => updateRating(1,x)"></Rating>
      </div>
      <div class="rating">
        <p>Puzzle</p>
        <Rating @changedRating="x => updateRating(2,x)"></Rating>
      </div>
      <div class="rating">
        <p>Sammelbuch</p>
        <Rating @changedRating="x => updateRating(3,x)"></Rating>
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
      ratings: [0,0,0,0],
      errors: {}
    }
  },
  methods: {
    updateRating(index, rating){
      this.ratings[index] = rating
    },
    async requestAccountDelete() {
      const rating = await fetch(`http://${window.location.hostname}:8080/api/submitrating`, {
        method: 'POST',
        headers: {
          sessionKey: this.getCookie('sessionKey'),
          'Content-Type': 'application/json'
        },
        body:  JSON.stringify({
          design: this.ratings[0],
          navigation: this.ratings[1],
          puzzle: this.ratings[2],
          sammelbuch: this.ratings[3]
        }),
        withCredentials: true,
        credentials: 'same-origin'
      });

      if (rating.ok) {
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
      } else {
        this.deleteAccountError(rating)
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
@import "src/assets/colors";
@import "src/assets/media_query";

.view-container {
  min-height: 100vh;
  background-image: url("../assets/background.png");
  background-position: center;

  &:last-child{
    padding-bottom: 1.5rem;
  }

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

@include media-query();
</style>