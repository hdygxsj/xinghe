import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Milestones from '../views/Milestones.vue'
import Certificates from '../views/Certificates.vue'
import AIAssistant from '../views/AIAssistant.vue'

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
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router