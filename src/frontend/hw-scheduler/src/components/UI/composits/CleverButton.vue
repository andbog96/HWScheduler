<template>
  <div>
    <div v-if="!to_change" @click="() => to_change=true">
      <my-button>{{text}}</my-button>
    </div>
    <div v-else>
      <my-input placeholder="Час: " v-model="hours"/>
      <my-input placeholder="Мин:" v-model="minutes"/>
      <my-button @click="() => {
        const h = parseInt(this.hours)
        const m = parseInt(this.minutes)
        hours = minutes = ''
        to_change = false;
        this.$emit('submit', event, h, m);
      }">DONE</my-button>
      <my-button @click="() => {to_change = false; hours=minutes='';}">Отмена</my-button>
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

</style>