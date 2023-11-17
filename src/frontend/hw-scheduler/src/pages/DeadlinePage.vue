<template>
  <div class="deadlineList">
    <common-form>
      <common-list v-if="this.$data.info" :not-empty="this.$data.info.events.length > 0" class="item-container">
        <list-item v-for="(cur, index) in this.$data.info.events" :key="index" class="item">
          <event-item :event=cur :channel="searchChannel(cur.channel_id)">
            <clever-button text="TODO" :event="cur" @submit="(event, h, m) => complete(event, h, m)"/>
          </event-item>
        </list-item>
      </common-list>
    </common-form>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import store from "@/store";
import {UserInfo, UserService, ChannelData, EventData} from "@/services/UserService";
import EventItem from "@/components/UI/list/results/eventItem.vue";
import CommonList from "@/components/UI/list/CommonList.vue";
import ListItem from "@/components/UI/list/ListItem.vue";
import router from "@/router/router";
import updateDataMixin from "@/components/mixins/updateDataMixin";
import CleverButton from "@/components/UI/composits/CleverButton.vue";

export default defineComponent({
  name: 'DeadlinePage',
  mixins: [updateDataMixin],
  components: {
    CleverButton,
    EventItem,
    CommonList,
    ListItem
  },
  computed: {
    us: () => new UserService(),
    userData: () => store.state.auth
  },
  data() : {info : UserInfo | null} {
    return {
      info: null
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
    async complete(event: EventData, hours: number, mints: number) {
      if (!isNaN(hours) && !isNaN(mints)) {
        await this.us.event_completed(event, hours * 60 + mints)
        await this.remove_event(event);
        this.$data.info = store.state.auth.data;
      }
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

.deadlineList{
  width: 50%;
  /* margin: 3% 10%; */
}

  /* .item-container {
    display: flex;
    flex-wrap: wrap;
  } */

  .item {
    /* flex: 0 0 50%;
    box-sizing: border-box; */
    max-width: 450px;
    min-width: 450px;
    margin: 1%;
  }
</style>
