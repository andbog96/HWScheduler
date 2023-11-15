<template>
  <div>
    <my-label>{{login}} profile</my-label>
    <my-button v-if="goToChannels" @click="visitChannels">Мои каналы</my-button>
    <my-button v-if="goToEvents" @click="visitEvents">Задачи</my-button>
    <my-button @click="logout">Выйти</my-button>
  </div>
</template>

<script lang="ts">
import router from "@/router/router";
import {mapActions} from "vuex";

export default {
  name: "UserProfile",
  props: {
    login: {
      type: String,
      required: true
    },
    goToChannels: {
      type: Boolean,
      default: true,
    },
    goToEvents: {
      type: Boolean,
      default: true
    }
  },
  methods: {
    ...mapActions({
      loggedOut: 'auth/loggedOut',
    }),
    async logout() {
      await this.loggedOut()
          .then(() => router.push('/'))
    },
    async visitChannels() {
      await router.push("/user/channel");
    },
    async visitEvents() {
      await router.push("/user/event")
    }
  }
}
</script>

<style scoped>

</style>