<template>
  <div>
    <div v-if="!to_change" @click="() => to_change=true">
      <my-button>{{text}}</my-button>
    </div>
    <div v-else class="toAdd">
      <my-input placeholder="Час: " v-model="hours" class="input"/>
      <my-input placeholder="Мин:" v-model="minutes" class="input"/>
      <my-label @click="() => {
        const h = parseInt(this.hours)
        const m = parseInt(this.minutes)
        hours = minutes = ''
        to_change = false;
        this.$emit('submit', event, h, m);
      }" class="button">SEND</my-label>
      <my-label @click="() => {to_change = false; hours=minutes='';}" class="button">Back</my-label>
    </div>
  </div>
</template>

<script lang="ts">
import MyInput from "@/components/UI/primitives/MyInput.vue";
import MyButton from "@/components/UI/primitives/MyButton.vue";
import {EventData} from "@/services/UserService";

export default {
  name: "CleverButton",
  components: {MyButton, MyInput},
  props: {
    text: {
      type: String,
      required: true
    },
    event: {
      type: Object as () => EventData,
      required: true
    }
  },
  data() {
    return {
      to_change: false,
      hours: "",
      minutes: ""
    }
  }
}
</script>

<style scoped>
  .toAdd {
    display: flex;
    flex-direction: row;
    justify-content: left;
  }
  .button {
    background-color: #445b54;
    color: #d8d8d8;
    width: 100px;
    height: 45px;
    line-height: 40px;

    border: none;
    border-radius: 4px;

    font-weight: bold;
    cursor: pointer;
  }
  .button:hover {
    background-color: #471002;;
  }
  .button:active {
    background-color: #0069d9;
    transform: translateY(1px);
  }

  .toAdd * {
    margin: 5px;
  }
</style>