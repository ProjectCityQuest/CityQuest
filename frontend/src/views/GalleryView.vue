<template>
  <div class="header-wrapper">
    <svg @click="backToCamera()" xmlns="http://www.w3.org/2000/svg" height="48" viewBox="0 96 960 960" width="48">
      <path d="M400 976 0 576l400-400 56 57-343 343 343 343-56 57Z"/>
    </svg>
    <p>1 Bild auswählen</p>
  </div>
  <div class="image-container">
    <div class="image" v-for="(image, index) in gallery" :key="index" :style="{height: imageHeight}"
         @click="selectedImage = index">
      <div v-if="selectedImage === index" class="cover" :style="{height: imageHeight}">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 96 960 960">
          <path d="M378 810 154 586l43-43 181 181 384-384 43 43-427 427Z"/>
        </svg>
      </div>
      <img :id="index" :src="image" :style="{height: imageHeight}"/>
    </div>
  </div>
  <div class="footer-wrapper">
    <CQButton b-style="login" :status="buttonStatus" @click="safeImage()">Auswählen</CQButton>
  </div>
</template>

<script>
import router from '@/router'
import CQButton from "@/components/CQButton.vue";

export default {
  name: "GalleryView",
  props: {
    source: String
  },
  data() {
    return {
      buttonStatus: "inactive",
      selectedImage: null,
      gallery: JSON.parse(sessionStorage.getItem('gallery')) || []
    }
  },
  components: {
    CQButton
  },
  computed: {
    imageHeight() {
      return (window.innerWidth * 0.45) + "px"
    }
  },
  methods: {
    backToCamera() {
      router.push('/kamera/' + this.source)
    },
    safeImage() {
      this.buttonStatus = "waiting"
      let gallery = JSON.parse(sessionStorage.getItem('gallery')) || []
      sessionStorage.setItem("selectedImage", gallery[gallery.length - 1])
      sessionStorage.removeItem("gallery")
      router.push('/' + this.source)
    }
  },
  watch: {
    'selectedImage'() {
      this.buttonStatus = this.selectedImage === null ? 'inactive' : 'active'
    }
  }
}
</script>

<style scoped lang="scss">
@import "src/assets/colors.scss";

.header-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 70px;
  background-color: #eaeaea;
  z-index: 9999;

  display: grid;
  grid-template-columns: 55px 4fr 55px;
  grid-template-rows: 1fr;
  gap: 0 0;
  grid-template-areas: ". . .";

  svg {
    grid-column: 1;
    margin: auto 0 auto 10px;
    height: 35px;
    fill: $blue;
  }

  p {
    grid-column: 2;
    text-align: center;
    margin: auto 0 auto 0;
    font-size: 1.2rem;
  }
}

.image-container {
  width: 100%;
  margin-top: 70px;
  height: calc(100vh - 140px);
  background-color: $white;
  overflow-y: scroll;

  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  align-content: flex-start;
  justify-content: space-around;

  .image {
    width: 45%;
    margin-top: 10px;
    margin-bottom: 7px;
    position: relative;
    border-radius: 10px;
    border: solid $light_gray;

    img {
      height: 100%;
      width: 100%;
      object-fit: cover;
      padding: 0;
      margin: 0;
      box-sizing: border-box;
      border-radius: 6px;
    }
  }

  .cover {
    position: absolute;
    background-color: $black;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    opacity: 0.5;
    z-index: 1;
    border-radius: 6px;

    display: flex;
    justify-content: center;
    align-items: center;

    svg {
      fill: $white;
      width: 60px;
      height: 60px;
      opacity: 1;
      z-index: 2;
    }
  }
}

.footer-wrapper {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 70px;
  background-color: $white;
  z-index: 9999;
  box-shadow: 0px -5px 80px 0px rgba(0, 0, 0, 0.75);

  display: flex;
  justify-content: center;
  align-items: center;

  * {
    width: 60%;
  }
}
</style>