<template>
  <header>
    <h2 class="title">HWScheduler</h2>
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
</style>