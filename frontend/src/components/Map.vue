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
import {Fill, Icon, Text, Style} from 'ol/style';
import {useGeographic} from "ol/proj";
import {spots} from "@/spots";

// styles nav elements on map
import 'ol/ol.css'
import {Cluster} from "ol/source";

export default {
  name: 'Map',
  data() {
    return {
      userPositionFound: false,
      map: undefined,
      positionSource: undefined,
      positionLayer: undefined,
      rangeSource: undefined,
      rangeLayer: undefined,
      locateButton: undefined,
      watcher: undefined,
      features: []
    }
  },
  methods: {
    initiateMap(enablePreloading) {
      this.map = new Map({
        target: 'map',
        layers: [new TileLayer({
          // enables preloading (blurry low res tiles)
          preload: enablePreloading ? Infinity : 0,
          maxZoom: 19,
          source: new OSM()
        })], view: new View({
          center: [0, 0],
          zoom: 2,
          maxZoom: 19
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
                console.warn("user has not been located yet");
              }
            }

            const coords = [pos.coords.longitude, pos.coords.latitude];

            this.drawRange(coords);
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
      let position = new Feature(new Point(coords));

      position.setStyle(new Style({
        image: new Icon({
          src: '/src/assets/location.svg',
          imgSize: [24, 24],
          rotateWithView: true,
        }),
      }));

      this.positionSource.clear(true);
      this.positionSource.addFeature(position);
    },
    drawRange(coords) {
      const rangeCircle = new Feature(new Point(coords))

      const iconStyle = new Style({
        image: new Icon({
          src: '/src/assets/range.svg',
          imgSize: [64, 64],
          rotateWithView: true,
        }),
      })

      this.rangeLayer.setStyle((feature, resolution) => {
        iconStyle.getImage().setScale(this.map.getView().getResolutionForZoom(17) / resolution);
        return iconStyle;
      })

      this.rangeSource.clear(true);
      this.rangeSource.addFeature(rangeCircle);
    },
    drawSpots() {
      let features = [];

      for (let [lat, lon] of spots) {
        features.push(new Feature(new Point([lon, lat])))
      }

      const spotsSource = new VectorSource({
        features: features
      });

      const clusterSource = new Cluster({
        distance: 30,
        minDistance: 10,
        source: spotsSource
      })

      const styleCache = {};
      const spotsLayer = new VectorLayer({
        source: clusterSource,
        style: function (feature) {
          const size = feature.get('features').length;
          let style = styleCache[size];

          let icon;
          let text = "";

          if (!style) {
            if (size === 1) {
              icon = new Icon({
                src: '/src/assets/spot/spot.svg',
                imgSize: [80, 80]
              });
            } else {
              icon = new Icon({
                src: '/src/assets/spot/cluster.svg',
                imgSize: [80, 80]
              });

              text = String(size);
            }

            style = new Style({
              image: icon,
              text: new Text({
                text: [text, "14px Berlin Sans FB"],
                offsetY: -20,
                fill: new Fill({
                  color: '#fff',
                }),
              }),
            });
            styleCache[size] = style;
          }
          return style;
        },
      });

      this.map.addLayer(spotsLayer);
    },
    zoomToUser() {
      this.map.getView().fit(this.positionSource.getExtent(), {
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
    // activates geographic coordinates
    useGeographic();

    this.initiateMap(true);

    this.positionSource = new VectorSource();
    this.positionLayer = new VectorLayer({
      source: this.positionSource
    });

    this.rangeSource = new VectorSource();
    this.rangeLayer = new VectorLayer({
      source: this.rangeSource,
      updateWhileAnimating: true,
      updateWhileInteracting: true,
    })

    this.map.addLayer(this.rangeLayer);
    this.map.addLayer(this.positionLayer);
    this.drawSpots();

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