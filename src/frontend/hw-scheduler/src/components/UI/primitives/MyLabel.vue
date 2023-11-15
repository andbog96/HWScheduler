<template>
  <div class="bold-label"><slot/></div>
</template>

<script lang="ts">
  import {defineComponent} from "vue";
  export default defineComponent({
    name: 'MyLabel'
  })
</script>

<style scoped>
  .bold-label {
    height: 40px;
    line-height: 40px;
    min-width: 100px;
    font-size: 18px;

    font-weight: bold;
  }
</style>