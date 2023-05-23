<template>
  <div class="header">
    <div class="heading">
      <h1>Sammelbuch</h1>
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 50 50" class="collection-icon">
        <path
            d="M35.21,11.16H14.51a.78.78,0,0,0-.78.78V38.51a.78.78,0,0,0,.78.79h20.7a.79.79,0,0,0,.79-.79V11.94A.78.78,0,0,0,35.21,11.16ZM31.67,21.28,29.06,18a.59.59,0,0,0-.94,0l-2.62,3.3V13.87h6.17Z"/>
      </svg>
    </div>
    <div class="back" @click="backToMap()">
      <svg xmlns="http://www.w3.org/2000/svg" height="48" viewBox="0 96 960 960" width="48">
        <path d="M400 976 0 576l400-400 56 57-343 343 343 343-56 57Z"/>
      </svg>
      <p>zurück</p>
    </div>
    <div class="hr"></div>
    <div class="entry-container" v-if="requestIsValid" >
      <CollectionNewEntry :spot-id="spotId" :location-name="spot.name"></CollectionNewEntry>
    </div>
    <div class="invalid-container" v-else>
      <p>Du bist nicht in Reichweite oder der Ort existiert nicht.</p>
    </div>
  </div>
</template>

<script>
import * as spotsHelper from "@/spotsHelper";
import * as cameraHelper from "@/cameraHelper";
import CollectionNewEntry from "@/components/CollectionNewEntry.vue";

export default {
  name: "CollectionNewEntryView",
  components: {CollectionNewEntry},
  props: {
    spotId: String
  },
  data() {
    return {
      requestIsValid: true,
      spot: {}
    }
  },
  methods: {
    backToMap() {
      cameraHelper.removeSelectedImage();
      this.$router.push("/karte/ort/" + this.spotId);
    }
  },
  mounted() {
    console.log(spotsHelper.getSpotsInRange(), [spotsHelper.getSpotsInRange()]);
    if (!spotsHelper.getSpotsInRange().includes(this.spotId)) {
      this.requestIsValid = false;
      return;
    }

    spotsHelper.getSpotByID(this.spotId).then(value => this.spot = value);
  }
}
</script>

<style scoped lang="scss">
@import "src/assets/colors";

.header {
  background-color: $white;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  min-height: 100vh;

  .heading {

    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;

    height: 100px;

    h1 {
      font-size: 32px;
    }

    svg {
      height: 45px;
      fill: $orange;
    }
  }

  .back {
    display: flex;
    flex-direction: row;
    height: 1rem;
    margin-left: 10px;
    color: $blue;
    width: fit-content;

    svg {
      height: 1rem;
      width: 1rem;
      fill: $blue;
    }
  }

  .hr {
    width: 100%;
    height: 2px;
    background-color: $light_gray;
    margin-top: 15px;
  }

  .entry-container {
    margin-top: 1rem;
  }
}
</style>