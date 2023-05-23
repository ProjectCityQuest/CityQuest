<template>
  <div class="entry-container">
    <div class="entry">
      <div class="top">
        <h1>{{ getLocationName }}</h1>
        <p>Besucht am: {{ getDateNowFormatted }}</p>
      </div>
      <div class="hr"></div>
      <form @submit.prevent>
        <div class="cover-image-container portrait">
          <img v-if="coverImage" :src="coverImage" alt="Sammelbuchbild" class="cover-image">
          <div class="action-container">
            <CQButton b-style="login" status="active" @click="toCamera" class="take-picture">Bild aufnehmen</CQButton>
            <CQButton b-style="login" status="active" @click="removeImage" class="remove-image">
              <svg xmlns="http://www.w3.org/2000/svg" fill="white" width="24" height="24" viewBox="0 96 960 960">
                <path
                    d="M261 936q-24.75 0-42.375-17.625T201 876V306h-41v-60h188v-30h264v30h188v60h-41v570q0 24-18 42t-42 18H261Zm438-630H261v570h438V306ZM367 790h60V391h-60v399Zm166 0h60V391h-60v399ZM261 306v570-570Z"/>
              </svg>
            </CQButton>
          </div>
        </div>
        <div class="experience-container">
          <label>Deine Erfahrung:</label>
          <textarea v-model="userInput" class="entry-text"></textarea>
        </div>
        <CQButton b-style="login" status="inactive">Absenden</CQButton>
      </form>
    </div>
    <div class="spacer"></div>
  </div>
</template>

<script>

import CQButton from "@/components/CQButton.vue";
import router from "@/router";
import * as cameraHelper from "@/cameraHelper";

export default {
  name: "CollectionNewEntry",
  components: {CQButton},
  props: {
    spotId: String,
    locationName: String
  },
  data() {
    return {
      userData: {},
      state: "inactive",
      feedback: {
        message: "",
        hasError: false
      },
      userInput: "",
      coverImage: ""
    }
  },
  validations() {
    return {}
  },
  methods: {
    status(validation) {
      return {
        error: validation.$error,
        dirty: validation.$dirty
      }
    },
    toCamera() {
      router.push("/kamera");
    },
    removeImage() {
      this.coverImage = "";
      cameraHelper.removeSelectedImage();
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
    if (sessionStorage.getItem("selectedImage")) {
      this.state = "active";
    }

    this.coverImage = cameraHelper.getSelectedImage();
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
    height: 70vh;
    background-color: #eaeaea;
    border-radius: 10px;
    margin: 0 auto 0 auto;
    overflow: scroll;

    form {
      width: 100%;
      height: fit-content;
      overflow: scroll;
      display: flex;
      flex-direction: column;
      align-items: center;

      .cover-image-container {
        display: flex;
        flex-direction: column;
        width: calc(100% - 20px);
        margin-top: 20px;

        .cover-image {
          border-radius: 5px;
        }

        .action-container {
          display: flex;
          gap: 10px;

          .take-picture {
            width: 75%;
          }

          .remove-image {
            width: 25%;
          }
        }
      }

      .experience-container {
        width: calc(100% - 20px);
        margin-top: 1em;
        display: flex;
        flex-direction: column;
        gap: 10px;

        .entry-text {
          padding: 0.5em;
          border: silver 1px solid;
          border-radius: 5px;
        }
      }

      &:last-child {
        margin-bottom: 1em;
      }
    }

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