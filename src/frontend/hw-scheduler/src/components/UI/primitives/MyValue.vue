<template>
  <div class="string-value"><slot/></div>
</template>

<script lang="ts">
  import {defineComponent} from "vue";
  export default defineComponent({
    name: 'MyValue'
  })
</script>

<style scoped>
  .string-value {
    overflow-wrap: break-word;

    height: auto;
    line-height: 40px;
  }
</style>