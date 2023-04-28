<template>
  <h1 @click="logStore()">HAllo hier ist Galerie</h1>
  <div>
    <div v-for="(image, index) in gallery" :key="index">
      <img :src="image" />
    </div>
  </div>
</template>

<script>
export default {
  name: "GalleryView",
  computed: {
    gallery() {
      return this.$store.state.gallery
    }
  },
  methods:{
  }
}
</script>

<style scoped>

</style>