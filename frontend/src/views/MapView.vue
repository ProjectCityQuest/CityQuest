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
import {Fill, Icon, Style} from 'ol/style';
import {fromLonLat} from "ol/proj";
import kompas from 'kompas';

// styles nav elements on map
import 'ol/ol.css'

export default {
  name: 'MapView',
  data() {
    return {
      positionFound: false
    }
  },
  mounted() {
    const map = new Map({
      target: 'map', layers: [new TileLayer({
        // enables preloading (blurry low res tiles)
        preload: Infinity,
        source: new OSM()
      })], view: new View({
        center: [0, 0], zoom: 2
      })
    });

    const source = new VectorSource();
    const layer = new VectorLayer({
      source: source,
    });

    map.addLayer(layer)

    // Draws GPS position on the map
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

    // Locate button to instantly move to GPS position
    const locate = document.createElement('div');
    locate.className = 'ol-control ol-unselectable locate';
    locate.innerHTML = '<button title="Locate me">◎</button>';
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

    // Displays direction of device
    const style = new Style({
      fill: new Fill({
        color: 'rgba(0, 0, 255, 0.2)',
      }),
      image: new Icon({
        src: '/src/assets/location-heading.svg',
        imgSize: [24, 24],
        rotateWithView: true,
      }),
    });
    layer.setStyle(style);

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
      locate.addEventListener('click', function () {
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