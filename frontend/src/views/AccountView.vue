<template>
  <div class="view-container">
    <Header>
      <h1>Account</h1>
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 50 50">
        <path d="M25,25.59c-7.09,0-12.88,6.44-13.21,14.52H38.21C37.88,32,32.09,25.59,25,25.59Z" transform="translate(0)"
              style="fill:#b41919"/>
        <circle cx="25" cy="16.56" r="6.45" style="fill:#b41919"/>
      </svg>
    </Header>
    <div class="account-wrapper">
      <Profile></Profile>
      <div class="account-actions">
        <router-link class="router-link" to="/passwort-aendern">Passwort ändern</router-link>
        <button @click="logOutOverlayVisible = true">Abmelden</button>
        <router-link class="router-link delete-account" :to="`account-loeschen`">Account löschen</router-link>
      </div>
    </div>
    <Overlay :is-visible="logOutOverlayVisible" @close-overlay="logOutOverlayVisible=false">
      <h1 class="overlay-header">Abmelden</h1>
      <p class="overlay-info">
        Bist du dir sicher, dass du dich abmelden möchtest?
      </p>
      <p class="overlay-info">
        (Dein Puzzlefortschritt und deine Sammelbuch-Einträge bleiben bestehen)
      </p>
      <div class="action-wrapper">
        <button class="overlay-action" @click="requestLogout">Abmelden</button>
      </div>
      <p class="overlay-error">{{ getLogoutError }}</p>
    </Overlay>
    <NavBar :active-icon="1"></NavBar>
  </div>
</template>

<script>
import NavBar from "@/components/NavBar.vue";
import Overlay from "@/components/Overlay.vue";
import Profile from "@/components/Profile.vue";
import Header from "@/components/Header.vue";

export default {
  name: "AccountView",
  components: {NavBar, Overlay, Profile, Header},
  data() {
    return {
      logOutOverlayVisible: false,
      deleteAccountOverlayVisible: false,
      errors: {}
    }
  },
  methods: {
    async requestAccountDelete() {
      const response = await fetch(`http://${window.location.hostname}/api/deleteusers`, {
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
    async requestLogout() {
      const response = await fetch(`http://${window.location.hostname}/api/logout`, {
        method: 'POST',
        headers: {
          sessionKey: this.getCookie('sessionKey')
        },
        withCredentials: true,
        credentials: 'same-origin'
      });

      if (response.ok) {
        this.$router.push("/");
        // deletes cookie
        document.cookie = "sessionKey= ; expires = Thu, 01 Jan 1970 00:00:00 GMT";
      } else {
        this.logoutError(response)
      }
    },
    logoutError(response) {
      if (response.ok) return

      if (response.status === 400) {
        response.json().then(data => this.errors.logout = data.error)
      } else {
        this.errors.logout = 'Das hat nicht geklappt ):'
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
    },

  },
  computed: {
    getDeleteAccountError() {
      return this.errors.deleteAccount;
    },
    getLogoutError() {
      return this.errors.logout;
    }
  }
}
</script>

<style scoped lang="scss">
@import "src/assets/colors";

.view-container {
  background-image: url("../assets/background.png");
  background-position: center;

  .account-wrapper {
    padding-top: 100px;
    height: calc(100vh - 100px);
    display: flex;
    flex-direction: column;
    align-items: center;
    overflow-y: scroll;

    h1 {
      font-size: 32px;
      margin: 1rem 1rem;
    }

    .account-actions {
      width: 60%;
      margin-top: 1rem;

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

  .overlay-header {
    text-align: center;
    font-size: 20px;
    margin-bottom: 1rem;
  }

  .overlay-info {
    font-size: 15px;
    margin-bottom: 0.3rem;
  }

  .action-wrapper {
    display: flex;
    justify-content: center;
    margin-top: 2rem;

    .overlay-action {
      font-family: 'Berlin Sans FB', sans-serif;
      width: 90%;
      height: 2rem;
      font-weight: normal;
      font-size: 15px;
      border-radius: 10px;
      background: $blue;
      color: $white;
      border: none;
    }
  }

  .overlay-error {
    margin: 1rem 0 0.5rem 0;
    text-align: center;
    color: $red;
  }
}
</style>