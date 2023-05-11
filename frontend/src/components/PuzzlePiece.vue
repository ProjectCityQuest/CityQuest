<template>
  <div @click="openOverlay" class="piece-container" ref="container" :style="{ 'grid-area': gridArea }">
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
    column: Number
  },
  mounted(){
    if (this.image !== ""){
      this.$refs.container.style.backgroundImage  = 'url(' + this.image + ')'
    }
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

      this.$emit('openOverlay')
    }
  }
}
</script>

<style scoped lang="scss">
@import "src/assets/colors";

.piece-container{
  width: 100%;
  height: 100%;
  background-color: transparent;
  border-style: solid;
  border-width: 1px;
  border-color: $light_gray;
  box-sizing: border-box;
  background-position: center;
  background-size: cover;
}
</style>