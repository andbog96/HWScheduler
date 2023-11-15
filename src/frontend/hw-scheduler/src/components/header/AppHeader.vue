<template>
  <header>
    <h2>HWScheduler</h2>
  </header>
</template>

<script lang="ts">
  import {defineComponent} from "vue";
  import store from '@/store';

  export default defineComponent({
    name: 'AppHeader',
    computed: {
      authorized: () => store.state.auth.isAuth
    },
  });
</script>

<style scoped>
  header {
    background-color: #333;
    color: #fff;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 15px;
  }

  header h2 {
    font-size: 22px;
  }
</style>