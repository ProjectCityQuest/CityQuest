<template>
  <div class="piece-container" ref="container" :style="{ 'grid-area': gridArea }">
  </div>
</template>

<script>
export default {
  name: "PuzzlePiece",
  props: {
    location_name: String,
    visited: Boolean,
    image: String,
    row: Number,
    column: Number
  },
  mounted(){
    if (this.visited){
      this.$refs.container.style.backgroundImage  = 'url(' + this.image + ')'
      //this.$refs.container.style.background = "rgb(" +(this.row * this.column * 2) +","+(this.row * this.column * 2)+","+(this.row * this.column * 2)+")"
    }
  },
  computed:{
    gridArea(){
      let row = String.fromCharCode('a'.charCodeAt(0) + (this.row -1 ))
      let column = String.fromCharCode('a'.charCodeAt(0) + (this.column -1 ))
      return column + row;
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
  box-sizing: border-box;
  background-position: center;
  background-size: cover;
}
</style>