package com.example.hwscheduler.group

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.hwscheduler.ErrorUtils
import com.example.hwscheduler.R
import com.example.hwscheduler.app.HWApplication
import com.example.hwscheduler.userToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GroupAdapter(
    private val activity: AppCompatActivity,
    private val updateUI: () -> Unit
) : RecyclerView.Adapter<GroupViewHolder>() {

    private var data: List<Group> = listOf()

    fun setData(data: List<Group>) {
        this.data = data
        notifyDataSetChanged()
    }

    @SuppressLint("CheckResult")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GroupViewHolder(inflater.inflate(R.layout.item_group, parent, false)).apply {
            itemView.findViewById<ConstraintLayout>(R.id.item_group_layout).setOnClickListener {
                val group = data[this.adapterPosition]
                if (group.isAdmin) {
                    val intent = Intent(activity, GroupEventsActivity::class.java)
                    intent.putExtra(GroupEventsActivity.GROUP_ID, group.channelId)
                    activity.startActivity(intent)
                }
            }
            itemView.findViewById<ImageView>(R.id.unsubscribe_img).setOnClickListener {
                val group = data[this.adapterPosition]
                if (!group.isAdmin) {
                    HWApplication.instance.hwApi.unsubscribe(userToken!!, group.channelId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            updateUI()
                            Log.i("Unsubscribed: ", group.channelId.toString())
                        }, {
                            ErrorUtils.showMessage(it, activity)
                        })
                }
            }
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = data[position]
        holder.bind(group)
    }
}

class GroupViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val groupName: TextView = root.findViewById(R.id.group_name)
    val unsubscribeImg: ImageView = root.findViewById(R.id.unsubscribe_img)

    fun bind(group: Group) {
        var text = group.name
        if (group.isAdmin) text += " (Admin)"
        groupName.text = text
        if (group.isAdmin) unsubscribeImg.visibility = View.INVISIBLE
    }

}