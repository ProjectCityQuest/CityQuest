<template>
  <div class="header">
    <div class="heading" @click="this.sortingOverlay=false">
      <h1>Sammelbuch</h1>
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 50 50" class="collection-icon">
        <path
            d="M35.21,11.16H14.51a.78.78,0,0,0-.78.78V38.51a.78.78,0,0,0,.78.79h20.7a.79.79,0,0,0,.79-.79V11.94A.78.78,0,0,0,35.21,11.16ZM31.67,21.28,29.06,18a.59.59,0,0,0-.94,0l-2.62,3.3V13.87h6.17Z"/>
      </svg>
    </div>
    <div class="back" @click="backToCollection()">
      <svg xmlns="http://www.w3.org/2000/svg" height="48" viewBox="0 96 960 960" width="48">
        <path d="M400 976 0 576l400-400 56 57-343 343 343 343-56 57Z"/>
      </svg>
      <p>zurück</p>
    </div>
    <div class="hr"></div>
  </div>
  <div class="entry-container">
    <div class="entry">
      <div class="top">
        <h1>{{ location }}</h1>
        <p>Besucht am: {{ timestampFormatted }}</p>
      </div>
      <div class="hr"></div>
      <div class="body imgHor">
        <img :src="image">
        <p>{{ text }}</p>
      </div>
    </div>
  </div>
  <NavBar :active-icon="4"></NavBar>
</template>

<script>
import router from "@/router";
import NavBar from "@/components/NavBar.vue";

export default {
  name: "CollectionEntry",
  props: {id: String},
  components: {NavBar},
  data() {
    return {
      image: null,
      location: "",
      text: "",
      timestamp: "",
    }
  },
  async mounted() {
    await this.fetchData()
  },
  methods: {
    fetchData() {
      return fetch(`http://${window.location.hostname}:8080/api/getentry`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'sessionKey': this.getCookie('sessionKey')
        },
        body: JSON.stringify({
          id: this.id
        }),
        withCredentials: true,
        credentials: 'same-origin'
      }).then(response => response.json())
          .then(data => {
            this.image = data.entry.image
            this.location = data.entry.location
            this.text = data.entry.text
            this.timestamp = data.entry.timestamp
          })
          .catch((error) => router.push("/sammelbuch"));
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
    },
    backToCollection() {
      router.push("/sammelbuch")
    }
  },
  computed: {
    timestampFormatted() {
      return (this.timestamp.split("T")[0]+"").split("-").reverse().join(".") + ", " + (this.timestamp.split("T")[1]+"").substring(0,5) + " Uhr"
    }
  }
}
</script>

<style scoped lang="scss">
@import "src/assets/colors";

.header {
  background-color: $white;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;

  .heading {

    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;

    height: 100px;

    h1 {
      font-size: 32px;
    }

    svg {
      height: 45px;
      fill: $orange;
    }
  }

  .back {
    display: flex;
    flex-direction: row;
    height: 1rem;
    margin-left: 10px;
    color: $blue;

    svg {
      height: 1rem;
      width: 1rem;
      fill: $blue;
    }
  }

  .hr {
    width: 100%;
    height: 2px;
    background-color: $light_gray;
    margin-top: 15px;
  }
}

.entry-container {
  margin-top: calc(117px + 1rem);
  width: 100%;
  height: calc(100vh - 135px - 1rem);
  background-image: url("../assets/background.png");
  padding-top: 15px;

  .entry {
    width: 90%;
    background-color: #eaeaea;
    border-radius: 10px;
    margin: 0 auto 0 auto;

    .top {
      padding: 15px;

      h1 {
        text-align: center;
        font-size: 1.3rem;
      }

      p {
        padding-top: 10px;
        font-size: 0.95rem;
      }
    }

    .hr {
      width: 100%;
      height: 2px;
      background-color: #c9c9c9;
      margin-top: 10px;
    }

    .body{

      img{
        object-fit: cover;
        border-radius: 5px;
      }

      p{
        font-size: 0.95rem;
      }

      &.imgHor{
        display: flex;
        flex-direction: column;
        gap: 15px;
        padding: 15px;
      }
    }
  }
}
</style>