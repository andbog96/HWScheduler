<template>
  <div class="labeled_input">
    <my-label class="label_field">{{label}}</my-label>
    <my-input class="input_field"
           :type="type"
           :value="modelValue"
           :placeholder="placeholder"
           @input="updateInput"
           autocomplete="off"
    />
  </div>
</template>

<script lang="ts">
  import { defineComponent } from "vue";
  import InputMixin from "@/components/mixins/InputMixin";

  export default defineComponent({
    name: 'LabelInput',
    mixins: [InputMixin],
    props: {
      label: {
        type: String,
        default: null
      }
    }
  })
</script>

<style scoped>
  .labeled_input {
    display: flex;
    flex-direction: row;
    width: 100%;
    height: 40px;
  }

  .label_field {
    flex-grow: 1;
  }
  .input_field {
    flex-grow: 2;
  }
</style>