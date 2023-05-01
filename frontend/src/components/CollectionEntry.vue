<template>
  <div class="entry">
    <h3>{{ this.location }}</h3>
    <p>{{ this.date }}</p>
    <p>{{ this.text }}</p>
    <img v-if="this.image!=null" src="{{ this.image }}">
  </div>
</template>

<script>
export default {
  name: "CollectionEntry",
  props:{
    id: Number,
    location: String,
    date: String,
    text: String,
    image: String
  }
}
</script>

<style scoped>

</style>