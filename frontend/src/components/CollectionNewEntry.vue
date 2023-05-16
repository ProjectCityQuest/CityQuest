<template>
  <div class="entry-container">
    <div class="entry">
      <div class="top">
        <h1>{{ getLocationName }}</h1>
        <p>Besucht am: {{ getDateNowFormatted }}</p>
      </div>
      <div class="hr"></div>
      <div>
        <img>
        <p>{{ }}</p>
      </div>
    </div>
    <div class="spacer"></div>
  </div>

</template>

<script>

export default {
  name: "CollectionNewEntry",
  props: {
    spotId: String,
    locationName: String
  },
  computed: {
    getLocationName() {
      return this.locationName;
    },
    getDateNowFormatted() {
      let date = new Date(Date.now());

      return `${String(date.getUTCDate()).padStart(2, "0")}.${String(date.getMonth() + 1).padStart(2, "0")}.
        ${date.getFullYear()}, ${String(date.getHours()).padStart(2, "0")}:${String(date.getMinutes()).padStart(2, "0")}`;
    }
  }
}
</script>

<style scoped lang="scss">
@import "src/assets/colors";

.entry-container {
  display: flex;
  flex-direction: column;
  align-items: center;

  .entry {
    width: 90%;
    background-color: #eaeaea;
    border-radius: 10px;
    margin: 0 auto 0 auto;

    .top {
      padding: 15px;
      text-align: center;

      h1 {
        font-size: 1.3rem;
      }

      p {
        padding-top: 10px;
        font-size: 0.95rem;
      }
    }

    .hr {
      width: 100%;
      height: 2px;
      background-color: #c9c9c9;
      margin-top: 10px;
    }

    .body {
      display: flex;
      gap: 15px;
      padding: 15px;

      img {
        object-fit: cover;
        border-radius: 5px;
      }

      p {
        font-size: 0.95rem;
        text-align: justify;
        hyphens: auto;
      }

      &.img-hor {
        flex-direction: column;
      }

      &.img-vert {
        flex-direction: row-reverse;

        img {
          width: 50%;
        }
      }
    }
  }

  button {
    width: 60%;
    margin-top: 15px;
  }

  .delete {
    text-align: center;
    margin-top: 15px;
    text-decoration: underline;
    color: $dark_gray;
  }

  .spacer {
    height: 80px;
    width: 100%;
  }
}
</style>