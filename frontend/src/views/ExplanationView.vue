<template>
  <div class="view-container">
    <div class="info-container">
      <div class="header" ref="header">
        <h1>Einführung</h1>
      </div>
      <carousel class="carousel" :wrap-around="true">
        <slide v-for="(index, slide) in 7" :key="index">
          <div class="slide-container">
            <img class="carousel-image" :src="slides[index-1]" :style="{ height: imageHeight }">
            <p ref="explanation">{{ comments[index - 1] }}</p>
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
      imageHeight: "0px",
      registerState: "active",
      loginState: "active",
      slides: [
        "/tour/slide_1.png",
        "/tour/slide_2.png",
        "/tour/slide_3.png",
        "/tour/slide_4.png",
        "/tour/slide_5.png",
        "/tour/slide_6.png",
        "/tour/slide_7.png",
      ],
      comments: [
        "Die Karte ist unsere Startseite. Hier siehst du alle Spots und deinen Standort. Über den Kreis links oben kannst du an deine Position heranzoomen.",
        "Wenn du auf einen Spot innerhalb deines Radius klickst hast du die Möglichkeit, das Puzzle-Teil einzusammeln und einen Sammelbucheintrag zu schreiben.",
        "Hast du ein Puzzle-Teil eingesammelt wird es dir auf deiner Puzzle-Seite hervorgehoben und angezeigt.",
        "Um an Infos zu einem Puzzle-Teil zu kommen genügt ein Klick auf das jeweilige Teil. Dann werden Name, Status und ein Link zur Karte angezeigt.",
        "Deine geschriebenen Einträge findest du auf der Sammelbuch-Seite. Du kannst nach Textstellen und Spotnamen suchen und die Suchergebnisse nach belieben sortieren.",
        "Nähere Infos zu deinem Account und Möglichkeiten zum Passwort ändern und abmelden findest du auf der Account-Seite ganz links im Menü.",
        "Dort kannst du auch deinen Benutzernamen und dein Profilbild ändern.",
      ]
    }
  },
  methods: {
    toMap() {
      router.push("/karte")
    },
  },
  mounted() {
    this.$nextTick(() => {
      const headerHeight = this.$refs.header.offsetHeight;
      let textHeight = 0;
      console.log(headerHeight)
      this.$refs.explanation.forEach(x=>{
        textHeight = Math.max(textHeight,x.offsetHeight)
      })
      this.imageHeight = `calc(100vh - ${headerHeight}px - ${textHeight}px - 7rem)`;
    });
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
    margin-bottom: 1rem;

    .header{

      h1 {
        font-size: 1.75rem;
        padding-top: 1.5rem;
        margin-bottom: -1rem;
      }

      p {
        text-align: center;
        font-size: 1.1rem;
      }
    }

    .button {
      width: 75%;
    }

    .carousel {
      width:75%;

      .slide-container {
        display: flex;
        flex-direction: column;
        width:75%;

        .carousel-image {
          width:100%;
          border: solid 3px $gray;
          border-radius: 10px;
        }

        p {
          padding: 10px;
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