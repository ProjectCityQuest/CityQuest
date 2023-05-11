<template>
  <div class="info-wrapper" v-if="isVisible">
    <nav>
      <svg class="close-icon" xmlns="http://www.w3.org/2000/svg"
           viewBox="0 0 50 50" @click="close">
        <circle cx="25" cy="25" r="25" style="fill:#e73829"/>
        <rect x="23" y="9.3" width="4" height="30.41" rx="1.24" transform="translate(24.65 -10.5) rotate(45)"
              style="fill:#f9f9f9"/>
        <rect x="23" y="9.8" width="4" height="30.41" rx="1.24" transform="translate(60.36 25) rotate(135)"
              style="fill:#f9f9f9"/>
        <rect class="close-btn" x="0" y="0" width="50" height="50" @click="close"/>
      </svg>
    </nav>
    <h1>{{ name }}</h1>
    <div class="info-box">
      <div class="description" ref="description">
        <p :class="isDescriptionExpanded ? 'more' : 'less'">{{ description }}</p>
        <div class="expand" @click="expandDescription">{{ isDescriptionExpanded ? "weniger" : "mehr" }}</div>
      </div>
      <div class="actions">
        <CQButton v-if="isInRange" @click="collectPuzzlePiece" b-style="puzzle-piece" :status="isDiscovered ? 'inactive' : 'active'">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 50 50">
            <path
                d="M46.11,22.31c-1.56,0-2.55,1.46-3.9,1.22a1.26,1.26,0,0,1-1.16-1.22V12.1a1.29,1.29,0,0,0-1.28-1.28H27.85A1.46,1.46,0,0,1,26.5,9.58c-.22-1.46,1.35-2.46,1.35-4.11S26.67,1.4,22.62,1.4s-6.3,2.34-4.86,6.17c1.17,3.09-.37,3.29-.37,3.29H6.74a1.28,1.28,0,0,0-1.28,1.26V22.56s.31,1.89,2.92.41c1.85-1,5.51.81,5.51,5.1s-2.82,5.49-3.68,5.49c-1.46,0-2.42-1.66-3.69-1.45a1.45,1.45,0,0,0-1.09,1.45V44.67A1.28,1.28,0,0,0,6.71,46H17.92a1.47,1.47,0,0,0,1.36-1.17c.22-1.35-1.36-2.35-1.36-3.91,0-.9,1.17-3.89,5.24-3.89S29,40.88,28,42.82c-1.45,2.79.4,3.12.4,3.12H39.79a1.29,1.29,0,0,0,1.3-1.28V31.71s.16-1.38,3.08-.27c3.64,1.31,5.83-.69,5.83-4.38S47.67,22.31,46.11,22.31Z"
                style="fill:#fff"/>
          </svg>
        </CQButton>
        <CQButton v-if="isInRange" @click="createCollectionEntry" b-style="collection-entry">Sammelbucheintrag erstellen</CQButton>
        <p v-else class="info">{{
            isDiscovered ? "Hier warst du schon. Du kannst trotzdem nochmal vorbeikommen und einen Sammelbucheintrag erstellen." :
                "Du hast diesen Ort noch nie besucht. Schau' vorbei und hol dir ein fehlendes Puzzleteil!"
          }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import CQButton from "@/components/CQButton.vue";

export default {
  name: "SpotInfo",
  components: {CQButton},
  emits: ["close", "collectPuzzlePiece"],
  props: {
    isVisible: Boolean,
    isDiscovered: Boolean,
    isInRange: Boolean,
    name: String,
    description: String,
    id: String
  },
  data() {
    return {
      isDescriptionExpanded: false,
    }
  },
  methods: {
    expandDescription() {
      this.isDescriptionExpanded = !this.isDescriptionExpanded;

      if (!this.isDescriptionExpanded) {
        this.$refs.description.scrollTop = 0;
      }
    },
    close() {
      this.$emit("close");
    },
    collectPuzzlePiece() {
      this.$emit("collectPuzzlePiece", this.id);
    },
    createCollectionEntry() {
      this.$router.push("/sammelbuch/neu/" + this.id.replace("CityQuest", ""));
    },
  },
  watch: {
    id() {
      this.isDescriptionExpanded = false;
    }
  }
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
    margin: 1rem 1rem 0 1rem;

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

  h1 {
    font-size: 20px;
    margin: 0 1rem 1rem 1rem;
  }

  .info-box {
    margin: 0 1rem 1rem 1rem;
    height: 70%;
    padding-right: 0.5rem;
    overflow: scroll;
    display: flex;
    flex-direction: column;
    justify-content: space-between;

    .description {
      height: fit-content;
      margin-bottom: 1rem;

      p {
        hyphens: auto;
        line-height: 20px;

        &.less {
          overflow: hidden;
          height: calc(20px * 3);
        }

        &.more {
        }
      }

      .expand {
        width: 100%;
        height: 1rem;
        color: $dark_gray;
      }
    }

    .actions {
      display: flex;
      gap: 10px;
      margin-top: 1rem;
      height: fit-content;
      justify-content: center;

      .info {
        font-style: italic;
        text-align: center;
        color: $dark_gray;
      }
    }
  }
}
</style>