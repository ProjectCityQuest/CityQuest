import {Fill, Icon, Stroke, Style, Text} from "ol/style";

export function getSpotStyle(isInRange, isDiscovered) {
    let icon;

    if (isInRange) {
        if (isDiscovered) {
            icon = new Icon({
                src: '/spot/in_range_discovered.svg',
                imgSize: [98, 98]
            });
        } else {
            icon = new Icon({
                src: '/spot/in_range.svg',
                imgSize: [98, 98]
            });
        }
    } else {
        if (isDiscovered) {
            icon = new Icon({
                src: '/spot/discovered.svg',
                imgSize: [80, 80]
            });
        } else {
            icon = new Icon({
                src: '/spot/spot.svg',
                imgSize: [80, 80]
            });
        }
    }

    return new Style({
        image: icon
    });
}

export function getClusterStyle(text) {
    return new Style({
        image: new Icon({
            src: '/spot/cluster.svg',
            imgSize: [80, 80]
        }),
        text: new Text({
            text: [text, "14px Berlin Sans FB"],
            offsetY: -20,
            fill: new Fill({
                color: '#fff',
            }),
        }),
    });
}

export function getPositionStyle() {
    return new Style({
        image: new Icon({
            src: '/location.svg',
            imgSize: [24, 24],
            rotateWithView: true,
        }),
    });
}

export function getRangeCircleStyle() {
    return new Style({
        stroke: new Stroke({
            color: 'blue',
            width: 2
        }),
        fill: new Fill({
            color: 'rgba(0, 0, 255, 0.1)'
        })
    });
}