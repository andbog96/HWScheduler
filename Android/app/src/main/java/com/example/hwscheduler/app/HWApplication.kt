package com.example.hwscheduler.app

import android.app.Application
import com.example.hwscheduler.api.HWAPI
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class HWApplication : Application() {

    lateinit var appRetrofit: Retrofit
    lateinit var hwApi: HWAPI

    companion object {
        private const val API = "http://10.0.2.2:5000"
        lateinit var instance: HWApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        appRetrofit = Retrofit.Builder()
            .baseUrl(API)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        hwApi = appRetrofit.create(HWAPI::class.java)
    }

}