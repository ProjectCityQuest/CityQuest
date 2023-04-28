<template>
  <div class="camera">
    <video id="video"></video>
  </div>
  <div class="footer-wrapper">
    <canvas id="canvas"></canvas>
    <div id="last-shot-container">
      <img id="last-shot" src="">
    </div>
    <div id="take-photo">
      <div id="inner-circle"></div>
    </div>
  </div>
</template>

<script>
export default {
  name: "CameraView",
  data() {
    return {
      width: 0,
      height: 100,
      streaming: false,
      video: null,
      canvas: null,
      gallery: []
    }
  },
  mounted() {
    this.runCamera()
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
      const context = this.canvas.getContext("2d")
      context.drawImage(this.video, 0, 0, this.width, this.height)
      const data = canvas.toDataURL("image/png")
      this.gallery.push(data)

      if (this.gallery.length > 0) {
        document.getElementById("last-shot").src = data;
      }
    }
  }

}
</script>

<style scoped lang="scss">
@import "src/assets/colors.scss";

.camera {
  background-color: $red;
  width: 100%;
  height: 100vh;
  z-index: 1;
  overflow-x: hidden;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.footer-wrapper {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 70px;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 9999;

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