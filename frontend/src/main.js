import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createStore } from 'vuex'
import './assets/base.scss'

const app = createApp(App)

const store = createStore({
    state () {
        return {
            gallery: [],
            seletedImage : ""
        }
    },
    mutations: {
        setGallery(state, newGallery){
            state.gallery = newGallery
        },
        setSelectedImage(state, seletedImage){
            state.seletedImage = seletedImage
        },
        addImage(state, data){
            state.gallery.push(data)
        },
        resetGallery(state){
            state.gallery = []
        }
    }
})

app.use(router)
app.use(store)
app.mount('#app')
