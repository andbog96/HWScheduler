<template>
  <div class="element">
    <slot/>
  </div>
</template>

<script lang="ts">
  import {defineComponent} from "vue";
  export default defineComponent({
    name: "FramedListItem",
  })
</script>

<style scoped>
  .element {
    border: 3px solid #ccc;
  }
  .element:hover {
    background-color: #d8d8d8;
    cursor: pointer;
  }
</style>