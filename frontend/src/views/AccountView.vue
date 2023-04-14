<template>
  <div class="view-container">
    <div class="account-wrapper">
      <h1>Mein Account</h1>
      <div class="profile-settings">
        <nav>
          <svg class="edit-icon" xmlns="http://www.w3.org/2000/svg"
               viewBox="0 0 139.81 139.31">
            <path
                d="M8.05,140.17l17.76-3.39a3.61,3.61,0,0,0,1.87-6.1L13.32,116.32a3.61,3.61,0,0,0-6.1,1.87L3.83,136A3.61,3.61,0,0,0,8.05,140.17Z"
                transform="translate(-3.76 -0.92)" style="fill:#1d1d1b"/>
            <path d="M127,49l12.74-12.74a13.09,13.09,0,0,0,0-18.52l-13-13a13.09,13.09,0,0,0-18.52,0L95.5,17.5Z"
                  transform="translate(-3.76 -0.92)" style="fill:#1d1d1b"/>
            <path d="M117.5,58.5,86,27,14.75,98.25A29,29,0,0,1,18.5,98,28.5,28.5,0,0,1,47,126.5c0,.89,0,1.76-.13,2.63Z"
                  transform="translate(-3.76 -0.92)" style="fill:#1d1d1b"/>
          </svg>
        </nav>
        <div class="edit-profile">
          <div class="user-data-container">
            <div class="username">
              <h2>Benutzername</h2>
              <p>{{ getUsername }}</p>
            </div>
            <div class="email">
              <h2>E-Mail-Adresse</h2>
              <p>{{ getEmail }}</p>
            </div>
          </div>
          <div class="image-container">
            <img class="profile-picture" src="../assets/placeholder_profile.png" alt="Profilbild">
          </div>
        </div>
      </div>
      <div class="account-actions">
        <router-link class="router-link" to="passwort-aendern">Passwort ändern</router-link>
        <button @click="logOutOverlayVisible = true">Abmelden</button>
        <button class="delete-account" @click="deleteAccountOverlayVisible = true">Account löschen</button>
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
    <Overlay :is-visible="deleteAccountOverlayVisible" @close-overlay="deleteAccountOverlayVisible=false">
      <h1 class="overlay-header">Account löschen</h1>
      <p class="overlay-info">
        Bist du dir sicher, dass du deinen Account löschen möchtest?
      </p>
      <p class="overlay-info">
        Dein Puzzlefortschritt und deine Sammelbuch-Einträge gehen verloren.
      </p>
      <div class="action-wrapper">
        <button class="overlay-action" @click="requestAccountDelete">Account löschen</button>
      </div>
      <p class="overlay-error">{{ getDeleteAccountError }}</p>
    </Overlay>
    <NavBar :active-icon="1"></NavBar>
  </div>
</template>

<script>
import NavBar from "@/components/NavBar.vue";
import Overlay from "@/components/Overlay.vue";

export default {
  name: "AccountView",
  components: {NavBar, Overlay},
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
    async requestLogout() {
      const response = await fetch(`http://${window.location.hostname}:8080/api/logout`, {
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
    fetchData() {
      return fetch(`http://${window.location.hostname}:8080/api/getusers`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'sessionKey': this.getCookie('sessionKey')
        },
        withCredentials: true,
        credentials: 'same-origin'
      }).then(response => response.json())
    }
  },
  computed: {
    getUsername() {
      return this.userData.name;
    },
    getEmail() {
      return this.userData.email;
    },
    getDeleteAccountError() {
      return this.errors.deleteAccount;
    },
    getLogoutError() {
      return this.errors.logout;
    }
  },
  async mounted() {
    await this.fetchData().then(data => this.userData = data)
  }
}
</script>

<style scoped lang="scss">
@import "../assets/colors";

.view-container {
  min-height: 100vh;
  background-image: url("../assets/background.png");
  background-position: center;

  .account-wrapper {
    display: flex;
    flex-direction: column;
    align-items: center;

    h1 {
      font-size: 32px;
      margin: 1rem 1rem;
    }

    .profile-settings {
      width: 90%;
      border-radius: 10px;
      background: $light_gray;
      padding: 0.5rem 0 1rem 0;

      nav {
        display: flex;
        justify-content: flex-end;
        padding-right: 1rem;
        margin-bottom: 0.5rem;

        .edit-icon {
          height: 25px;
          width: 25px;
        }
      }

      .edit-profile {
        display: flex;
        width: 100%;

        .user-data-container {
          width: 50%;
          padding-left: 1rem;

          h2 {
            font-size: 20px;
            margin-top: 1rem;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
          }

          p {
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
          }
        }

        .image-container {
          width: 50%;
          padding-right: 1rem;
          display: flex;
          justify-content: center;
          align-items: center;
          overflow: hidden;

          .profile-picture {
            border-radius: 10px;
            width: 100px;
            height: 100px;
            object-fit: cover;
          }
        }
      }
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