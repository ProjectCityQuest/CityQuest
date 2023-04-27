<template>
  <div class="profile-wrapper">
    <div v-if="editingEnabled" class="profile-edit">
      <nav @click="toggleEdit">
        <svg class="edit-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 50 50">
          <circle cx="25" cy="25" r="25" style="fill:#e73829"/>
          <rect x="23" y="9.3" width="4" height="30.41" rx="1.24" transform="translate(24.65 -10.5) rotate(45)"
                style="fill:#f9f9f9"/>
          <rect x="23" y="9.8" width="4" height="30.41" rx="1.24" transform="translate(60.36 25) rotate(135)"
                style="fill:#f9f9f9"/>
        </svg>
      </nav>
      <div class="profile">
        <div class="user-data-container">
          <div class="username">
            <h2>Benutzername</h2>
            <input type="text" :value="getUsername">
          </div>
          <div class="email">
            <h2>E-Mail-Adresse</h2>
            <p>{{ getEmail }}</p>
          </div>
        </div>
        <div class="image-container">
          <div class="image edit">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 139.81 139.31">
              <path
                  d="M8.05,140.17l17.76-3.39a3.61,3.61,0,0,0,1.87-6.1L13.32,116.32a3.61,3.61,0,0,0-6.1,1.87L3.83,136A3.61,3.61,0,0,0,8.05,140.17Z"
                  transform="translate(-3.76 -0.92)"/>
              <path d="M127,49l12.74-12.74a13.09,13.09,0,0,0,0-18.52l-13-13a13.09,13.09,0,0,0-18.52,0L95.5,17.5Z"
                    transform="translate(-3.76 -0.92)"/>
              <path
                  d="M117.5,58.5,86,27,14.75,98.25A29,29,0,0,1,18.5,98,28.5,28.5,0,0,1,47,126.5c0,.89,0,1.76-.13,2.63Z"
                  transform="translate(-3.76 -0.92)"/>
            </svg>
          </div>
        </div>
      </div>
      <div class="submit-container">
        <CQButton b-style="login" status="inactive">Speichern</CQButton>
      </div>
    </div>
    <div v-else class="profile-overview">
      <nav>
        <svg @click="toggleEdit" class="edit-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 139.81 139.31">
          <path
              d="M8.05,140.17l17.76-3.39a3.61,3.61,0,0,0,1.87-6.1L13.32,116.32a3.61,3.61,0,0,0-6.1,1.87L3.83,136A3.61,3.61,0,0,0,8.05,140.17Z"
              transform="translate(-3.76 -0.92)" style="fill:#1d1d1b"/>
          <path d="M127,49l12.74-12.74a13.09,13.09,0,0,0,0-18.52l-13-13a13.09,13.09,0,0,0-18.52,0L95.5,17.5Z"
                transform="translate(-3.76 -0.92)" style="fill:#1d1d1b"/>
          <path d="M117.5,58.5,86,27,14.75,98.25A29,29,0,0,1,18.5,98,28.5,28.5,0,0,1,47,126.5c0,.89,0,1.76-.13,2.63Z"
                transform="translate(-3.76 -0.92)" style="fill:#1d1d1b"/>
        </svg>
      </nav>
      <div class="profile">
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
          <div class="image"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import CQButton from "@/components/CQButton.vue";

export default {
  name: "Profile",
  components: {CQButton},

  data() {
    return {
      userData: {},
      editingEnabled: false
    }
  },
  methods: {
    toggleEdit() {
      this.editingEnabled = !this.editingEnabled
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
    }
  },
  async mounted() {
    await this.fetchData().then(data => this.userData = data)
  }
}
</script>

<style scoped lang="scss">
@import "/src/assets/colors";

.profile-wrapper {
  border-radius: 10px;
  background: $light_gray;
  padding: 0.5rem 0 1rem 0;
  width: 90%;

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

  .profile {
    display: flex;
    justify-content: space-between;
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

      input {
        width: 90%;
        height: 1.5rem;
        border: 1px solid $gray;
        border-radius: 10px;
        background: white;
        margin-bottom: 0.5rem;
        padding-left: 0.5rem;

        &.dirty {
          border-color: $green;

          &:focus {
            outline-color: $green;
          }
        }

        &.error {
          border-color: $red;

          &:focus {
            outline-color: $red;
          }
        }
      }
    }

    .image-container {
      padding-right: 1rem;
      overflow: hidden;

      .image {
        background-image: url("/src/assets/placeholder_profile.png");
        background-size: 120px;
        background-position: center;
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 10px;
        width: 100px;
        height: 100px;
        object-fit: cover;

        svg {
          fill: white;
          width: 35px;
          height: 35px;
        }
      }
    }
  }

  .submit-container {
    margin: 1rem auto 0 auto;
    width: 80%;
  }
}
</style>