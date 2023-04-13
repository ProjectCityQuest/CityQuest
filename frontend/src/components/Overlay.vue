<template>
  <div class="overlay-wrapper" :class="isVisible ? 'visible' : 'invisible'">
    <div class="overlay-background" @click="closeOverlay">
      <div class="overlay-container">
        <nav>
          <button @click="closeOverlay">X</button>
        </nav>
        <slot></slot>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Overlay",
  props: {
    isVisible: Boolean
  },
  emits: ['closeOverlay'],
  methods: {
    closeOverlay(event) {
      if (event.target !== event.currentTarget) return

      this.$emit('closeOverlay')
    }
  }
}
</script>

<style scoped lang="scss">
@import "../assets/colors";

.overlay-wrapper {
  &.visible {
    display: block;
  }

  &.invisible {
    display: none;
  }

  .overlay-background {
    position: fixed;
    top: 0;
    left: 0;
    z-index: 1;
    background: rgba(0, 0, 0, 0.5);
    height: 100vh;
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;

    .overlay-container {
      z-index: 2;
      background: $white;
      padding: 1rem;

      nav {
        display: flex;
        justify-content: flex-end;
      }

      width: 75%;
    }
  }
}
</style>