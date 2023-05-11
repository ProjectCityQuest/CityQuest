<template>
  <div ref="map-root" id="map"></div>
  <div ref="popup" class="popup" hidden="hidden"></div>
  <div class="ol-control ol-unselectable locate" ref="locate">
    <button title="Locate me" @click="zoomToUser">◎</button>
  </div>
  <SpotInfo :is-visible="spotInfo.isVisible" :name="spotInfo.name" :description="spotInfo.description"
            @close="spotInfo.isVisible=false"></SpotInfo>
</template>

<script>
// styles nav elements on map
import 'ol/ol.css'
import * as featureStyles from "@/featureStyles";
import * as spotsHelper from "@/spotsHelper";
import SpotInfo from "@/components/SpotInfo.vue";

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
import {Circle, Polygon} from "ol/geom";
import {transform, useGeographic} from "ol/proj";
import {PinchRotate} from "ol/interaction";
import {Overlay} from "ol";

export default {
  name: 'Map',
  components: {SpotInfo},
  data() {
    return {
      // in meters
      spots: [],
      range: 20,
      map: undefined,
      positionSource: undefined,
      positionLayer: undefined,
      spotsLayer: undefined,
      rangeSource: undefined,
      rangeCircle: undefined,
      rangeLayer: undefined,
      spotsInRange: [],
      watcher: undefined,
      spotInfo: {
        isVisible: false,
        name: "",
        description: ""
      }
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

      this.spotsInRange = [];

      features.forEach((feature) => {
        let features = feature.get("features");
        if (features.length === 1) {
          let featureId = features[0].id_;

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
    async drawSpots() {
      let features = this.generateSpots(this.spots);

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
          this.getSpotsInRange();
          spotsSource.clear(true);
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
            let spot = feature.get("features")[0];

            let featureId = spot.id_;
            let isDiscovered = spot.get("discovered") === "true";

            if (this.spotsInRange.includes(featureId)) {
              return featureStyles.getSpotStyle(true, isDiscovered);
            } else {
              return featureStyles.getSpotStyle(false, isDiscovered);
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
    generateSpots(spots) {
      let features = [];
      let counter = 0;

      for (let spot of spots) {
        let coords = spot.coordinates;
        let id = spot.id;

        let feature = new Feature({
          geometry: new Point([coords[1], coords[0]])
        });

        feature.setId(id);
        feature.set("discovered", String(spot.discovered));

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

              spotsHelper.getSpotByID(featureId).then(spot => {
                // show overlay
                this.spotInfo.isVisible = true;
                this.spotInfo.name = spot.name;
                this.spotInfo.description = spot.description;

                // display popup
                valueToShow = spot.name;
                this.$refs.popup.innerHTML = valueToShow;
                this.$refs.popup.hidden = false;

                popup.setPosition(feature.getGeometry().getCoordinates());

                let offsetX = this.$refs.popup.clientWidth/2;
                popup.setOffset([-offsetX, 0])
              });
            }

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

      if (features.length === 1) {
        this.centerFeatureInTopHalfOfScreen(features[0])
        return;
      }

      // zoom to feature
      this.map.getView().fit(polygon.getExtent(), {
        duration: 500
      });

    },
    centerFeatureInTopHalfOfScreen(feature) {
      const paddingTop = -(this.map.getTargetElement().clientHeight / 2)

      this.map.getView().fit(feature.getGeometry().getExtent(), {
        duration: 500,
        padding: [paddingTop, 0, 0, 0]
      });
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
  async mounted() {
    // activates geographic coordinates
    useGeographic();

    await spotsHelper.getAll().then(data => this.spots = data);

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
    await this.drawSpots();

    this.trackUserPosition();

    this.createLocateButton();

    this.map.on("click", this.interact);

    this.updateSpotsInRangeOnZoom();

    // setInterval(this.getSpotsInRange, 500);
  },
  unmounted() {
    navigator.geolocation.clearWatch(this.watcher);
  }
}
</script>

<style scoped lang="scss">
@import "/src/assets/colors";

#map {
  width: 100vw;
  height: 100vh;
  border-style: none;
  margin: 0;
  padding: 0;
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

.popup {
  color: $blue;
  letter-spacing: 0.05em;
  text-shadow: -1px 1px 2px #fff,
  1px 1px 2px #fff,
  1px -1px 0 #fff,
  -1px -1px 0 #fff;
}
</style>