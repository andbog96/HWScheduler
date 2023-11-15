<template>
  <div class="labeled-value">
    <my-label>{{label}}</my-label>
    <my-value>{{value}}</my-value>
  </div>
</template>

<script lang="ts">
  import {defineComponent} from "vue";
  export default defineComponent({
    name: 'LabelValue',
    props: {
      value: {
        type: String,
        required: true
      },
      label: {
        type: String,
        required: true
      }
    }
  })
</script>

<style scoped>
  .labeled-value {
    display: flex;
    align-items: start;
    margin-bottom: 10px;
    height: 40px;
  }
</style>