import { createRouter, createWebHistory } from 'vue-router'
import LoginView from "@/views/LoginView.vue";
import LandingView from "@/views/LandingView.vue";
import RegisterView from "@/views/RegisterView.vue";
import EmailView from "@/views/EmailView.vue";

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
    }
  ]
})

export default router
