<template>
  <div class="body" v-if="notEmpty">
    <slot/>
  </div>
  <my-label v-else>
    Тут пока пусто
  </my-label>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import MyLabel from "@/components/UI/primitives/MyLabel.vue";
export default defineComponent({
  name: "CommonList",
  components: {MyLabel},
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