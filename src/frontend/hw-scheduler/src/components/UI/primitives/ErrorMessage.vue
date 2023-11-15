<template>
  <span class="error-message"><slot/></span>
</template>

<script lang="ts">
  import {defineComponent} from "vue";
  import store from "@/store";
  import router from "@/router/router";
  export default defineComponent({
    name: "ErrorMessage",
    async created() {
      if (!store.state.auth.isAuth)
        await router.push("/");
    }
  })
</script>

<style scoped>
  .error-message {
    font-weight: bold;
    height: 40px;
    line-height: 40px;
    color: #b63838;
  }
</style>