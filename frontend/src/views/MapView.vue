<template>
  <iframe id="map"
          src="https://www.openstreetmap.org/export/embed.html?bbox=16.396129131317142%2C48.18961203548659%2C16.398049592971805%2C48.19273046006295&amp;layer=mapnik">
  </iframe>
</template>

<script>
export default {
  name: "MapView"
}
</script>

<style scoped lang="scss">
#map {
  width: 100vw;
  height: 100vh;
  border-style: none;
  margin: 0;
  padding: 0;
}
</style>