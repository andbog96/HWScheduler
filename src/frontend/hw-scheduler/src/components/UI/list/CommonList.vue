<template>
  <div class="list">
    <div class="body" v-if="notEmpty">
      <slot/>
    </div>
    <div class="body" v-else>
      <framed-list-item>
        <my-label class="empty_msg">
          No results
        </my-label>
      </framed-list-item>
    </div>
  </div>
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