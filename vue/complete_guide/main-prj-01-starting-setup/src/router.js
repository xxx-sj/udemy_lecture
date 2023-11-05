import { createRouter, createWebHistory } from 'vue-router'

// import CoachDetail from '@/pages/coaches/CoachDetail';
import CoachesList from '@/pages/coaches/CoachesList';
// import CoachRegistration from '@/pages/coaches/CoachRegistration';

// import ContactCoach from '@/pages/requests/ContactCoach';
// import RequestsReceived from '@/pages/requests/RequestsReceived';
import NotFound from '@/pages/NotFound';
// import UserAuth from '@/pages/auth/UserAuth';
import store from './storae/index'

const CoachDetail = () => import('@/pages/coaches/CoachDetail');
const CoachRegistration = () => import('@/pages/coaches/CoachRegistration');
const ContactCoach = () => import('@/pages/requests/ContactCoach');
const RequestsReceived = () => import('@/pages/requests/RequestsReceived');
const UserAuth = () => import('@/pages/auth/UserAuth');

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/coaches' },
    { path: '/coaches', component: CoachesList },
    { path: '/coaches/:id', props:true, component: CoachDetail, children: [
        { path: 'contact', component: ContactCoach }, // /coaches/c1/contact
      ] },
    { path: '/register', component: CoachRegistration, meta: { requiresAuth: true} },
    { path: '/requests', component: RequestsReceived,  meta: { requiresAuth: true} },
    { path: '/auth', component: UserAuth,  meta: { requiresUnauth: true} },
    { path: '/:notFound(.*)', component: NotFound },

  ],
});

router.beforeEach((to, _, next) => {
  if (to.meta.requiresAuth && !store.getters['auth/isAuthenticated']) {
    next('/auth');
  } else if (to.meta.requiresUnauth && store.getters['auth/isAuthenticated']) {
    next('/coaches');
  } else {
    next();
  }
});

export default router;
