package com.example.hwscheduler.event

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hwscheduler.R

class EventAdapter : RecyclerView.Adapter<EventViewHolder>() {

    private var data: List<Event> = listOf()
    private var channelIdToName: Map<Int, String> = mapOf()

    fun setData(data: List<Event>, channelIdToName: Map<Int, String>) {
        this.data = data
        this.channelIdToName = channelIdToName
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return EventViewHolder(inflater.inflate(R.layout.item_event, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = data[position]
        holder.bind(event, channelIdToName[event.channelId]!!)
    }

}

class EventViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val eventName: TextView = root.findViewById(R.id.event_name)
    val completeImg: ImageView = root.findViewById(R.id.complete_img)

    @SuppressLint("SetTextI18n")
    fun bind(event: Event, channelName: String) {
        eventName.text = "$channelName/${event.name}"
    }
}