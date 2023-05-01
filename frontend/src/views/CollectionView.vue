<template>
  <div class="entry-container" v-for="entry in entries">
    <CollectionEntry :id="entry.id" :location="entry.location" :date="entry.date" :text="entry.text" :image="entry.image"></CollectionEntry>
  </div>
  <NavBar :active-icon="4"></NavBar>
</template>

<script>
import NavBar from "@/components/NavBar.vue";
import CollectionEntry from "@/components/CollectionEntry.vue";

export default {
  name: "CollectionView",
  components: {
    NavBar,
    CollectionEntry
  },
  data() {
    return {
      entries: [
        {
          id: 1,
          location: "Zentralfriedhof",
          date: "21.03.2019",
          text: "Der Zentralfriedhof war ein wirklich beeindruckender Ort mit seinen riesigen Gräbern und Gedenkstätten. Es war erstaunlich zu sehen, wie gut die Gräber gepflegt und in Stand gehalten wurden.",
          image: "https://www.geschichtewiki.wien.gv.at/images/3/39/Zentralfriedhof-Vogelschau.jpg"
        },
        {
          id: 2,
          location: "Schafbergbad",
          date: "12.06.2018",
          text: "Das Schafbergbad war eine tolle Möglichkeit, an einem heißen Sommertag abzukühlen. Das Schwimmbecken und die Liegewiese waren beide sehr gepflegt und das Wasser war erfrischend.",
          image: "https://www.wien.gv.at/freizeit/baeder/images/schafbergbad5-gr.jpg"
        },
        {
          id: 3,
          location: "Bezirksmuseum Birgittenau",
          date: "05.09.2020",
          text: "Das Bezirksmuseum Birgittenau war eine interessante Möglichkeit, mehr über die Geschichte des Bezirks zu erfahren. Es gab viele Ausstellungsstücke und informative Texte, die einen Einblick in die Vergangenheit gaben.",
          image: null
        },
        {
          id: 4,
          location: "Theresienbad",
          date: "18.07.2017",
          text: "Das Theresienbad war ein wunderschöner Ort, um zu entspannen und ein Bad zu nehmen. Die alten, traditionellen Gebäude und der Gartenbereich vermittelten eine angenehme Atmosphäre.",
          image: null
        },
        {
          id: 5,
          location: "Burgtheater",
          date: "30.11.2019",
          text: "Das Burgtheater war ein fantastischer Ort für eine Aufführung. Die Architektur und die Innenausstattung waren atemberaubend und die Vorstellung war unglaublich unterhaltsam.",
          image: null
        },
        {
          id: 6,
          location: "Cafe Sacher",
          date: "03.02.2021",
          text: "Das Cafe Sacher war ein gemütlicher Ort für einen Kaffee und ein Stück Sachertorte. Die Atmosphäre war angenehm und das Personal war freundlich und zuvorkommend.",
          image: null
        },
        {
          id: 7,
          location: "Funkhaus Wien",
          date: "19.05.2018",
          text: "Das Funkhaus Wien war ein beeindruckendes Gebäude mit einer faszinierenden Geschichte. Es war interessant, die verschiedenen Studios und Aufnahmeräume zu sehen und mehr über die Arbeit hinter den Kulissen zu erfahren.",
          image: null
        },
        {
          id: 8,
          location: "Zentralfriedhof",
          date: "27.10.2020",
          text: "Der Zentralfriedhof war an einem trüben Herbsttag etwas düster und unheimlich, aber dennoch faszinierend. Es war interessant, die verschiedenen Gräber und Gedenkstätten zu sehen und über die Persönlichkeiten hinter ihnen zu erfahren.",
          image: null
        },
        {
          id: 9,
          location: "Bezirksmuseum Birgittenau",
          date: "14.05.2019",
          text: "Das Bezirksmuseum Birgittenau war eine interessante Entdeckung. Es war toll, mehr über die lokale Geschichte und Kultur zu erfahren. Die Exponate und Ausstellungsstücke waren informativ und ansprechend präsentiert.",
          image: null
        },
        {
          id: 10,
          location: "Theresienbad",
          date: "25.07.2018",
          text: "Das Theresienbad war ein verstecktes Juwel mitten in der Stadt. Die alten Gebäude und der Gartenbereich schafften eine friedliche und entspannende Atmosphäre. Das Wasser im Schwimmbecken war sauber und erfrischend.",
          image: null
        },
        {
          id: 11,
          location: "Cafe Sacher",
          date: "01.12.2021",
          text: "Das Cafe Sacher war ein Genuss für die Sinne. Die Innenausstattung und Atmosphäre waren klassisch und elegant, und der Kaffee und die Sachertorte waren ausgezeichnet. Das Personal war freundlich und aufmerksam.",
          image: null
        }
      ]
    }
  }
}
</script>

<style scoped lang="scss">
@import "src/assets/colors";
.entry-container{
  display: flex;
  flex-direction: column;
  align-items: center;

  *{
    width:90%;
    margin-top: 15px;
  }
}
</style>