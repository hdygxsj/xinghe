import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Milestones from '../views/Milestones.vue'
import Certificates from '../views/Certificates.vue'
import AIAssistant from '../views/AIAssistant.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Mentors from '../views/Mentors.vue'
import CareerPlans from '../views/CareerPlans.vue'
import Skills from '../views/Skills.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/milestones',
    name: 'Milestones',
    component: Milestones
  },
  {
    path: '/certificates',
    name: 'Certificates',
    component: Certificates
  },
  {
    path: '/ai-assistant',
    name: 'AIAssistant',
    component: AIAssistant
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/mentors',
    name: 'Mentors',
    component: Mentors
  },
  {
    path: '/career-plans',
    name: 'CareerPlans',
    component: CareerPlans
  },
  {
    path: '/skills',
    name: 'Skills',
    component: Skills
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router