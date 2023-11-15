import {defineComponent} from "vue";
import {mapActions} from "vuex";
import {ChannelData, EventData, UserInfo, UserService} from "@/services/UserService";
import store from "@/store";

export default defineComponent({
    methods: {
        ...mapActions({
            saveData: 'auth/saveData'
        }),
        async update_data() {
          const us = new UserService();
          const res = await us.userInfo();
          if ('events' in res)
              await this.saveData({data: res})
        },
        async remove_event(event: EventData) {
            const data = store.state.auth.data;
            await this.saveData({data: {channels: data.channels, events: data.events.filter(ev => ev != event)}})
        },
        async remove_channel(channel: ChannelData) {
            const data = store.state.auth.data;
            await this.saveData({data: {
                channels: data.channels.filter(ch => ch != channel),
                events: data.events.filter(ev => ev.channel_id != channel.channel_id),
            }})
        }
    }
});