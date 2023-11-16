<template>
  <header>
    <h2 class="title">HWScheduler</h2>
    <div class="links">
      <HeaderLink v-if="authorized" class="link" to="/user/channel">Каналы</HeaderLink>
      <HeaderLink v-if="authorized" class="link" to="/user/event">Задачи</HeaderLink>
      <HeaderLink v-if="authorized" class="link" to="/" @click="logout">Выйти</HeaderLink>
    </div>
  </header>
</template>

<script lang="ts">
  import {defineComponent} from "vue";
  import store from '@/store';
  import {mapActions} from "vuex";
  import router from "@/router/router";
  import HeaderLink from "@/components/header/HeaderLink.vue";

  export default defineComponent({
    name: 'AppHeader',
    components: {HeaderLink},
    computed: {
      authorized: () => store.state.auth.isAuth,
      user: () => store.state.auth
    },
    methods: {
      ...mapActions({
        loggedOut: 'auth/loggedOut',
      }),
      async logout() {
        await this.loggedOut()
            .then(() => router.push('/'))
      },
    }
  });
</script>

<style scoped>
  header {
    background-color: #445b54;
    color: #fff;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 15px;
  }

  .title {
    font-size: 27px;
    /*font-family: 'Signika', sans-serif;*/
    font-family: 'Rouge Script', cursive;
    font-weight: 320;
    /*line-height: 25px;*/
  }


  .links * {
    margin: 0px 20px;
    justify-content: center;
  }
</style>