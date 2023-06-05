<template>
  <div class="overlay-wrapper" :class="isVisible ? 'visible' : 'invisible'">
    <div class="overlay-background" @click="closeOverlay">
      <div class="overlay-container">
        <nav>
          <div>
            <svg class="close-icon" xmlns="http://www.w3.org/2000/svg" @click="closeOverlay"
                 viewBox="0 0 50 50">
              <circle cx="25" cy="25" r="25" style="fill:#e73829"/>
              <rect x="23" y="9.3" width="4" height="30.41" rx="1.24" transform="translate(24.65 -10.5) rotate(45)"
                    style="fill:#f9f9f9"/>
              <rect x="23" y="9.8" width="4" height="30.41" rx="1.24" transform="translate(60.36 25) rotate(135)"
                    style="fill:#f9f9f9"/>
              <rect class="close-btn" x="0" y="0" width="50" height="50" @click="closeOverlay"/>
            </svg>
          </div>
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
@import "src/assets/colors";
@import "src/assets/media_query";

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
      border-radius: 10px;
      width: 75%;

      nav {
        display: flex;
        justify-content: flex-end;


        .close-icon {
          height: 25px;

          .close-btn {
            stroke: #fff;
            fill: #fff;
            fill-opacity: 0;
            stroke-opacity: 0;
          }
        }
      }
    }
  }
}

@include overlay-media-query();
</style>