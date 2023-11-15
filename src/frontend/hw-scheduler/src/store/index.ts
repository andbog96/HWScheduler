import {createStore} from "vuex";
import {authModule} from "@/store/authModule";
import {StoreState} from "@/store/types";

const store = createStore<StoreState>({
    modules: {
        auth: authModule
    }
});

export default store