import {defineComponent} from "vue";

export default defineComponent({
    data() {
        return {
            errorMsg: ''
        }
    },
    methods: {
        hasError() {
            return this.errorMsg.length > 0;
        }
    }
})