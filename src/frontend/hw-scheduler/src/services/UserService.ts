import {AxiosError} from "axios";
import {ErrorResponse, SuccessResponse} from "@/services/ErrorResponse";
import {Api} from "@/services/ApiService";

export interface TokenUserData {
  token: string
}

export interface LoginForm {
  login: string,
  password: string
}

export interface SubmitSolutionForm {
  work_time: number | null
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
      const api = new Api();
      return await api.post<TokenUserData, LoginForm>('/user', form);
    } catch (e) {
      const error = e as AxiosError;
      if (error.response != null)
        return error.response.data as ErrorResponse;
      throw e;
    }
  }

  public async userInfo(): Promise<ErrorResponse | UserInfo> {
    try {
      const api = new Api();
      return await api.get<UserInfo>('/user/info');
    } catch (e) {
      const error = e as AxiosError;
      if (error.response != null)
        return error.response.data as ErrorResponse;
      throw e;
    }
  }

  public async event_completed(event: EventData, time: number): Promise<ErrorResponse | SuccessResponse> {
    try {
      const api = new Api();
      return await api.post<SuccessResponse, SubmitSolutionForm>('/user/event/' + event.event_id, {work_time: time});
    } catch (e) {
      const error = e as AxiosError;
      if (error.response != null)
        return error.response.data as ErrorResponse;
      throw e;
    }
  }

  public async channel_unsubscribe(channel: ChannelData): Promise<ErrorResponse | null> {
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

  public async channel_subscribe(name: string): Promise<ErrorResponse | null> {
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

  public async channel_delete(channel_id: number): Promise<ErrorResponse | null> {
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