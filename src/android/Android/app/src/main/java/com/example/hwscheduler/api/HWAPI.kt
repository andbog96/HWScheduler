package com.example.hwscheduler.api

import com.example.hwscheduler.UserInfo
import com.example.hwscheduler.group.Group
import com.squareup.moshi.Json
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface HWAPI {

    @POST("user")
    fun registerUser(
        @Body login: LoginPasswordReq
    ): Single<Token>

    @GET("user/info")
    fun userInfo(
        @Header(value = "token") token: String
    ): Single<UserInfo>

    @POST("channel")
    fun createChannel(
        @Header(value = "token") token: String,
        @Body channelReq: CreateChannelReq
    ): Single<Group>

    @POST("user/channel")
    fun subscribe(
        @Header(value = "token") token: String,
        @Body channelReq: SubscribeChannelReq
    ): Completable

    @DELETE("user/channel/{ch_id}")
    fun unsubscribe(
        @Header(value = "token") token: String,
        @Path("ch_id") chId: Long
    ): Completable

    @POST("channel/{ch_id}/event")
    fun createEvent(
        @Header(value = "token") token: String,
        @Path("ch_id") chId: Long,
        @Body eventReq: CreateEventReq
    ): Completable

    @PUT("event/{ev_id}")
    fun updateEvent(
        @Header(value = "token") token: String,
        @Path("ev_id") evId: Long,
        @Body eventReq: CreateEventReq
    ): Completable

    @DELETE("channel/{ch_id}/event/{ev_id}")
    fun deleteEvent(
        @Header(value = "token") token: String,
        @Path("ch_id") chId: Long,
        @Path("ev_id") evId: Long
    ): Completable

    @POST("user/event/{ev_id}")
    fun completeEvent(
        @Header(value = "token") token: String,
        @Path("ev_id") evId: Long,
        @Body completeEventReq: CompleteEventReq
    ): Completable

}

data class LoginPasswordReq(
    @field:Json(name = "login") val login: String,
    @field:Json(name = "password") val password: String
)

data class Token(
    @field:Json(name = "token") val token: String
)

data class CreateChannelReq(
    @field:Json(name = "name") val name: String
)

data class SubscribeChannelReq(
    @field:Json(name = "shortname") val name: String,
)

data class CreateEventReq(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "description") val description: String,
    @field:Json(name = "deadline") val deadline: String,
)

data class CompleteEventReq(
    @field:Json(name = "work_time") val workTime: Long
)