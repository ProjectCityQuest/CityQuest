let spots = [];

async function setup() {
    if (!sessionStorage.getItem("spots")) {
        await fetch("/src/assets/spots.json")
            .then(res => res.json())
            .then(data => spots = data);

        spots.forEach(spot => spot.id = "CityQuest" + spot.id);

        sessionStorage.setItem("spots", JSON.stringify(spots));
    } else {
        spots = JSON.parse(sessionStorage.getItem("spots"));
    }
}

export async function getAll() {
    await setup();
    return spots;
}

export async function getSpotByID(id) {
    await setup();

    for (let spot of spots) {
        if (spot.id === id) {
            return spot;
        }
    }

    return spots[0];
}