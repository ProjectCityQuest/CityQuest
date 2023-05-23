<template>
  <div class="view-container">
    <Header>
      <h1>Puzzle-Fortschritt</h1>
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 50 50" class="puzzle-icon">
        <path
            d="M40.33,22.74c-1.07,0-1.75,1-2.67.84a.88.88,0,0,1-.8-.84v-7a.88.88,0,0,0-.89-.88H27.81a1,1,0,0,1-.93-.85c-.15-1,.93-1.69.93-2.82S27,8.4,24.22,8.4,19.9,10,20.89,12.63c.8,2.12-.26,2.26-.26,2.26h-7.3a.87.87,0,0,0-.88.88v7.14s.21,1.3,2,.28c1.27-.72,3.78.56,3.78,3.5s-1.93,3.77-2.52,3.77c-1,0-1.66-1.14-2.53-1a1,1,0,0,0-.75,1v7.62a.88.88,0,0,0,.88.88H21a1,1,0,0,0,.93-.8c.15-.93-.93-1.61-.93-2.68,0-.62.8-2.67,3.59-2.67s4,2.67,3.31,4c-1,1.91.27,2.14.27,2.14H36a.89.89,0,0,0,.89-.88V29.19S37,28.24,39,29c2.5.9,4-.47,4-3S41.4,22.74,40.33,22.74Z"
            transform="translate(0)"/>
      </svg>
    </Header>
    <div class="puzzle-container">
      <div v-if="loading" class="loading"></div>
      <PuzzlePiece v-for='piece in pieces'
                   :id="piece.id"
                   :location_name="piece.location_name"
                   :image="piece.image"
                   :row="piece.row"
                   :column="piece.column" @open-overlay="openOverlay"
                   :class="this.id!== -1?this.id == piece.id? 'newPiece':'oldPiece':''"
      ></PuzzlePiece>
    </div>
    <Overlay :isVisible="overlayVisibile" @close-overlay="overlayVisibile = !overlayVisibile">
      <h1 class="overlay-heading" ref="overlay-heading"></h1>
      <p class="overlay-status" ref="overlay-status"></p>
      <CQButton class="overlay-button" b-style="login" :status="mapButtonState" @click="toMap">auf Karte anzeigen
      </CQButton>
    </Overlay>
    <NavBar :active-icon="3"></NavBar>
  </div>
</template>

<script>
import NavBar from "@/components/NavBar.vue";
import Header from "@/components/Header.vue";
import PuzzlePiece from "@/components/PuzzlePiece.vue";
import Overlay from "@/components/Overlay.vue";
import CQButton from "@/components/CQButton.vue";
import router from "@/router";

export default {
  name: "PuzzleView",
  components: {NavBar, Header, PuzzlePiece, Overlay, CQButton},
  props: {
    id: {
      type: Number,
      default: -1
    }
  },
  data() {
    return {
      overlayVisibile: false,
      mapButtonState: "active",
      activePiece: -1,
      pieces: [],
      loading: true,
      focus: false
    }
  },
  async mounted() {
    for (let i = 1; i <= 12; i++) {
      await fetch(`http://${window.location.hostname}:8080/api/getpuzzle`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          sessionKey: this.getCookie('sessionKey'),
        },
        withCredentials: true,
        credentials: 'same-origin',
        body: JSON.stringify({
          pageIndex: i
        })
      }).then(r => r.json())
          .then(data => {
            this.loading = false
            data.pieces.forEach(x => this.pieces.push(x))
          })
    }
    this.focus = this.id !== -1
  },
  methods: {
    getCookie(cname) {
      let name = cname + "=";
      let decodedCookie = decodeURIComponent(document.cookie);
      let ca = decodedCookie.split(';');
      for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') {
          c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
          return c.substring(name.length, c.length);
        }
      }
      return false;
    },
    openOverlay(e) {
      if (this.focus) {
        history.replaceState({id: 1}, '', `http://${window.location.hostname}:5173/puzzle`)

        let elems = document.querySelectorAll(".oldPiece");
        [].forEach.call(elems, function (el) {
          el.classList.remove("oldPiece");
        });

        elems = document.querySelectorAll(".newPiece");
        [].forEach.call(elems, function (el) {
          el.classList.remove("newPiece");
        });
        this.focus = false
      } else {
        this.overlayVisibile = !this.overlayVisibile
        this.activePiece = e.id
        this.$refs['overlay-heading'].innerHTML = e.location_name
        this.$refs['overlay-status'].innerHTML = "Status: " + (e.visited ? 'erkundet' : 'unerkundet')
      }
    },
    toMap() {
      router.push('/karte/ort/' + this.activePiece)
    }
  }
}
</script>

<style scoped lang="scss">
@import "src/assets/colors";

.view-container {
  .puzzle-icon {
    fill: $dark_green;
  }

  .puzzle-container {
    height: calc(100svh - 140px);
    width: 100%;
    padding-top: 70px;

    background-image: url("../assets/background.png");
    background-position: center;

    display: grid;
    grid-template-columns: 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr;
    grid-template-rows: 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr;
    gap: 0 0;
    grid-template-areas:
    "aa ba ca da ea fa ga ha ia ja"
    "ab bb cb db eb fb gb hb ib jb"
    "ac bc cc dc ec fc gc hc ic jc"
    "ad bd cd dd ed fd gd hd id jd"
    "ae be ce de ee fe ge he ie je"
    "af bf cf df ef ff gf hf if jf"
    "ag bg cg dg eg fg gg hg ig jg"
    "ah bh ch dh eh fh gh hh ih jh"
    "ai bi ci di ei fi gi hi ii ji"
    "aj bj cj dj ej fj gj hj ij jj"
    "ak bk ck dk ek fk gk hk ik jk"
    "al bl cl dl el fl gl hl il jl";

    .newPiece {
      border-color: gold;
      border-width: 3px;
    }

    .oldPiece {
      opacity: 0.5;
    }

    .loading {
      border: 0.8rem solid $light_gray;
      border-radius: 50%;
      border-top-color: $blue;
      width: 50px;
      height: 50px;

      grid-row-start: ef;
      grid-column-start: ef;
      grid-row-end: fg;
      grid-column-end: fg;

      -webkit-animation: spin 2s linear infinite;
      animation: spin 2s linear infinite;
    }
  }

  .overlay-heading {
    text-align: center;
    font-size: 20px;
    margin-bottom: 1rem;
  }

  .overlay-status {
    text-align: center;
    margin-bottom: 1rem;
  }

  .overlay-button {
    text-align: center;
    margin-left: auto;
    margin-right: auto;
  }
}

@-webkit-keyframes spin {
  0% {
    -webkit-transform: rotate(0deg);
  }
  100% {
    -webkit-transform: rotate(360deg);
  }
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>