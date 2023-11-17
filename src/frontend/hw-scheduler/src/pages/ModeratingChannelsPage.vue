<template>
  <common-form class="form">
    <my-label>Управление: {{channel.name}}</my-label>

    <list-item id="create">
      <div id="add-box">
        <label-input placeholder="Название" label="Задание: " v-model="in_name"></label-input>
        <my-button :classes="'add'" @click="create_event">Создать</my-button>
      </div>
      <my-area placeholder="Описание задания" v-model="in_description">Описание:</my-area>
      <label-input type="datetime-local" v-model="time" label="Дедлайн: "/>
    </list-item>

    <common-list :not-empty="true" class="item-container">
      <list-item v-for="(cur, index) in events" :key="index" class="item">
        <event-item :event="cur" :channel="channel.name">
          <my-button :classes="'remove'" @click="() => remove(cur)">Удалить</my-button>
        </event-item>
      </list-item>
    </common-list>
  </common-form>
</template>

<script lang="ts">
  import {defineComponent} from "vue";
  import {EventData, UserService} from '@/services/UserService';
  import store from "@/store";
  import router from "@/router/router";
  import MyLabel from "@/components/UI/primitives/MyLabel.vue";
  import EventItem from "@/components/UI/list/results/eventItem.vue";
  import CommonList from "@/components/UI/list/CommonList.vue";
  import ListItem from "@/components/UI/list/ListItem.vue";
  import MyArea from "@/components/UI/composits/MyArea.vue";
  import MyButton from "@/components/UI/primitives/MyButton.vue";
  import LabelInput from "@/components/UI/composits/LabelInput.vue";
  import updateDataMixin from "@/components/mixins/updateDataMixin";

  export default defineComponent({
    name: "SignUpPage",
    mixins: [updateDataMixin],
    components: {
      LabelInput, MyButton, MyArea, MyLabel, EventItem, CommonList, ListItem},
    async created() {
      if (!store.state.auth.isAuth)
        await router.push("/");
    },
    data() {
      return {
        info: store.state.auth.data,
        channel_id: parseInt(this.$route.params.ch_id as string),
        in_name: "",
        time: "",
        in_description: ""
      }
    },
    computed: {
      us: () => new UserService(),
      userData: () => store.state.auth,
      channel: function() {
        return this.$data.info.channels.find(ch => ch.channel_id === this.$data.channel_id)
      },
      events: function() {
        console.log(this.$data.info.events)
        console.log(this.$data.info.events.filter(ev => ev.channel_id === this.$data.channel_id))
        return this.$data.info.events.filter(ev => ev.channel_id === this.$data.channel_id)
      }
    },
    methods: {
      async remove(event: EventData) {
        const res = await this.us.delete_event(event.event_id, event.channel_id)
        if (!res) {
          await this.update_data()
          this.info = store.state.auth.data
        }
      },
      async create_event() {
        const res = await this.us.event_create({
          deadline: this.time.replace('T', ' ') + ':00',
          description: this.in_description,
          name: this.in_name},
            this.channel_id
        )
        console.log(res);
        if (!res) {
          await this.update_data()
          this.info = store.state.auth.data
          this.in_name = ""
          this.in_description = ""
          this.time = ""
        }
      }
    }
  })
</script>

<style scoped>
.form {
  display: flex;
  flex-direction: column;
}

#create {
  width: 80%;
  max-width: 800px;
}

#add-box {
  display: flex;
}

.item-container {
  display: flex;
  flex-wrap: wrap;
}

.item {
  flex: 0 0 48%; /* Set the flex basis to one-third of the container width */
  box-sizing: border-box;
  margin: 1%; /* Add padding or adjust as needed */
}

  #separate {
    margin-top: 25px;
    padding-bottom: 20px;
    width: auto;
  }

  #submit_form > * {
    margin: 15px 0px;
  }
</style>