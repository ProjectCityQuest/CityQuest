<template>
  <div @click="openOverlay" class="piece-container" :style="{ 'grid-area': gridArea }">
    <img @click="openOverlay" class="piece-image" v-if="image!==''" :src="image">
  </div>
</template>

<script>
export default {
  name: "PuzzlePiece",
  emits: ['openOverlay'],
  props: {
    location_name: String,
    image: String,
    row: Number,
    column: Number,
    id: Number
  },
  computed:{
    gridArea(){
      let row = String.fromCharCode('a'.charCodeAt(0) + (this.row -1 ))
      let column = String.fromCharCode('a'.charCodeAt(0) + (this.column -1 ))
      return column + row;
    }
  },
  methods:{
    openOverlay(event) {
      if (event.target !== event.currentTarget) return
      this.$emit('openOverlay', {location_name: this.location_name, visited: this.image !== "", id: this.id})
    }
  }
}
</script>

<style scoped lang="scss">
@import "src/assets/colors";

.piece-container{
  width: 100%;
  height: calc(calc(100vh - 140px) / 12);
  background-color: transparent;
  border-style: solid;
  border-width: 1px;
  border-color: $light_gray;
  box-sizing: border-box;

  &:hover{
    cursor: pointer;
  }

  .piece-image{
    width: 100%;
    height: 100%;
    object-fit: fill;
  }
}
</style>