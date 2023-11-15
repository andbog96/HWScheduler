import axios, { AxiosInstance, AxiosRequestConfig } from 'axios';
import store from '@/store';

export class Api {
    private axiosInstance: AxiosInstance;

    constructor() {
        const url = process.env.VUE_APP_BASE_URL;
        const config: AxiosRequestConfig = {
            url,
            headers: {
                'Authorization': `Bearer ${store.state.auth.token}`
            }
        };
        this.axiosInstance = axios.create(config);
    }

    public async get<Result>(url: string, config?: AxiosRequestConfig) {
        const response = await this.axiosInstance.get<Result>(url, config);
        return response.data;
    }

    public async post<Result, RequestBody>(url: string, data?: RequestBody, config?: AxiosRequestConfig) {
        const response = await this.axiosInstance.post<Result>(url, data, config);
        return response.data;
    }

    public async patch<Result, RequestBody>(url: string, data?: RequestBody, config?: AxiosRequestConfig) {
        const response = await this.axiosInstance.patch<Result>(url, data, config);
        return response.data;
    }

    public async delete(url: string, config?: AxiosRequestConfig) {
        return this.axiosInstance.delete(url, config);
    }
}