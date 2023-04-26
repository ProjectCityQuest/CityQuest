<template>
  <div ref="map-root" id="map">
  </div>
  <div class="ol-control ol-unselectable locate" ref="locate">
    <button title="Locate me" @click="zoomToUser">◎</button>
  </div>
  <NavBar :active-icon="2"></NavBar>
</template>

<script>
import NavBar from "@/components/NavBar.vue";

import View from 'ol/View'
import Map from 'ol/Map'
import TileLayer from 'ol/layer/Tile'
import OSM from 'ol/source/OSM'
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import {circular} from 'ol/geom/Polygon';
import Control from 'ol/control/Control';
import {Icon, Stroke, Style} from 'ol/style';
import {fromLonLat} from "ol/proj";
import kompas from 'kompas';

// styles nav elements on map
import 'ol/ol.css'

export default {
  name: 'MapView',
  components: {NavBar},
  data() {
    return {
      userPositionFound: false,
      map: undefined,
      vectorSource: undefined,
      vectorLayer: undefined,
      locateButton: undefined,
      watcher: undefined
    }
  },
  methods: {
    initiateMap(enablePreloading) {
      this.map = new Map({
        target: 'map', layers: [new TileLayer({
          // enables preloading (blurry low res tiles)
          preload: enablePreloading ? Infinity : 0,
          source: new OSM()
        })], view: new View({
          center: [0, 0], zoom: 2
        })
      });
    },
    trackUserPosition() {
      this.watcher = navigator.geolocation.watchPosition(
          (pos) => {
            // centers map on user position
            if (!this.userPositionFound) {
              try {
                this.zoomToUser()

                this.userPositionFound = true;
              } catch (err) {
                console.log("User not located yet")
              }
            }

            const coords = [pos.coords.longitude, pos.coords.latitude];
            const accuracy = circular(coords, pos.coords.accuracy);
            this.vectorSource.clear(true);
            this.vectorSource.addFeatures([
              new Feature(
                  accuracy.transform('EPSG:4326', this.map.getView().getProjection())
              ),
              new Feature(new Point(fromLonLat(coords))),
            ]);
          },
          function (error) {
            alert(`ERROR: ${error.message}`);
          },
          {
            enableHighAccuracy: true,
          }
      );
    },
    zoomToUser() {
      this.map.getView().fit(this.vectorSource.getExtent(), {
        maxZoom: 18,
        duration: 500,
      });
    },
    createLocateButton() {
      this.map.addControl(
          new Control({
            element: this.$refs.locate,
          })
      );
    },
    trackUserHeading() {
      const style = new Style({
        stroke: new Stroke({
          color: 'rgba(237, 54, 36, 0.6)',
          lineDash: [4, 8]
        }),
        image: new Icon({
          src: '/src/assets/location-heading.svg',
          imgSize: [64, 64],
          rotateWithView: true,
        }),
      });

      this.vectorLayer.setStyle(style)

      function startCompass() {
        kompas()
            .watch()
            .on('heading', function (heading) {
              style.getImage().setRotation((Math.PI / 180) * heading);
            });
      }

      if (
          window.DeviceOrientationEvent &&
          typeof DeviceOrientationEvent.requestPermission === 'function'
      ) {
        this.$refs.locate.addEventListener('click', function () {
          DeviceOrientationEvent.requestPermission()
              .then(startCompass)
              .catch(function (error) {
                alert(`ERROR: ${error.message}`);
              });
        });
      } else if ('ondeviceorientationabsolute' in window) {
        startCompass();
      } else {
        alert('No device orientation provided by device');
      }
    }
  },
  mounted() {
    this.initiateMap(true)

    this.vectorSource = new VectorSource();
    this.vectorLayer = new VectorLayer({
      source: this.vectorSource,
    });

    this.map.addLayer(this.vectorLayer)

    this.trackUserPosition()

    this.createLocateButton()

    this.trackUserHeading()
  },
  unmounted() {
    navigator.geolocation.clearWatch(this.watcher)
  }
}
</script>

<style lang="scss">
#map {
  width: 100vw;
  height: 100vh;
  border-style: none;
  margin: 0;
  padding: 0;

  .ol-attribution {
    // slightly illegal
    display: none;
  }
}

.locate {
  top: 6em;
  left: .5em;
}
</style>