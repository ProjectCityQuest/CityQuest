<template>
  <header>
    <slot></slot>
  </header>
</template>

<script>
export default {
  name: "Header"
}
</script>

<style scoped lang="scss">
@import "/src/assets/colors";

header {
  background-color: $white;
  position: fixed;
  width: 100%;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  margin-bottom: 1rem;
  border-bottom: solid $light_gray 2px;;

  height: 70px;

  :slotted(h1) {
    font-size: 32px;
  }

  :slotted(svg) {
    height: 45px;
  }
}

</style>