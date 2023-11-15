import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router';

const routes: RouteRecordRaw[] = [
    {
        path: '/',
        name: 'Sign In',
        component: () => import('@/pages/LoginPage.vue')
    },
    {
        path: '/user/event',
        name: 'Deadlines',
        component: () => import('@/pages/DeadlinePage.vue')
    },
    {
        path: '/user/channel',
        name: 'My Channels',
        component: () => import('@/pages/ChannelsPage.vue')
    },
    {
        path: '/user/channel/:ch_id/event',
        name: 'Channels Moderating',
        component: () => import('@/pages/ModeratingChannelsPage.vue')
    },
    {
        path: '/:catchAll(.*)',
        name: 'PageNotFound',
        component: () => import('../pages/ErrorNotFound.vue')
    },
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
});

export default router