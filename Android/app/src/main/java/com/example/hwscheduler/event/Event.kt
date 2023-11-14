package com.example.hwscheduler.event

data class Event(
    val eventId: Int,
    val channelId: Int,
    val name: String,
    val description: String,
    val deadline: Int,
    val estimated: Int
)