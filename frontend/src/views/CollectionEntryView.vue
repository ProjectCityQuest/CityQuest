<template>
  <h1>{{ id }}</h1>
</template>

<script>
import router from "@/router";
export default {
  name: "CollectionEntry",
  props: {id: String},
  data(){
    return {
      image: null,
      location: "",
      text: "",
      timestamp: "",
    }
  },
  async mounted(){
    await this.fetchData()
  },
  methods:{
    fetchData() {
      return fetch(`http://${window.location.hostname}:8080/api/getentry`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'sessionKey': this.getCookie('sessionKey')
        },
        body: JSON.stringify({
          id : this.id
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
    }
  }
}
</script>

<style  lang="scss">
@import "src/assets/colors";

</style>