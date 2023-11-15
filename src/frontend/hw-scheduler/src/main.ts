import { createApp } from 'vue'

import App from './App.vue'
import store from '@/store/index'
import router from '@/router/router'
import components from '@/components/UI/index';


const app = createApp(App);

components.forEach(component => {
    app.component(component.name, component)
});

app
    .use(store)
    .use(router)
    .mount('#app');
