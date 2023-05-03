<template>
  <div ref="map-root" id="map">
  </div>
  <div class="ol-control ol-unselectable locate" ref="locate">
    <button title="Locate me" @click="zoomToUser">◎</button>
  </div>
</template>

<script>
import View from 'ol/View'
import Map from 'ol/Map'
import TileLayer from 'ol/layer/Tile'
import OSM from 'ol/source/OSM'
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import Control from 'ol/control/Control';
import {Icon, Style} from 'ol/style';
import {fromLonLat} from "ol/proj";

// styles nav elements on map
import 'ol/ol.css'

export default {
  name: 'Map',
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
                this.zoomToUser();

                this.userPositionFound = true;
              } catch (err) {
                console.warn("User not located yet");
              }
            }

            const coords = [pos.coords.longitude, pos.coords.latitude];

            this.drawPosition(coords);
          },
          function (error) {
            alert(`ERROR: ${error.message}`);
          },
          {
            enableHighAccuracy: true,
          }
      );
    },
    drawPosition(coords) {
      let feature = new Feature(new Point(fromLonLat(coords)));
      feature.setStyle(new Style({
        image: new Icon({
          src: '/src/assets/location.svg',
          imgSize: [24, 24],
          rotateWithView: true,
        }),
      }));

      this.vectorSource.clear(true);
      this.vectorSource.addFeature(feature);
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
  },
  mounted() {
    this.initiateMap(true);

    this.vectorSource = new VectorSource();
    this.vectorLayer = new VectorLayer({
      source: this.vectorSource,
    });

    this.map.addLayer(this.vectorLayer);

    this.trackUserPosition();

    this.createLocateButton();
  },
  unmounted() {
    navigator.geolocation.clearWatch(this.watcher);
  }
}
</script>

<style scoped lang="scss">
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