<template>
  <common-form>
    <user-profile :login="userData.login"/>
    <my-label>Manage {{channel.name}} events: </my-label>
    <common-list :not-empty="events.length > 0">
      <list-item v-for="(cur, index) in events" :key="index">
        <event-item :event="cur" :channel="channel.name"/>
      </list-item>
    </common-list>
  </common-form>
</template>

<script lang="ts">
  import {defineComponent} from "vue";
  import {EventData, UserService} from '@/services/UserService';
  import store from "@/store";
  import router from "@/router/router";
  import UserProfile from "@/components/UI/composits/UserProfile.vue";
  import MyLabel from "@/components/UI/primitives/MyLabel.vue";
  import EventItem from "@/components/UI/list/results/eventItem.vue";
  import CommonList from "@/components/UI/list/CommonList.vue";
  import ListItem from "@/components/UI/list/ListItem.vue";

  export default defineComponent({
    name: "SignUpPage",
    components: {MyLabel, UserProfile, EventItem, CommonList, ListItem},
    async created() {
      if (!store.state.auth.isAuth)
        await router.push("/");
    },
    data() {
      return {
        channel: ((channel: number) => store.state.auth.data.channels.find(ch => ch.channel_id === channel)) (
            parseInt(this.$route.params.ch_id as string)
        )
      }
    },
    computed: {
      us: () => new UserService(),
      userData: () => store.state.auth,
      events: function() {
        let res: EventData[] = []
        if (this.channel !== undefined) {
          const ch_id = this.channel.channel_id;
          res = store.state.auth.data.events.filter(event => event.channel_id === ch_id)
        }
        return res;
      }
    },
  })
</script>

<style scoped>
  .sign_up_page {
    display: flex;
    width: 500px;
    height: auto;
    margin: auto 0;
  }

  #submit_form > * {
    margin: 15px 0px;
  }
</style>