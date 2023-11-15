import {defineComponent} from "vue";
import {mapActions} from "vuex";
import {EventData} from "@/services/UserService";
import store from "@/store";

export default defineComponent({
    methods: {
        ...mapActions({
            saveData: 'auth/saveData'
        }),
        async remove_event(event: EventData) {
            const data = store.state.auth.data;
            await this.saveData({data: {events: data.events.filter(ev => ev != event), channels: []}})
        }
    }
});