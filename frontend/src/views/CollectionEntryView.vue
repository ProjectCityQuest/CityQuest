<template>
  <h1>{{ id }}</h1>
</template>

<script>
export default {
  name: "CollectionEntry",
  props: {id: String},
  data(){
    return {

    }
  },
  async mounted(){
    await this.fetchData()
  },
  methods:{
    fetchData() {
      return fetch(`http://${window.location.hostname}:8080/api/getprofilepicture`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'sessionKey': this.getCookie('sessionKey')
        },
        withCredentials: true,
        credentials: 'same-origin'
      }).then(response => response.json())
          .then(data => this.profilePicture = data.image);
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