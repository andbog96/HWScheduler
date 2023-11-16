<template>
  <div class="deadlineList">
    <common-form>
      <div v-if="to_submit">
        <event-item :event="event"/>
        <label-input v-model="time" placeholder="Затраченное время: " label="Время:"/>
        <my-button :classes="'add'" @click="send">Отправить</my-button>
        <my-button :classes="'remove'" @click="revert">Отмена</my-button>
      </div>
      <common-list v-if="this.$data.info" :not-empty="this.$data.info.events.length > 0">
        <list-item v-for="(cur, index) in this.$data.info.events" :key="index">
          <event-item :event=cur :channel="searchChannel(cur.channel_id)"/>
          <my-button @click="() => complete(cur)">Submit</my-button>
        </list-item>
      </common-list>
    </common-form>
  </div>
</template>

<script lang="ts">
  import { defineComponent } from 'vue';
  import store from "@/store";
  import {UserInfo, UserService, ChannelData, EventData} from "@/services/UserService";
  import MyButton from "@/components/UI/primitives/MyButton.vue";
  import EventItem from "@/components/UI/list/results/eventItem.vue";
  import CommonList from "@/components/UI/list/CommonList.vue";
  import ListItem from "@/components/UI/list/ListItem.vue";
  import router from "@/router/router";
  import UserProfile from "@/components/UI/composits/UserProfile.vue";
  import updateDataMixin from "@/components/mixins/updateDataMixin";
  import LabelInput from "@/components/UI/composits/LabelInput.vue";

  export default defineComponent({
    name: 'DeadlinePage',
    mixins: [updateDataMixin],
    components: {
      LabelInput,
      MyButton,
      EventItem,
      CommonList,
      ListItem
    },
    computed: {
      us: () => new UserService(),
      userData: () => store.state.auth
    },
    data() : {info : UserInfo | null, to_submit: boolean, event : EventData | null, time: string} {
      return {
        info: null,
        to_submit: false,
        event: null,
        time: "",
      }
    },
    async created() {
      if (!store.state.auth.isAuth)
        await router.push("/");

      const res = await this.us.userInfo() as UserInfo
      this.$data.info = res
      await this.saveData({data: res})
    },
    methods: {
      async complete(event: EventData) {
        this.$data.to_submit = true
        this.$data.event = event
      },
      async send() {
        const res = parseInt(this.$data.time)
        if (this.$data.event && !isNaN(res)) {
          await this.us.event_completed(this.$data.event, res)
          await this.remove_event(this.$data.event);
          this.$data.info = store.state.auth.data;
        }
        this.revert()
      },
      revert() {
        this.$data.to_submit = false;
        this.$data.time = ""
      },
      searchChannel(id: number): string {
        let response = "";
        if (this.$data.info) {
          const found = this.$data.info.channels.find((channel: ChannelData) => channel.channel_id == id);
          if (found)
            response = found.name;
        }
        return response
      }
    }
});
</script>

<style scoped>
.profile {
  display: flex;
  width: 500px;
  height: auto;
  margin: auto 0;
}

.userDataContainer {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.userDataContainer button {
  margin-top: 20px;
}
list-item{
  display: flex;
  justify-content: space-between;
}
.deadlineList{
  width: 80%;
  margin: 3% 10%;
}
</style>
