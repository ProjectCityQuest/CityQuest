<template>
  <div class="camera">
      <video id="video"></video>
      <div id="takePhoto">Foto machen</div>
    <canvas id="canvas"></canvas>
  </div>
  <div class="footer-wrapper">

  </div>
</template>

<script>
export default {
  name: "CameraView",
  data() {
    return {
      width: 200,
      height: 0,
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
              this.height = this.video.videoHeight / (this.video.videoWidth / this.width)

              this.video.setAttribute("height", this.height)
              this.canvas.setAttribute("width", this.width)
              this.canvas.setAttribute("height", this.height)
              this.streaming = true
            }
          }, false)

      document.getElementById("takePhoto").addEventListener(
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
      const photo = document.createElement("img")
      photo.src = data
      this.gallery.push(data)
      console.log(this.gallery)
    }
  }

}
</script>

<style scoped lang="scss">
@import "src/assets/colors.scss";

.camera{
  background-color: $red;
  width: 100%;
  height: 100vh;
  z-index: 1;
}

.footer-wrapper {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 70px;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 9999;
}
</style>