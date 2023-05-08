<template>
  <div class="info-wrapper">
    <nav>
      <svg class="close-icon" xmlns="http://www.w3.org/2000/svg"
           viewBox="0 0 50 50">
        <circle cx="25" cy="25" r="25" style="fill:#e73829"/>
        <rect x="23" y="9.3" width="4" height="30.41" rx="1.24" transform="translate(24.65 -10.5) rotate(45)"
              style="fill:#f9f9f9"/>
        <rect x="23" y="9.8" width="4" height="30.41" rx="1.24" transform="translate(60.36 25) rotate(135)"
              style="fill:#f9f9f9"/>
        <rect class="close-btn" x="0" y="0" width="50" height="50"/>
      </svg>
    </nav>
    <div class="info-box">hallo test</div>
  </div>
</template>

<script>
export default {
  name: "SpotInfo"
}
</script>

<style scoped lang="scss">
@import "/src/assets/colors";

.info-wrapper {
  position: fixed;
  z-index: 1;
  background: $white;
  border-radius: 10px 10px 0 0;
  height: 50vh;
  width: 100%;
  bottom: 0;
  box-shadow: 0 0 8px 3px rgba(0, 0, 0, 0.5);
  -webkit-box-shadow: 0 0 8px 3px rgba(0, 0, 0, 0.5);

  nav {
    display: flex;
    justify-content: flex-end;
    margin: 1rem;

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

  .info-box {
    margin: 1rem;
  }
}
</style>