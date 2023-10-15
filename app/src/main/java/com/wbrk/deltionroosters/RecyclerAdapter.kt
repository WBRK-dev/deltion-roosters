package com.wbrk.deltionroosters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter (private var times: List<String>, private var titles: List<String>, private var locations: List<String>) :
RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemTime: TextView
        val itemTitle: TextView
        val itemLocation: TextView

        init {
            itemTime = itemView.findViewById(R.id.time)
            itemTitle = itemView.findViewById(R.id.title)
            itemLocation = itemView.findViewById(R.id.location)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return times.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTime.text = times[position]
        holder.itemTitle.text = titles[position]
        holder.itemLocation.text = locations[position]
    }
}