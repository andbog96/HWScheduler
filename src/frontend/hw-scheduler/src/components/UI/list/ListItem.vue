<template>
  <div
      class="element"
      :class="{ 'hovered': hover }"
  >
    <slot/>
  </div>
</template>

<script lang="ts">
  import {defineComponent} from "vue";
  export default defineComponent({
    name: "ListItem",
    props: {
      hover: {
        type: Boolean,
        default: false
      }
    }
  })
</script>

<style scoped>
  .element {
    border-bottom: 3px solid #ccc;
    margin-top: -3px;
  }
  .hovered:hover {
    background-color: #d8d8d8;
    cursor: pointer;
  }
</style>