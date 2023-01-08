import { createRouter, createWebHistory } from 'vue-router';

import TeamsList from '@/components/teams/TeamsList';
import UsersList from '@/components/users/UsersList';
import TeamMembers from '@/components/teams/TeamMembers';
import NotFound from '@/components/nav/NotFound';
import TeamsFooter from '@/components/teams/TeamsFooter';
import UsersFooter from '@/components/users/UsersFooter';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/teams', },
    { path: '/teams', //our-domain.com/teams ...=>
      components: { default: TeamsList, footer: TeamsFooter },
      meta: { needsAuth: true,},
      name: 'teams',
      alias: '/',
      children: [
        { name: 'team-members', path: ':teamId', component: TeamMembers, props: true } ,
      ],
    },
    { path: '/users', //our-domain.com/uses ...=>
      components: {default: UsersList, footer: UsersFooter},
      beforeEnter(to, from, next) {
        console.log('users beforeEnter');
        console.log(to, from);
        next();
      },
    },
    { path: '/:notFound(.*)', component: NotFound}
  ],
  linkActiveClass: 'active',
  scrollBehavior(_, _2 ,savedPosition) {
    // console.log(to, from, savedPosition);
    if (savedPosition) {
      return savedPosition;
    }
    return { left: 0, top: 0};
  },
});

router.beforeEach(function(to, from, next) {
  console.log('Global beforeEach')
  console.log(to, from);
  if (to.meta.needsAuth) {
    console.log('Needs auth!');
    next();
  }
  // if (to.name === 'team-members') {
  //   next();
  // } else {
  //   next({name: 'team-members', params: {teamId: 't2'}});
  // }
  next();
});

router.afterEach(function(to, from) {
  console.log(to, from);
  //sending analytics data;
});

export default router;
