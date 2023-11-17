<template>
  <div class="profile">
    <common-form>
      <common-list :not-empty="true">
        <list-item class="toAdd">
          <my-input placeholder="Новый канал: " v-model="this.create_name"></my-input>
          <my-button :classes="'add'" @click="create_channel">Создать</my-button>
        </list-item>
        <list-item class="toAdd">
          <my-input placeholder="Имя канала: " v-model="this.subscribe_name"></my-input>
          <my-button :classes="'add'" @click="subscribe_channel">Вступить</my-button>
        </list-item>
        <list-item v-for="(cur, index) in channels" :key="index" class="listItem">
          <channel-item :channel="cur"/>
          <div class="actions">
            <my-button v-if="cur.is_admin" @click="() => to_manage(cur)" :classes="'edit'"></my-button>
            <my-button @click="() => to_delete(cur)" :classes="'remove'"></my-button>
          </div>
        </list-item>
      </common-list>
    </common-form>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {ChannelData, UserInfo, UserService} from "@/services/UserService";
import store from "@/store";
import ChannelItem from "@/components/UI/list/results/channelItem.vue";
import CommonList from "@/components/UI/list/CommonList.vue";
import ListItem from "@/components/UI/list/ListItem.vue";
import router from "@/router/router";
import updateDataMixin from "@/components/mixins/updateDataMixin";
import MyButton from "@/components/UI/primitives/MyButton.vue";
import MyInput from "@/components/UI/primitives/MyInput.vue";

export default defineComponent({
  name: "SignInPage",
  components: {MyInput, MyButton, ChannelItem, CommonList, ListItem},
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
    channels: () => store.state.auth.data.channels
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
  .toAdd {
    display: flex;
  }
  .buttons button {
    margin: 20px 10px;
  }

  .listItem {
    border: 3px solid #ccc;
    border-radius: 8px;
    margin: 3% 0;
    padding: 10px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .actions {
    display: flex;
  }
</style>