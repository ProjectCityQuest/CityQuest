import { createRouter, createWebHistory } from 'vue-router'
import LoginView from "@/components/views/LoginView.vue";
import LandingView from "@/components/views/LandingView.vue";

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
    }
  ]
})

export default router
