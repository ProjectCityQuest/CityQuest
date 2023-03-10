import { createRouter, createWebHistory } from 'vue-router'
import LoginView from "@/components/views/LoginView.vue";
import LandingView from "@/components/views/LandingView.vue";
import RegisterView from "@/components/views/RegisterView.vue";

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
      path: '/register',
      name: 'register',
      component: RegisterView
    }
  ]
})

export default router
