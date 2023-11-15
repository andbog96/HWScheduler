<template>
  <button
      class="my-button"
      :disabled="disabled"
      :class="classes"
  >
    <slot></slot>
  </button>
</template>

<script lang="ts">
  import { defineComponent } from 'vue';
  import ButtonMixin from "@/components/mixins/buttonMixin";

  export default defineComponent({
    name: 'MyButton',
    mixins: [ButtonMixin],
    props: {
      classes: {
        type: String as () => 'my-button' | 'remove' | 'add',
        default: 'my-button'
      }
    }
  });
</script>

<style scoped>
  button {
    width: 100px;
    height: 40px;
    line-height: 40px;

    border: none;
    border-radius: 4px;

    font-weight: bold;
    cursor: pointer;
  }

  .my-button {
    background-color: #007bff;
    color: #fff;
  }
  .my-button:hover {
    background-color: #0069d9;
  }
  .my-button:active {
    background-color: #0069d9;
    transform: translateY(1px);
  }
  .my-button:disabled {
    background-color: #688296;
  }

  .remove {
    background-color: #b60808;
    color: #fff;
  }
  .remove:hover {
    background-color: #b63838;
  }
  .remove:active {
    background-color: #b63838;
    transform: translateY(1px);
  }

  .add {
    color: #fff;
    background-color: #008000;
  }
  .add:hover {
    background-color: #00A020;
  }
  .add:active {
    background-color: #00A020;
    transform: translateY(1px);
  }
</style>