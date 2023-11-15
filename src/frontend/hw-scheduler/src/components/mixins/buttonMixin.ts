import { defineComponent } from 'vue';

export default defineComponent({
    props: {
        disabled: {
            type: Boolean,
            default: false,
        },
    },
});