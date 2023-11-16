<template>
  <router-link :to="to" class="nav-link">
    <slot/>
  </router-link>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
export default defineComponent({
  props: {
    to: {
      type: String,
      required: true
    },
    exact: {
      type: Boolean,
      default: false
    }
  }
})
</script>

<style scoped>
a {
  height: 30px;
  line-height: 30px;

  color: white;
  text-decoration: underline;

}

a:hover {
  color: blue;
}
</style>