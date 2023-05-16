let spots = [];

async function setup() {
    // if (sessionStorage.getItem("spots")) {
        // spots = JSON.parse(sessionStorage.getItem("spots"));
        // return;
    // }

    await fetch("/src/assets/spots.json")
        .then(res => res.json())
        .then(data => spots = data);

    // sessionStorage.setItem("spots", JSON.stringify(spots));
}

export async function getAll() {
    await setup();
    return spots;
}

export async function getSpotByID(id) {
    await setup();

    for (let spot of spots) {
        if (String(spot.id).toLowerCase() === id.toLowerCase()) {
            return spot;
        }
    }

    return spots[0];
}

export function saveSpotsInRange(spotsInRange) {
    sessionStorage.setItem("spotsInRange", spotsInRange)
}

export function getSpotsInRange() {
    return sessionStorage.getItem("spotsInRange");
}