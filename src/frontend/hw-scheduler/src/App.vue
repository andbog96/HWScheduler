<template>
  <AppHeader/>
  <main>
    <router-view />
  </main>
</template>

<script lang="ts">
  import { defineComponent } from 'vue';
  import AppHeader from "@/components/header/AppHeader.vue";

  export default defineComponent({
    components: {
      AppHeader
    }
  });
</script>

<style>
  * {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    font-size: 16px;
  }

  header {
    height: 60px;
    width: 100%;
  }

  main {
    display: flex;
    align-items: flex-start;
    justify-content: center;

    height: auto;
    min-height: 500px;
    width: 100%;
    margin: 20px auto;
  }
</style>