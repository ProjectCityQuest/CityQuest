import { createRouter, createWebHistory } from 'vue-router'
import LoginView from "@/views/LoginView.vue";
import LandingView from "@/views/LandingView.vue";
import RegisterView from "@/views/RegisterView.vue";
import EmailView from "@/views/EmailView.vue";
import MapView from "@/views/MapView.vue";

function getCookie(cname) {
  let name = cname + "=";
  let decodedCookie = decodeURIComponent(document.cookie);
  let ca = decodedCookie.split(';');
  for(let i = 0; i <ca.length; i++) {
    let c = ca[i];
    while (c.charAt(0) == ' ') {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
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
      component: LoginView
    },
    {
      path: '/registrierung',
      name: 'registrierung',
      component: RegisterView
    },
    {
      path: '/email-verifizierung/:email/:verificationKey',
      name: 'email-verifizierung-checking',
      component: EmailView,
      props: true
    },
    {
      path: '/email-verifizierung/:email',
      name: 'email-verifizierung-pending',
      component: EmailView,
      props: true
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

router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    console.log(getCookie('sessionKey'))
    if (false){
      next()
    } else {
      next({
        path: '/login'
      })
    }
  } else { next() }
})

export default router
