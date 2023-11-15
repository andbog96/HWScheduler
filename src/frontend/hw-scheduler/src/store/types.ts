import {UserInfo} from "@/services/UserService";
export interface AuthState {
    isAuth: boolean,
    token: string,
    login: string | null,
    data: UserInfo,
}

export interface StoreState {
    auth: AuthState
}