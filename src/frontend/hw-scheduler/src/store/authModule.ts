import {Commit, Module} from "vuex";
import {AuthState, StoreState} from "@/store/types";
import {UserInfo} from "@/services/UserService";

export const authModule: Module<AuthState, StoreState> = {
    state: (): AuthState => ({
        isAuth: false,
        token: '',
        login: null,
        data: {events: [], channels: []}
    }),
    mutations: {
        SET_IS_AUTH(state: AuthState, isAuth: boolean) {
            state.isAuth = isAuth;
        },
        SET_TOKEN(state: AuthState, token: string) {
            state.token = token;
        },
        SET_LOGIN(state: AuthState, login: string) {
            state.login = login;
        },
        SET_DATA(state: AuthState, data: UserInfo) {
            state.data = data
        }
    },
    actions: {
        loggedIn({commit}: {commit: Commit}, {token, login}: {token: string, login: string}) {
            commit('SET_IS_AUTH', true);
            commit('SET_TOKEN', token);
            commit('SET_LOGIN', login);
        },
        loggedOut({commit}: {commit: Commit}) {
            commit('SET_IS_AUTH', false);
            commit('SET_TOKEN', '');
            commit('SET_LOGIN', null);
            commit('SET_DATA', {events: [], channels: []})
        },
        saveData({commit}: {commit: Commit}, {data}: {data: UserInfo}) {
            commit('SET_DATA', data)
        }
    },
    getters: {
        isAuth: (state: AuthState) => state.isAuth,
        token: (state: AuthState) => state.token,
        login: (state: AuthState) => state.login,
        data: (state: AuthState) => state.data
    },
    namespaced: true
}