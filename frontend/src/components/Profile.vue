<template>
  <div class="profile-wrapper">
    <div v-if="editingEnabled" class="profile-edit">
      <nav @click="toggleEdit">
        <svg class="edit-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 50 50">
          <circle cx="25" cy="25" r="25" style="fill:#ED3624"/>
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
            <input type="text" v-model="v$.changedUsername.$model" :class="status(v$.changedUsername)"
                   @input="changeUsername">
            <!-- error message -->
            <div class="input-errors" v-for="(error, index) of v$.changedUsername.$errors" :key="index">
              <div class="error-msg">{{ error.$message }}</div>
            </div>
          </div>
          <div class="email">
            <h2>E-Mail-Adresse</h2>
            <p>{{ getEmail }}</p>
          </div>
        </div>
        <router-link to="kamera" class="image-container">
          <div class="image edit" :style="getProfilePictureStyle">
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
        </router-link>
      </div>
      <div class="submit-container">
        <CQButton @click="updateProfile" b-style="login" :status="state">Speichern</CQButton>
      </div>
      <p v-html="feedback.message" class="feedback-message" :class="feedback.hasError ? 'error' : 'success'"></p>
    </div>
    <div v-else class="profile-overview">
      <nav>
        <svg @click="toggleEdit" class="edit-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 139.81 139.31">
          <path
              d="M8.05,140.17l17.76-3.39a3.61,3.61,0,0,0,1.87-6.1L13.32,116.32a3.61,3.61,0,0,0-6.1,1.87L3.83,136A3.61,3.61,0,0,0,8.05,140.17Z"
              transform="translate(-3.76 -0.92)" style="fill:white"/>
          <path d="M127,49l12.74-12.74a13.09,13.09,0,0,0,0-18.52l-13-13a13.09,13.09,0,0,0-18.52,0L95.5,17.5Z"
                transform="translate(-3.76 -0.92)" style="fill:white"/>
          <path d="M117.5,58.5,86,27,14.75,98.25A29,29,0,0,1,18.5,98,28.5,28.5,0,0,1,47,126.5c0,.89,0,1.76-.13,2.63Z"
                transform="translate(-3.76 -0.92)" style="fill:white"/>
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
          <div class="image" :style="getProfilePictureStyle"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import CQButton from "@/components/CQButton.vue";
import useVuelidate from "@vuelidate/core";
import {required$} from "@/validators";

export default {
  name: "Profile",
  components: {CQButton},
  setup() {
    return {v$: useVuelidate()}
  },
  data() {
    return {
      changedUsername: "",
      userData: {},
      editingEnabled: false,
      state: "inactive",
      feedback: {
        message: "",
        hasError: false
      },
      profilePicture: ""
    }
  },
  validations() {
    return {
      changedUsername: {
        required$
      },
    }
  },
  methods: {
    status(validation) {
      return {
        error: validation.$error,
        dirty: validation.$dirty
      }
    },
    async updateProfile() {
      await this.updateUsername();
      if (sessionStorage.getItem("selectedImage")) {
        await this.updateProfilePicture();
      }

      await this.toggleEdit();
    },
    async updateUsername() {
      const response = await fetch(`http://${window.location.hostname}/api/changeusername`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'sessionKey': this.getCookie('sessionKey')
        },
        withCredentials: true,
        credentials: 'same-origin',
        body: JSON.stringify({
              'username': this.changedUsername,
            }
        )
      }).catch((err) => {
        this.state = "active";
        if (err.message === 'Failed to fetch') {
          this.setError("Verbindung konnte nicht hergestellt werden.");
        } else {
          this.setError("Das hat nicht geklappt ):");
        }
      });

      if (!response) {
        return;
      }

      if (response.ok) {
        this.state = "inactive";
        this.userData.name = this.changedUsername;
      } else {
        let data = await response.json();
        this.setError(data.error);
      }
    },
    async updateProfilePicture() {
      const response = await fetch(`http://${window.location.hostname}/api/changeprofilepicture`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'sessionKey': this.getCookie('sessionKey')
        },
        withCredentials: true,
        credentials: 'same-origin',
        body: JSON.stringify({
              'image': sessionStorage.getItem("selectedImage"),
            }
        )
      }).catch((err) => {
        this.state = "active";
        if (err.message === 'Failed to fetch') {
          this.setError("Verbindung konnte nicht hergestellt werden.");
        } else {
          this.setError("Das hat nicht geklappt ):");
        }
      });

      if (!response) {
        return;
      }

      if (response.ok) {
        this.state = "inactive";

        this.profilePicture = sessionStorage.getItem("selectedImage");
        sessionStorage.removeItem("selectedImage");
      } else {
        let data = await response.json();
        this.setError(data.error);
      }
    },
    setError(message) {
      this.feedback = {
        hasError: true,
        message: message
      };

      setTimeout(this.removeFeedback, 4000)
    },
    removeFeedback() {
      this.feedback = {
        hasError: false,
        message: ""
      };
    },
    async toggleEdit() {
      this.editingEnabled = !this.editingEnabled

      // if editing is closed the image preview is discarded
      if (this.editingEnabled === false) {
        sessionStorage.removeItem("selectedImage");
      }

      await this.fetchUserData();
      this.v$.changedUsername.$model = this.userData.name;
      this.v$.$reset();

      this.removeFeedback();
    },
    fetchUserData() {
      return fetch(`http://${window.location.hostname}/api/getusers`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'sessionKey': this.getCookie('sessionKey')
        },
        withCredentials: true,
        credentials: 'same-origin'
      }).then(response => response.json())
          .then(data => this.userData = data)
    },
    fetchProfilePicture() {
      return fetch(`http://${window.location.hostname}/api/getprofilepicture`, {
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
    },
    changeUsername(event) {
      if (sessionStorage.getItem("selectedImage")) {
        this.state = 'active';
        return;
      }

      if (this.v$.$invalid) {
        this.state = 'inactive';
      } else if (this.userData.name === event.target.value) {
        this.state = 'inactive';
      } else {
        this.state = 'active';
      }
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
    getProfilePictureStyle() {
      if (sessionStorage.getItem("selectedImage") && this.editingEnabled) {
        return `background-image: url(${sessionStorage.getItem("selectedImage")})`;
      }
      return `background-image: url(${this.profilePicture})`;
    }
  },
  async mounted() {
    await this.fetchUserData();
    this.v$.changedUsername.$model = this.userData.name;

    await this.fetchProfilePicture();

    const prevRoute = this.$router.options.history.state.back;

    if (prevRoute.includes("kamera") || prevRoute.includes("galerie")) {
      await this.toggleEdit();
    }

    if (sessionStorage.getItem("selectedImage")) {
      this.state = "active";
    }
  },
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
    padding: .25rem 1rem 0.25rem;
    margin-bottom: 0.5rem;
    background: $gray;

    .edit-icon {
      height: 25px;
      width: 25px;
    }
  }

  .profile {
    margin-top: 1rem;
    display: flex;
    justify-content: space-between;
    width: 100%;

    .user-data-container {
      width: 50%;
      padding-left: 1rem;
      display: flex;
      gap: 10px;
      flex-direction: column;
      justify-content: center;

      h2 {
        font-size: 20px;
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
        border-radius: 5px;
        background: white;
        margin: 0.25rem 0 0.5rem 0;
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

      .error-msg {
        color: $red;
        text-align: left;
      }
    }

    .image-container {
      padding-right: 1rem;
      overflow: hidden;
      display: flex;
      justify-content: center;
      align-items: center;

      .image {
        background-image: url("/src/assets/placeholder_profile.png");
        background-position: center;
        background-size: cover;
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 10px;
        width: 100px;
        height: 100px;

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

  .feedback-message {
    margin-top: 0.5rem;
    text-align: center;

    &.error {
      color: $red;
    }

    &.success {
      color: $dark_green;
    }
  }
}
</style>