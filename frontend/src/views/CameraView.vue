<template>
  <div class="camera">
    <div class="close-icon" @click="goBack()">
      <svg xmlns="http://www.w3.org/2000/svg"
           viewBox="0 0 50 50">
        <circle cx="25" cy="25" r="25" style="fill:#e73829"/>
        <rect x="23" y="9.3" width="4" height="30.41" rx="1.24" transform="translate(24.65 -10.5) rotate(45)"
              style="fill:#f9f9f9"/>
        <rect x="23" y="9.8" width="4" height="30.41" rx="1.24" transform="translate(60.36 25) rotate(135)"
              style="fill:#f9f9f9"/>
      </svg>
    </div>
    <video id="video"></video>
    <div class="hr top"></div>
    <div class="hr bottom"></div>
    <div class="vr left"></div>
    <div class="vr right"></div>
  </div>
  <div class="footer-wrapper">
    <canvas id="canvas"></canvas>
    <div id="last-shot-container" @click="toGallery()">
      <img v-if="isPreviewVisible" id="last-shot" :src="lastShot" alt="Vorschaubild">
    </div>
    <div id="take-photo">
      <div id="inner-circle"></div>
    </div>
  </div>
</template>

<script>
import router from '@/router'
import * as cameraHelper from "@/cameraHelper";

export default {
  name: "CameraView",
  data() {
    return {
      width: 0,
      height: 100,
      streaming: false,
      video: null,
      canvas: null,
      fromPath: "",
      isPreviewVisible: false
    }
  },
  mounted() {
    this.runCamera()
  },
  beforeRouteEnter (to, from, next) {
    next(vm => {
      vm.fromPath = from.path

      if (!String(vm.fromPath).includes("galerie")) {
        cameraHelper.saveFromPath(vm.fromPath);
      }
    })
  },
  methods: {
    runCamera() {
      this.video = document.getElementById("video")
      this.canvas = document.getElementById("canvas")

      navigator.mediaDevices
          .getUserMedia({
            video: true,
            audio: false
          })
          .then((stream) => {
            this.video.srcObject = stream
            this.video.play()
          })

      this.video.addEventListener(
          "canplay",
          () => {
            if (!this.streaming) {
              this.width = this.video.videoWidth / (this.video.videoHeight / this.height)

              this.video.setAttribute("height", this.height + "%")
              this.canvas.setAttribute("width", this.width + "%")
              this.canvas.setAttribute("height", this.height + "%")
              this.streaming = true
            }
          }, false)

      document.getElementById("take-photo").addEventListener(
          "click",
          (event) => {
            this.takePicture()
            event.preventDefault()
          }, false)
    },
    takePicture() {
      this.isPreviewVisible = true;

      const context = this.canvas.getContext("2d");
      context.drawImage(this.video, 0, 0, this.width, this.height);
      const data = canvas.toDataURL("image/png");

      cameraHelper.addImageToGallery(data);

      if (cameraHelper.getGallery().length > 0) {
        document.getElementById("last-shot").src = data
      }
    },
    toGallery() {
      router.push('/galerie/');
    },
    goBack() {
      cameraHelper.clearGallery();
      sessionStorage.removeItem("selectedImage")
      router.push(cameraHelper.getFromPath());
    }
  },
  computed: {
    lastShot() {
      // const gallery = JSON.parse(sessionStorage.getItem('gallery')) || [];
      const gallery = cameraHelper.getGallery();
      if (gallery.length > 0) {
        return gallery[gallery.length - 1];
      } else {
        return "";
      }
    }
  }
}
</script>

<style scoped lang="scss">
@import "src/assets/colors.scss";

.camera {
  background-color: $light_gray;
  width: 100%;
  height: 100vh;
  z-index: 1;
  overflow-x: hidden;
  display: flex;
  flex-direction: column;
  justify-content: center;

  #video{
    transform: scaleX(-1) !important;
  }

  .hr,
  .vr {
    position: absolute;
    background-color: $white;
    opacity: 0.5;
    z-index: 2;
  }

  .close-icon {
    position: absolute;
    top: 10px;
    right: 10px;

    width: 25px;
    height: 25px;
    background-color: transparent;
    z-index: 3;
  }

  .hr {
    width: 100%;
    height: 1px;

    &.top {
      top: 33%;
    }

    &.bottom {
      bottom: 33%;
    }
  }

  .vr {
    height: 100%;
    width: 1px;

    &.left {
      left: 33%;
    }

    &.right {
      right: 33%;
    }
  }
}

.footer-wrapper {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 70px;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 3;

  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-template-rows: 1fr;
  gap: 0 0;
  grid-template-areas: ". . .";

  #canvas {
    display: none;
  }

  #last-shot-container {
    grid-column: 1;
    width: 55px;
    height: 55px;
    background-color: $black;
    border-radius: 5px;
    margin: 7px 0 0 7px;

    img {
      height: 100%;
      width: 100%;
      object-fit: cover;
      padding: 0;
      margin: 0;
      border-radius: 5px;
    }
  }

  #take-photo {
    grid-column: 2;
    margin: 7px auto 0 auto;
    border-radius: 99999px;
    height: 55px;
    width: 55px;
    background-color: $black;
    border: solid 4px $white;
    box-sizing: border-box;

    display: flex;
    justify-content: center;
    align-items: center;

    #inner-circle {
      height: 43px;
      width: 43px;
      background-color: $white;
      border-radius: 99999px;
      transition: transform 0.2s ease-in-out;

      &:active {
        transform: scale(0.8);
        animation: none;
      }
    }
  }
}
</style>