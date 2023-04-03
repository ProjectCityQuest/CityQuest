<template>
  <div ref="map-root" id="map">
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
import {circular} from 'ol/geom/Polygon';
import Control from 'ol/control/Control';

// importing the OpenLayers stylesheet is required for having
// good-looking buttons!
import {fromLonLat} from "ol/proj";
import 'ol/ol.css'

export default {
  name: 'MapView',
  components: {},
  props: {},
  mounted() {
    const source = new VectorSource();
    const layer = new VectorLayer({
      source: source,
    });

    // this is where we create the OpenLayers map
    const map = new Map({
      // the map will be created using the 'map-root' ref
      target: this.$refs['map-root'],
      layers: [
        // adding a background tiled layer
        new TileLayer({
          source: new OSM() // tiles are served by OpenStreetMap
        }),
      ],

      // the map view will initially show the whole world
      view: new View({
        zoom: 0,
        center: [0, 0],
        constrainResolution: true
      }),
    })

    map.addLayer(layer)

    navigator.geolocation.watchPosition(
        function (pos) {
          const coords = [pos.coords.longitude, pos.coords.latitude];
          const accuracy = circular(coords, pos.coords.accuracy);
          source.clear(true);
          source.addFeatures([
            new Feature(
                accuracy.transform('EPSG:4326', map.getView().getProjection())
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

    const locate = document.createElement('div');
    locate.className = 'ol-control ol-unselectable locate';
    locate.innerHTML = '<button title="Locate me" class="locate">◎</button>';
    locate.addEventListener('click', function () {
      if (!source.isEmpty()) {
        map.getView().fit(source.getExtent(), {
          maxZoom: 18,
          duration: 500,
        });
      }
    });
    map.addControl(
        new Control({
          element: locate,
        })
    );
  },
}
</script>

<style lang="scss">
#map {
  width: 100vw;
  height: 100vh;
  border-style: none;
  margin: 0;
  padding: 0;
}

.locate {
  top: 6em;
  left: .5em;
}
</style>