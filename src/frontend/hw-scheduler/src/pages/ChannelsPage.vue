<template>
  <div class="profile">
    <common-form>
      <my-label>Managing channels:</my-label>
      <common-list :not-empty="managing.length > 0">
        <list-item v-for="(cur, index) in managing" :key="index">
          <channel-item :channel="cur"/>
          <my-button @click="() => to_manage(cur)">Модерировать</my-button>
          <my-button @click="() => to_delete(cur)" :classes="'remove'">Удалить</my-button>
        </list-item>
        <list-item>
          <my-input placeholder="Имя канала: " v-model="this.create_name"></my-input>
          <my-button :classes="'add'" @click="create_channel">Создать</my-button>
        </list-item>
      </common-list>

      <my-label>Subscribed channels:</my-label>
      <common-list :not-empty="others.length > 0">
        <list-item v-for="(cur, index) in others" :key="index">
          <channel-item :channel="cur"/>
          <my-button @click="() => to_delete(cur)" :classes="'remove'">Отписаться</my-button>
        </list-item>
        <list-item>
          <my-input placeholder="Имя канала: " v-model="this.subscribe_name"></my-input>
          <my-button :classes="'add'" @click="subscribe_channel">Вступить</my-button>
        </list-item>
      </common-list>
    </common-form>
  </div>
</template>

<script lang="ts">
  import {defineComponent} from "vue";
  import {ChannelData, EventData, UserInfo, UserService} from "@/services/UserService";
  import store from "@/store";
  import MyLabel from "@/components/UI/primitives/MyLabel.vue";
  import ChannelItem from "@/components/UI/list/results/channelItem.vue";
  import CommonList from "@/components/UI/list/CommonList.vue";
  import ListItem from "@/components/UI/list/ListItem.vue";
  import router from "@/router/router";
  import updateDataMixin from "@/components/mixins/updateDataMixin";
  import MyButton from "@/components/UI/primitives/MyButton.vue";
  import MyInput from "@/components/UI/primitives/MyInput.vue";

  export default defineComponent({
    name: "SignInPage",
    components: {MyInput, MyButton, ChannelItem, MyLabel, CommonList, ListItem},
    mixins: [updateDataMixin],
    async created() {
      if (!store.state.auth.isAuth)
        await router.push("/");
    },
    data() : {info : UserInfo, create_name: string, subscribe_name: string} {
      return {
        info: store.state.auth.data,
        create_name: "",
        subscribe_name: ""
      }
    },
    computed: {
      us: () => new UserService(),
      userData: () => store.state.auth,
      managing: function() {
        return this.$data.info.channels.filter(ch => ch.is_admin)
      },
      others: function() {
        return this.$data.info.channels.filter(ch => !ch.is_admin)
      }
    },
    methods: {
      async to_manage(channel: ChannelData) {
        this.$router.push("/user/channel/" + channel.channel_id + '/event')
      },
      async to_delete(channel: ChannelData) {
        const res = await this.us.channel_delete(channel.channel_id);
        if (res === null) {
          await this.remove_channel(channel);
          this.info = store.state.auth.data;
        }
      },
      async create_channel() {
        const res = await this.us.channel_create(this.create_name)
        if ('is_admin' in res) {
          await this.update_data()
          this.info = store.state.auth.data
        }
      },
      async subscribe_channel() {
        const res = await this.us.channel_subscribe(this.subscribe_name)
        if ('message' in res) {
          await this.update_data()
          this.info = store.state.auth.data
        }
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