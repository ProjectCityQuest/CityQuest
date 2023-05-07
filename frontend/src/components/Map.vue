<template>
  <div ref="map-root" id="map"></div>
  <div ref="popup" class="popup" hidden="hidden"></div>
  <div class="ol-control ol-unselectable locate" ref="locate">
    <button title="Locate me" @click="zoomToUser">◎</button>
  </div>
  <p ref="log" class="log"></p>
</template>

<script>
// styles nav elements on map
import 'ol/ol.css'
import {spots} from "@/spots";
import * as featureStyles from "@/featureStyles";

import View from 'ol/View'
import Map from 'ol/Map'
import TileLayer from 'ol/layer/Tile'
import OSM from 'ol/source/OSM'
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import Control from 'ol/control/Control';
import {Cluster} from "ol/source";
import {Overlay} from "ol";
import {Circle, Polygon} from "ol/geom";
import {transform, useGeographic} from "ol/proj";
import {PinchRotate} from "ol/interaction";

export default {
  name: 'Map',
  data() {
    return {
      // in meters
      range: 20,
      map: undefined,
      positionSource: undefined,
      positionLayer: undefined,
      spotsLayer: undefined,
      rangeSource: undefined,
      rangeCircle: undefined,
      rangeLayer: undefined,
      spotsInRange: [],
      watcher: undefined
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
        })],
        view: new View({
          center: [0, 0],
          zoom: 2,
          maxZoom: 19
        })
      });

      this.disablePinchToRotate();
    },
    disablePinchToRotate() {
      let interactions = this.map.getInteractions().getArray();
      let pinchRotateInteraction = interactions.filter(function (interaction) {
        return interaction instanceof PinchRotate;
      })[0];

      pinchRotateInteraction.setActive(false);
    },
    trackUserPosition() {
      this.watcher = navigator.geolocation.watchPosition(
          (pos) => {
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

      position.setStyle(featureStyles.getPositionStyle());

      this.positionSource.clear(true);
      this.positionSource.addFeature(position);
    },
    getSpotsInRange() {
      if (!this.rangeCircle) {
        return;
      }

      const extent = this.rangeCircle.getExtent();

      let features = this.spotsLayer.getSource().getFeaturesInExtent(extent);

      if (features.length === 0) {
        return;
      }

      this.$refs.log.innerHTML = "";
      this.spotsInRange = [];

      features.forEach((feature) => {
        let features = feature.get("features");
        if (features.length === 1) {
          let featureId = features[0].id_;

          this.$refs.log.innerHTML += featureId + ", ";
          this.spotsInRange.push(featureId);
        }
      });
    },
    drawRange(coords) {
      this.rangeCircle = new Circle(
          transform(coords, 'EPSG:4326', 'EPSG:3857'),
          this.range
      ).transform('EPSG:3857', 'EPSG:4326');

      const rangeFeature = new Feature(this.rangeCircle);

      this.rangeSource.clear();
      this.rangeSource.addFeature(rangeFeature);
    },
    drawSpots() {
      let features = this.generateSpots();

      const spotsSource = new VectorSource({
        features: features
      });

      const clusterSource = new Cluster({
        distance: 30,
        minDistance: 10,
        source: spotsSource
      })

      spotsSource.setLoader(() => {
        let load = () => {
          spotsSource.addFeatures(features);
        }

        load();

        setInterval(load, 500);
      })

      const styleCache = {};
      this.spotsLayer = new VectorLayer({
        source: clusterSource,
        style: (feature) => {
          const clusterSize = feature.get('features').length;

          if (clusterSize === 1) {
            let featureId = feature.get('features')[0].id_;

            if (this.spotsInRange.includes(featureId)) {
              return featureStyles.getSpotStyle(true);
            } else {
              return featureStyles.getSpotStyle(false);
            }
          }

          let cachedStyle = styleCache[clusterSize];

          if (!cachedStyle) {
            let featureText = String(clusterSize);

            cachedStyle = featureStyles.getClusterStyle(featureText);
          }
          styleCache[clusterSize] = cachedStyle;
          return cachedStyle;
        },
      });

      this.map.addLayer(this.spotsLayer);
    },
    generateSpots() {
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

      return features;
    },
    zoomToUser() {
      if (this.positionSource.getExtent()[0] === Infinity) {
        // user position not found
        return;
      }

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
      let popup = new Overlay({
        element: this.$refs.popup,
        offset: [-9, -9]
      });

      this.map.addOverlay(popup);

      this.map.forEachFeatureAtPixel(event.pixel,
          (feature) => {

            this.zoomToFeature(feature);

            let valueToShow = "";

            let features = feature.get('features');

            if (!features) {
              return;
            }

            if (features.length === 1) {
              let featureId = features[0].id_;

              valueToShow = featureId;

              if (this.spotsInRange.includes(featureId)) {
                console.log(featureId, " is in range");
                valueToShow += " IN RANGE";
              }
            }

            this.$refs.popup.innerHTML = valueToShow;
            this.$refs.popup.hidden = false;

            popup.setPosition(feature.getGeometry().getCoordinates());
          })
    },
    zoomToFeature(feature) {
      let features = feature.get('features');

      if (!features) {
        return;
      }

      // create polygon geometry with all the features of a cluster in it
      let polygonPoints = [];
      features.forEach(elem => polygonPoints.push(elem.getGeometry().getCoordinates()));
      let polygon = new Polygon([polygonPoints])

      // scale the polygon for some padding
      polygon.scale(1.5);

      // zoom to feature
      this.map.getView().fit(polygon.getExtent(), {
        duration: 500
      })
    },
    updateSpotsInRangeOnZoom() {
      let currZoom = this.map.getView().getZoom();
      this.map.on('moveend', () => {
        let newZoom = this.map.getView().getZoom();
        if (currZoom !== newZoom) {
          currZoom = newZoom;

          this.getSpotsInRange();
        }
      });
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
      style: featureStyles.getRangeCircleStyle,
    })

    this.map.addLayer(this.rangeLayer);
    this.map.addLayer(this.positionLayer);
    this.drawSpots();

    this.trackUserPosition();

    this.createLocateButton();

    this.map.on("click", this.interact);

    this.updateSpotsInRangeOnZoom();

    setInterval(this.getSpotsInRange, 500);
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

.log {
  position: fixed;
  top: 6em;
  left: 5em;
}

.locate {
  top: 6em;
  left: .5em;
}
</style>