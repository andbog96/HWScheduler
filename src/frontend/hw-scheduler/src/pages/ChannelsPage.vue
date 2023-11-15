<template>
  <div class="profile">
    <common-form>
      <user-profile :login="userData.login" :go-to-channels="false"/>

      <my-label>Managing channels:</my-label>
      <common-list :not-empty="managing.length > 0">
        <list-item v-for="(cur, index) in managing" :key="index">
          <channel-item :channel="cur" @click="() => to_manage(cur)"/>
          <my-button @click="() => to_manage(cur)" :classes="'remove'">Delete</my-button>
        </list-item>
      </common-list>

      <my-label>Subscribed channels:</my-label>
      <common-list :not-empty="others.length > 0">
        <list-item v-for="(cur, index) in others" :key="index">
          <channel-item :channel="cur"/>
          <my-button @click="() => to_manage(cur)" :classes="'remove'">Delete</my-button>
        </list-item>
      </common-list>
    </common-form>
  </div>
</template>

<script lang="ts">
  import {defineComponent} from "vue";
  import {ChannelData, UserInfo, UserService} from "@/services/UserService";
  import errorMixin from "@/components/mixins/errorMixin";
  import store from "@/store";
  import MyLabel from "@/components/UI/primitives/MyLabel.vue";
  import ChannelItem from "@/components/UI/list/results/channelItem.vue";
  import CommonList from "@/components/UI/list/CommonList.vue";
  import ListItem from "@/components/UI/list/ListItem.vue";
  import router from "@/router/router";
  import UserProfile from "@/components/UI/composits/UserProfile.vue";

  export default defineComponent({
    name: "SignInPage",
    components: {UserProfile, ChannelItem, MyLabel, CommonList, ListItem},
    mixins: [errorMixin],
    async created() {
      if (!store.state.auth.isAuth)
        await router.push("/");
    },
    computed: {
      us: () => new UserService(),
      userData: () => store.state.auth,
      managing: () => store.state.auth.data.channels.filter(ch => ch.is_admin),
      others: () => store.state.auth.data.channels.filter(ch => !ch.is_admin)
    },
    methods: {
      async to_manage(channel: ChannelData) {
        this.$router.push("/user/channel/" + channel.channel_id + '/event')
      }
    }
  })
</script>

<style scoped>
  .sign_in_page {
    width: 500px;
    height: 60%;
    margin: auto 0;
  }

  .buttons button {
    margin: 20px 10px;
  }

  #li, #pi {
    margin-top: 10px;
    margin-bottom: 30px;
  }
</style>