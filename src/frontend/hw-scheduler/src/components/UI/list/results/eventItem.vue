<template>
  <div class="value">
    <my-label>{{channel}}</my-label>
    <my-label>{{event.name}}</my-label>
    <my-label>{{new Date(event.deadline)}}</my-label>
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
  justify-content: space-between;
  align-items: flex-start;
  flex-wrap: wrap;
}
</style>