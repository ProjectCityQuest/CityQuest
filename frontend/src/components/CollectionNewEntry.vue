<template>
  <div class="entry-container">
    <div class="entry">
      <div class="top">
        <h1>{{ getLocationName }}</h1>
        <p>Besucht am: {{ getDateNowFormatted }}</p>
      </div>
      <div class="hr"></div>
      <div>
        <img>
        <router-link to="/kamera/" class="image-container">
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
        <div class="username">
          <h2>Benutzername</h2>
          <input type="text" v-model="v$.changedUsername.$model" :class="status(v$.changedUsername)"
                 @input="changeUsername">
          <!-- error message -->
          <div class="input-errors" v-for="(error, index) of v$.changedUsername.$errors" :key="index">
            <div class="error-msg">{{ error.$message }}</div>
          </div>
        </div>
        <p>{{ }}</p>
      </div>
    </div>
    <div class="spacer"></div>
  </div>

</template>

<script>

import useVuelidate from "@vuelidate/core";
import {required$} from "@/validators";

export default {
  name: "CollectionNewEntry",
  props: {
    spotId: String,
    locationName: String
  },
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
      const response = await fetch(`http://${window.location.hostname}:8080/api/changeusername`, {
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
      const response = await fetch(`http://${window.location.hostname}:8080/api/changeprofilepicture`, {
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
      return fetch(`http://${window.location.hostname}:8080/api/getusers`, {
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
    },
    getLocationName() {
      return this.locationName;
    },
    getDateNowFormatted() {
      let date = new Date(Date.now());

      return `${String(date.getUTCDate()).padStart(2, "0")}.${String(date.getMonth() + 1).padStart(2, "0")}.
        ${date.getFullYear()}, ${String(date.getHours()).padStart(2, "0")}:${String(date.getMinutes()).padStart(2, "0")}`;
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
  }
}
</script>

<style scoped lang="scss">
@import "src/assets/colors";

.entry-container {
  display: flex;
  flex-direction: column;
  align-items: center;

  .entry {
    width: 90%;
    background-color: #eaeaea;
    border-radius: 10px;
    margin: 0 auto 0 auto;

    .top {
      padding: 15px;
      text-align: center;

      h1 {
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

    .body {
      display: flex;
      gap: 15px;
      padding: 15px;

      img {
        object-fit: cover;
        border-radius: 5px;
      }

      p {
        font-size: 0.95rem;
        text-align: justify;
        hyphens: auto;
      }

      &.img-hor {
        flex-direction: column;
      }

      &.img-vert {
        flex-direction: row-reverse;

        img {
          width: 50%;
        }
      }
    }
  }

  button {
    width: 60%;
    margin-top: 15px;
  }

  .delete {
    text-align: center;
    margin-top: 15px;
    text-decoration: underline;
    color: $dark_gray;
  }

  .spacer {
    height: 80px;
    width: 100%;
  }
}
</style>