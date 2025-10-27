let spots = [];

async function setup() {
    // if (sessionStorage.getItem("spots")) {
        // spots = JSON.parse(sessionStorage.getItem("spots"));
        // return;
    // }

    await fetch(`https://${window.location.hostname}/api/getspots`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'sessionKey': getCookie('sessionKey')
        },
        withCredentials: true,
        credentials: 'same-origin',
    })
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
    sessionStorage.setItem("spotsInRange", JSON.stringify(spotsInRange));
}

export function getSpotsInRange() {
    return JSON.parse(sessionStorage.getItem("spotsInRange"));
}

function getCookie(cname) {
    let name = cname + "=";
    let decodedCookie = decodeURIComponent(document.cookie);
    let ca = decodedCookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return false;
}
