<template>

</template>


<script>
import NavBar from "@/components/NavBar.vue";
import Overlay from "@/components/Overlay.vue";

export default {
  name: "deleteAccountView",
  components: {},
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

<style scoped>

</style>