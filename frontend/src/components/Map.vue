<template>
  <div ref="map-root" id="map"></div>
  <div ref="popup" class="popup" hidden="hidden"></div>
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
import {Fill, Icon, Text, Style, Stroke} from 'ol/style';
import {Cluster} from "ol/source";
import {Overlay} from "ol";
import {Circle} from "ol/geom";
import {transform, useGeographic} from "ol/proj";
import {spots} from "@/spots";

// styles nav elements on map
import 'ol/ol.css'

export default {
  name: 'Map',
  data() {
    return {
      userPositionFound: false,
      map: undefined,
      positionSource: undefined,
      positionLayer: undefined,
      spotsLayer: undefined,
      rangeSource: undefined,
      rangeCircle: undefined,
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
            this.getSpotsInRange();
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
    getSpotsInRange() {
      const extent = this.rangeCircle.getExtent();

      let features = this.spotsLayer.getSource().getFeaturesInExtent(extent);

      if (features.length === 0) {
        console.log("no spots found");
      }

      features.forEach(function (feature) {
        let features = feature.get("features");
        if (features.length === 1) {
          // alert(features[0].id_);
        }
      });
    },
    drawRange(coords) {
      const radius = 20;

      this.rangeCircle = new Circle(
          transform(coords, 'EPSG:4326', 'EPSG:3857'),
          radius
      ).transform('EPSG:3857', 'EPSG:4326');

      const rangeFeature = new Feature(this.rangeCircle);

      this.rangeSource.clear();
      this.rangeSource.addFeature(rangeFeature);
    },
    drawSpots() {
      let features = [];
      let counter = 0;

      for (let [lat, lon] of spots) {
        let feature = new Feature({
          geometry: new Point([lon, lat])
        });

        feature.setId("CityQuest" + counter);

        counter++;

        features.push(feature);
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
      this.spotsLayer = new VectorLayer({
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

      this.map.addLayer(this.spotsLayer);
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
    interact(event) {
      let features = [];
      let popup = new Overlay({
        element: this.$refs.popup,
        offset: [-9, -9]
      });

      this.map.addOverlay(popup);

      this.map.forEachFeatureAtPixel(event.pixel,
          (feature) => {
            features = feature.get('features');
            let valueToShow = "";

            if (features.length === 1) {
              valueToShow = features[0].id_;
            }

            this.$refs.popup.innerHTML = valueToShow;
            this.$refs.popup.hidden = false;

            popup.setPosition(feature.getGeometry().getCoordinates());
          })
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
      style: new Style({
        stroke: new Stroke({
          color: 'blue',
          width: 2
        }),
        fill: new Fill({
          color: 'rgba(0, 0, 255, 0.1)'
        })
      }),
    })

    this.map.addLayer(this.rangeLayer);
    this.map.addLayer(this.positionLayer);
    this.drawSpots();

    this.trackUserPosition();

    this.createLocateButton();

    this.map.on("click", this.interact);
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

.popup {
  border-radius: 5px;
  border: 1px solid grey;
  background-color: rgba(255, 255, 255, 0.9);
  padding: 2px;
}

.locate {
  top: 6em;
  left: .5em;
}
</style>