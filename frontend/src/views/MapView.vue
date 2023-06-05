<template>
  <div class="view-container">
    <Map :location-request="this.spotId"></Map>
    <NavBar :active-icon="2"></NavBar>
  </div>
</template>

<script>
import NavBar from "@/components/NavBar.vue";
import Map from "@/components/Map.vue";

export default {
  name: 'MapView',
  components: {Map, NavBar},
  props: {
    spotId: String,
  }
}
</script>

<style lang="scss">
@import "src/assets/media_query";

.ol-attribution {
  // slightly illegal
  display: none;
}

@include media-query();
</style>