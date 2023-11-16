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

export interface SubscribeForm {
  shortname: string
}

export interface ChannelCreateForm {
  name: string
}

export interface EventCreateForm {
  name: string,
  description: string,
  deadline: string
}

export interface ChannelResponse {
  channel_id: number,
  name: string
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

  public async channel_subscribe(name: string): Promise<ErrorResponse | SuccessResponse> {
    try {
      const api = new Api();
      return await api.post<SuccessResponse, SubscribeForm>('/user/channel', {shortname: name});
    } catch (e) {
      const error = e as AxiosError;
      if (error.response != null)
        return error.response.data as ErrorResponse;
      throw e;
    }
  }

  public async channel_delete(channel_id: number): Promise<ErrorResponse | null> {
    try {
      const api = new Api();
      await api.delete('/user/channel/' + channel_id);
      return null;
    } catch (e) {
      const error = e as AxiosError;
      if (error.response != null)
        return error.response.data as ErrorResponse;
      throw e;
    }
  }

  public async channel_create(channel_name: string): Promise<ErrorResponse | ChannelData> {
    try {
      const api = new Api();
      const res = await api.post<ChannelResponse, ChannelCreateForm>('/channel', {name: channel_name});
      return {name: res.name, channel_id: res.channel_id, is_admin: true}
    } catch (e) {
      const error = e as AxiosError;
      if (error.response != null)
        return error.response.data as ErrorResponse;
      throw e;
    }
  }

  public async delete_event(event_id: number, channel_id: number): Promise<ErrorResponse | null> {
    try {
      const api = new Api();
      await api.delete('/channel/' + channel_id + '/event/' + event_id);
      return null;
    } catch (e) {
      const error = e as AxiosError;
      if (error.response != null)
        return error.response.data as ErrorResponse;
      throw e;
    }
  }

  public async event_create(form: EventCreateForm, channel: number): Promise<ErrorResponse | null> {
    try {
      const api = new Api();
      await api.post<undefined, EventCreateForm>('/channel/' + channel + '/event', form);
      return null;
    } catch (e) {
      const error = e as AxiosError;
      if (error.response != null)
        return error.response.data as ErrorResponse;
      throw e;
    }
  }
}