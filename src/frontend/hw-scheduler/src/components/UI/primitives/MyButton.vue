<template>
  <div v-if="classes === 'remove'">
    <button class="remove">
      <img src="../../../assets/delete2.png" alt="Delete">
    </button>
  </div>
  <div v-else-if="classes === 'edit'">
    <button class="edit">
      <img src="../../../assets/edit.png" alt="Edit">
    </button>
  </div>
  <div v-else>
    <button
        class="my-button"
        :disabled="disabled"
        :class="classes"
    >
      <slot></slot>
    </button>
  </div>
</template>

<script lang="ts">
  import { defineComponent } from 'vue';
  import ButtonMixin from "@/components/mixins/buttonMixin";

  export default defineComponent({
    name: 'MyButton',
    mixins: [ButtonMixin],
    props: {
      classes: {
        type: String as () => 'my-button' | 'remove' | 'add' | 'edit',
        default: 'my-button'
      }
    }
  });
</script>

<style scoped>
  .my-button {
    background-color: #445b54;
    /*color: #fbf2d5;*/
    color: #d8d8d8;
    width: 100px;
    height: 45px;
    line-height: 40px;

    border: none;
    border-radius: 4px;

    font-weight: bold;
    cursor: pointer;
  }
  .my-button:hover {
    background-color: #471002;;
  }
  .my-button:active {
    background-color: #0069d9;
    transform: translateY(1px);
  }
  .my-button:disabled {
    background-color: #688296;
  }

  .remove, .edit {
    background: none;
    border: none;
    padding: 0;
    cursor: pointer;
  }

  .remove img {
    width: 35px;
    height: 35px;
  }
  .edit img {
    width: 30px;
    height: 30px;
  }
  /*.remove:hover {*/
  /*  background-color: #b63838;*/
  /*}*/
  /*.remove:active {*/
  /*  background-color: #b63838;*/
  /*  transform: translateY(1px);*/
  /*}*/

  .add {
    color: #fff;
  }
  .add:hover {
  }
  .add:active {
    transform: translateY(1px);
  }
</style>