let fromPath = "/";
let selectedImage = "";
let gallery = [];

function setup() {
    if (sessionStorage.getItem("fromPath")) {
        fromPath = sessionStorage.getItem("fromPath")
    }

    if (sessionStorage.getItem("selectedImage")) {
        selectedImage = sessionStorage.getItem("selectedImage");
    }

    if (sessionStorage.getItem("gallery")) {
        gallery = sessionStorage.getItem("gallery");
    }
}

export function addImageToGallery(image) {
    setup();

    gallery.push(image);
}

export function getGallery() {
    setup();

    return gallery;
}

export function clearGallery() {
    sessionStorage.removeItem("gallery");
}

export function getSelectedImage() {
   setup();

   return selectedImage;
}

export function setSelectedImage(image) {
    sessionStorage.setItem("selectedImage", image);
}

export function removeSelectedImage() {
    sessionStorage.removeItem("selectedImage");
}

export function saveFromPath(fromPath) {
    sessionStorage.setItem("fromPath", fromPath);
}

export function getFromPath() {
    setup();

    return fromPath;
}