<template>
  <Map></Map>
  <NavBar :active-icon="2"></NavBar>
</template>

<script>
import NavBar from "@/components/NavBar.vue";
import Map from "@/components/Map.vue";

export default {
  name: 'MapView',
  components: {Map, NavBar}
}
</script>

<style lang="scss">
</style>