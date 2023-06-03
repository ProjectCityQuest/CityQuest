<template>
  <div class="view-container">
    <div class="info-container">
      <h1>Einführung</h1>
      <p>Eine kurze Tour durch CityQuest, um dir die Funktionen näher zu bringen:</p>

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
      <CQButton class="button" @click="toMap()" b-style="login" :status="registerState">Einführung beenden</CQButton>
    </div>
  </div>
</template>

<script>
import CQButton from "@/components/CQButton.vue";
import router from "@/router";

import 'vue3-carousel/dist/carousel.css';
import {Carousel, Slide, Pagination, Navigation} from 'vue3-carousel';

export default {
  name: "ExplanationView",
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
    toMap() {
      router.push("/karte")
    },
  }
}
</script>

<style scoped lang="scss">
@import "src/assets/colors";

.view-container {
  background-image: url("../assets/background.png");
  min-height: 100vh;
  width: 100vw;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;

  svg {
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
      width:100vw;

      .slide-container {
        display: flex;
        flex-direction: column;
        border: solid 3px $gray;
        border-radius: 10px;
        width:70vw;

        .carousel-image {
          width:100%;
          max-height:124vw;
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
</style>