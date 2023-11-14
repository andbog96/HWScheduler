package com.example.hwscheduler.group

import com.example.hwscheduler.event.Event

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hwscheduler.R

class GroupEventAdapter : RecyclerView.Adapter<GroupEventViewHolder>() {

    private var data: List<Event> = listOf()

    fun setData(data: List<Event>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupEventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GroupEventViewHolder(inflater.inflate(R.layout.item_group_event, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: GroupEventViewHolder, position: Int) {
        val event = data[position]
        holder.bind(event)
    }

}

class GroupEventViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val eventName: TextView = root.findViewById(R.id.group_event_name)

    @SuppressLint("SetTextI18n")
    fun bind(event: Event) {
        eventName.text = event.name
    }
}