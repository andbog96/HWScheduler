<template>
  <div class="value">
    <my-label>{{channel}}</my-label>
    <my-label>{{event.name}}</my-label>
    <label class="deadline">{{new Date(event.deadline).toUTCString()}}</label>
    <div class="button"><slot></slot></div>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {ChannelData, EventData} from "@/services/UserService";
import MyLabel from "@/components/UI/primitives/MyLabel.vue";
export default defineComponent({
  name: "EventItem",
  components: {MyLabel},
  props: {
    event: {
      type: Object as () => EventData,
      required: true
    },
    channel: {
      type: String,
      default: ""
    }
  }
})
</script>

<style scoped>
.value {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 170px;
  margin: 0 0;
}

.deadline {
  height: 40px;
  line-height: 40px;
  min-width: 100px;
  font-size: 18px;
  color: #5d0f02;
}
/*.button {*/
/*  margin: 0 auto;*/
/*}*/
</style>