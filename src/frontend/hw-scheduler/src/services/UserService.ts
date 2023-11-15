import {AxiosError} from "axios";
import {ErrorResponse} from "@/services/ErrorResponse";
import {Api} from "@/services/ApiService";

export interface TokenUserData {
  token: string
}

export interface LoginForm {
  login: string,
  password: string
}

export interface EventData {
  event_id: number,
  channel_id: number,
  name: string,
  description: string,
  deadline: Date,
  estimated: number | null
}

export interface ChannelData {
  channel_id: number,
  name: string,
  is_admin: boolean
}

export interface UserInfo {
  channels: ChannelData[],
  events: EventData[]
}

export class UserService {
  public async signInUp(form: LoginForm): Promise<ErrorResponse | TokenUserData> {
    try {
      return {token: 'Abba'};
      // const api = new Api();
      // return await api.post<TokenUserData, LoginForm>('/users', form);
    } catch (e) {
      const error = e as AxiosError;
      if (error.response != null)
        return error.response.data as ErrorResponse;
      throw e;
    }
  }

  public async userInfo(): Promise<ErrorResponse | UserInfo> {
    try {
      return {
        channels: [
          {channel_id: 0, name: 'db_itmo', is_admin: false},
          {channel_id: 1, name: 'fp_itmo', is_admin: true},
          {channel_id: 2, name: 'ml-itmo', is_admin: true},
          {channel_id: 3, name: 'cpp-itmo', is_admin: false}
        ],
        events: [
          {event_id: 0, channel_id: 0, name: "LR1", description: "Do or Die", deadline: new Date(), estimated: null},
          {event_id: 1, channel_id: 1, name: "LR1", description: "Church", deadline: new Date(), estimated: null},
          {event_id: 2, channel_id: 1, name: "LR2", description: "Foldl", deadline: new Date(), estimated: null},
          {event_id: 3, channel_id: 2, name: "LR1", description: "K-nearest", deadline: new Date(), estimated: 150},
          {event_id: 4, channel_id: 2, name: "LR2", description: "Perceptron", deadline: new Date(), estimated: 100},
          {event_id: 5, channel_id: 3, name: "LR1", description: "cls-07", deadline: new Date(), estimated: null},
          {event_id: 6, channel_id: 3, name: "LR2", description: "bignum", deadline: new Date(), estimated: 250}
        ]
      }
    } catch (e) {
      const error = e as AxiosError;
      if (error.response != null)
        return error.response.data as ErrorResponse;
      throw e;
    }
  }

  public async completed(event: EventData, time: number | null): Promise<ErrorResponse | null> {
    try {
      // const api = new Api();
      // return await api.post<TokenUserData, LoginForm>('/users', form);
      return null;
    } catch (e) {
      const error = e as AxiosError;
      if (error.response != null)
        return error.response.data as ErrorResponse;
      throw e;
    }
  }
}