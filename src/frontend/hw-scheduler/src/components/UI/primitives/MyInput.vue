<template>
  <input class="input_field"
         :type="type"
         :value="modelValue"
         :placeholder="placeholder"
         @input="updateInput"
         autocomplete="off"
  />
</template>

<script lang="ts">
  import InputMixin from "@/components/mixins/InputMixin";

  export default {
    name: "MyInput",
    mixins: [InputMixin]
  }
</script>

<style scoped>
  .input_field{
    width: 100%;
    height: 40px;

    border: none;
    border-bottom: 2px solid #ccc;
    background-color: #f8f8f8;
    font-size: 18px;
    font-family: 'Lato', italic;

    padding: 5px;
  }
</style>