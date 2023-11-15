<template>
  <div class="input_area">
    <my-label><slot/></my-label>
    <textarea
           :value="modelValue"
           :placeholder="placeholder"
           @input="updateInput"
           autocomplete="off"
    />
  </div>
</template>

<script lang="ts">
  import {defineComponent} from "vue";
  import InputMixin from "@/components/mixins/InputMixin";

  export default defineComponent({
    name: "MyArea",
    mixins: [InputMixin]
  })
</script>

<style scoped>
  .input_area {
    display: flex;
    justify-content: flex-start;
    width: 100%;
    padding: 5px;
  }

  textarea {
    width: 100%;
    height: 120px;

    background-color: #f8f8f8;
    font-size: 18px;

    padding: 5px;
  }
</style>