import {createRouter, createWebHistory} from 'vue-router'
import LoginView from "@/views/LoginView.vue";
import LandingView from "@/views/LandingView.vue";
import RegisterView from "@/views/RegisterView.vue";
import EmailView from "@/views/EmailView.vue";
import MapView from "@/views/MapView.vue";

// gets the value of a cookie by name
// if the cookie doesn't exist, the function returns 'false'
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

// gets the value of the sessionKey cookie, then check its validity by sending it to the backend
// it returns a boolean, which indicates, if the response status of this request is 200 or not
async function checkSessionKey() {
    const response = await fetch('http://127.0.0.1:8080/api/users', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'sessionKey': getCookie('sessionKey')
        },
        withCredentials: true,
        credentials: 'same-origin'
    })
    return response.status === 200;
}

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'landing',
            component: LandingView
        },
        {
            path: '/login',
            name: 'login',
            component: LoginView,
            meta: {
                requiresNoCookie: true
            }
        },
        {
            path: '/registrierung',
            name: 'registrierung',
            component: RegisterView,
            meta: {
                requiresNoCookie: true
            }
        },
        {
            path: '/email-verifizierung/:email/:verificationKey',
            name: 'email-verifizierung-checking',
            component: EmailView,
            props: true,
            meta: {
                requiresNoCookie: true
            }
        },
        {
            path: '/email-verifizierung/:email',
            name: 'email-verifizierung-pending',
            component: EmailView,
            props: true,
            meta: {
                requiresNoCookie: true
            }
        },
        {
            path: '/map',
            name: 'map',
            component: MapView,
            meta: {
                requiresAuth: true
            }
        }
    ]
})

// runs on all path requests which have the meta-tag 'requiresAuth' set to 'true'
// checks if the stored sessionKey is valid
// if so the request is permitted, else the user gets redirected to the '/login' path
router.beforeEach(async (to, from, next) => {
    if (to.matched.some(record => record.meta.requiresAuth)) {
        if (await checkSessionKey()) {
            next()
        } else {
            document.cookie = "sessionKey= ; expires = Thu, 01 Jan 1970 00:00:00 GMT"
            next({
                path: '/login'
            })
        }
    } else {
        next()
    }
})

// runs on all path requests which have the meta-tag 'requiresNoCookie' set to 'true'
// checks if there is not sessionKey or the stored sessionKey is invalid
// if so the request is permitted, else the user gets redirected to the '/map' path
router.beforeEach(async (to, from, next) => {
    if (to.matched.some(record => record.meta.requiresNoCookie)) {
        if (document.cookie.indexOf('sessionKey=') > -1) {
            if (await checkSessionKey()) {
                next({
                    path: '/map'
                })
            } else {
                document.cookie = "sessionKey= ; expires = Thu, 01 Jan 1970 00:00:00 GMT"
                next()
            }
        } else {
            next()
        }
    } else {
        next()
    }
})

export default router
