<template>
  <div class="body" v-if="notEmpty">
    <slot/>
  </div>
  <framed-list-item v-else>
    <my-label class="empty_msg">
      No results
    </my-label>
  </framed-list-item>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import MyLabel from "@/components/UI/primitives/MyLabel.vue";
import FramedListItem from "@/components/UI/list/FramedListItem.vue";
export default defineComponent({
  name: "CommonList",
  components: {FramedListItem, MyLabel},
  props: {
    notEmpty: {
      type: Boolean,
      required: true
    },
  }
})
</script>

<style scoped>
.empty_msg {
  padding: 0 10px;
}
</style>