<template>
  <div class="view-container">
    <svg class="logo" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 226.57 277.17">
      <g id="f8d16813-69cc-40c1-944c-750c609b7c90" data-name="Ebene 2">
        <path
            d="M42.8,185.22a1,1,0,0,0,0,1.52c5.93,4.95,38.5,32.7,34.93,41.94S57.18,232,50.24,239c-10.37,10.36-.35,22.76,5.22,26.64,17.79,12.4,23.46,2,24.91.11,3.4-4.5,2-14.22,13.69-21.76,11.07-7.14,43.83,27.44,46.35,30.14a.28.28,0,0,0,.35,0C144,272,187.43,242.3,181,229.83c-4.74-9.14-20.3-1-27.69-10.53-5.67-7.29,4.84-18.33,9.38-22.36,10.84-9.61,20.51-7.53,26.25-4.18,11.77,6.86.86,17,11.73,22.72,8.84,4.61,41.32-28.13,46.89-33.85a1,1,0,0,0-.1-1.56c-6-4.79-39.85-32.25-32.75-42.53,7.39-10.7,17-1.72,23.36-6,18.3-12.32,4.26-25.35-1.63-29.36-5.57-3.79-18.85-9.94-26.89,1.28-3.4,4.74-1,20.47-10.37,21.89C183.5,127.73,149.5,98,142.87,92a1.4,1.4,0,0,0-2.06.2c-6.76,8.88-48.63,62.87-63.43,55.94-10.09-4.73-3-17.07-13.73-24.76-4.72-3.4-14-5.52-23,3.5-12.47,12.48-12.74,18-9.94,23.21,7.87,14.63,18.25-1.46,26.52,12.54C60.94,168.93,46.6,181.94,42.8,185.22Z"
            transform="translate(-25.24 -1.02)" style="fill:none;stroke:#969696;stroke-miterlimit:10;stroke-width:8px"/>
      </g>
      <g id="a85a94cf-0ebe-46c7-bb29-2d7381f4baaf" data-name="Ebene 3">
        <path
            d="M134.71,5c-21.22-.3-62,14.13-62,58.05,0,45.08,62,126.31,62,126.31s59.58-80.16,59.58-125.29C194.29,17.34,155.69,5.31,134.71,5Zm-.39,84.16a26.93,26.93,0,1,1,26.93-26.93A26.93,26.93,0,0,1,134.32,89.18Z"
            transform="translate(-25.24 -1.02)"
            style="fill:#ed3524;stroke:#b41415;stroke-miterlimit:10;stroke-width:8px"/>
        <circle cx="109.08" cy="61.23" r="26.93"
                style="fill:none;stroke:#b41415;stroke-miterlimit:10;stroke-width:8px"/>
      </g>
    </svg>
    <div class="info-container">
      <h1>CityQuest</h1>
      <p>Erkunde die Stadt und sammle unvergessliche Erinnerungen!</p>

      <carousel class="carousel" :wrap-around="true">
        <slide v-for="(index, slide) in 3" :key="index">
          <div class="slide-container">
            <img class="carousel-image" :src="slides[index-1]">
            <p>{{ comments[index - 1] }}</p>
          </div>
        </slide>
        <template #addons>
          <navigation/>
          <pagination/>
        </template>
      </carousel>
      <CQButton class="button" @click="toRegistration()" b-style="login" :status="registerState">Registrieren</CQButton>
      <CQButton class="button" @click="toLogin()" b-style="login" :status="loginState">Login</CQButton>
    </div>
  </div>
</template>

<script>
import CQButton from "@/components/CQButton.vue";
import router from "@/router";

import 'vue3-carousel/dist/carousel.css';
import {Carousel, Slide, Pagination, Navigation} from 'vue3-carousel';

export default {
  name: "LandingView",
  components: {
    CQButton,
    Carousel,
    Slide,
    Pagination,
    Navigation,
  },
  data() {
    return {
      registerState: "active",
      loginState: "active",
      slides: [
        "/slides/slide_1.png",
        "/slides/slide_2.png",
        "/slides/slide_3.png",
      ],
      comments: [
        "Lerne neue Orte kennen",
        "Vervollständige das Puzzle",
        "Halte deine Erinnerungen fest"
      ]
    }
  },
  methods: {
    toLogin() {
      router.push("/login")
    },
    toRegistration() {
      router.push("/registrierung")
    },
  }
}
</script>

<style scoped lang="scss">
@import "src/assets/colors";
@import "src/assets/media_query";

.view-container {
  background-image: url("../assets/background.png");
  min-height: 100vh;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;

  .logo {
    width: 25%;
    margin: 2rem auto 0 auto;
  }

  .info-container {
    width: 80%;
    text-align: center;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 1rem;
    margin-bottom: 1rem;

    h1 {
      font-size: 1.75rem;
      padding-top: 0.5rem;
      padding-bottom: 0.5rem;
    }

    p {
      text-align: center;
      font-size: 1.1rem;
    }

    .button {
      width: 75%;
    }

    .carousel {
      width: 100%;

      .slide-container {
        display: flex;
        flex-direction: column;
        border: solid 3px $gray;
        border-radius: 10px;
        width: 75%;

        .carousel-image {
          width: 100%;
          border-top-left-radius: 10px;
          border-top-right-radius: 10px;
        }

        p {
          padding: 10px;
          background-color: $white;
          border-bottom-left-radius: 10px;
          border-bottom-right-radius: 10px;
        }
      }
    }
  }
}

@include media-query() {
  .info-container {
    .carousel {
      width: 100%;
    }
  }
}
</style>