<template>
  <div class="value">
    <my-label>{{channel.name}}</my-label>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {ChannelData} from "@/services/UserService";
import MyLabel from "@/components/UI/primitives/MyLabel.vue";
export default defineComponent({
  name: "ChannelItem",
  components: {MyLabel},
  props: {
    channel: {
      type: Object as () => ChannelData,
      required: true
    }
  }
})
</script>

<style scoped>

</style>