<template>
  <form @submit.prevent="formSubmitted" class="common-form">
    <slot/>
  </form>
</template>

<script lang="ts">
  import {defineComponent} from "vue";

  export default defineComponent({
    name: 'CommonForm',
    methods: {
      formSubmitted() {
        this.$emit('submitted')
      }
    }
  })
</script>

<style scoped>
  .common-form {
    width: 100%;
    /*height: auto;*/

    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

    background-color: #f8f8f8;
    border: 2px solid #ccc;
    border-radius: 10px;

    padding: 20px 40px;
    margin: 0 auto;
  }
</style>