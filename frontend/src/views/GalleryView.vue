<template>
  <div class="header-wrapper">
    <svg @click="backToCamera()" xmlns="http://www.w3.org/2000/svg" height="48" viewBox="0 96 960 960" width="48">
      <path d="M400 976 0 576l400-400 56 57-343 343 343 343-56 57Z"/>
    </svg>
    <p>1 Bild auswählen</p>
  </div>
  <div>
    <div v-for="(image, index) in gallery" :key="index">
      <img :src="image"/>
    </div>
  </div>
  <div class="footer-wrapper">

  </div>
</template>

<script>
import router from '@/router'
export default {
  name: "GalleryView",
  computed: {
    gallery() {
      return this.$store.state.gallery
    }
  },
  methods: {
    backToCamera(){
      router.push('/kamera')
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
  background-color: $white;
  z-index: 9999;

  display: grid;
  grid-template-columns: 55px 4fr 55px;
  grid-template-rows: 1fr;
  gap: 0 0;
  grid-template-areas: ". . .";

  svg{
    grid-column: 1;
    margin: auto 0 auto 10px;
    height: 35px;
    fill: $blue;
  }

  p{
    grid-column: 2;
    text-align: center;
    margin: auto 0 auto 0;
    font-size: 1.2rem;
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
}
</style>