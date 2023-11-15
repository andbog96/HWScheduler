import {defineComponent} from 'vue';

export default defineComponent({
    props: {
        modelValue: {
            type: [String, Number, Boolean],
            default: ''
        },
        type: {
            type: String,
            default: 'text'
        },
        placeholder: {
            type: String,
            default: 'Input ...'
        },
    },
    emits: ['update:modelValue'],
    methods: {
        updateInput(event: Event) {
            const inputElement = event.target as HTMLInputElement;
            this.$emit('update:modelValue', inputElement.value);
        }
    },
});