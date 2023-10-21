package com.wbrk.deltionroosters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView

class KlasRecyclerAdapter (private var klasLabel: List<String>, private var klasSelected: List<Boolean>) :
RecyclerView.Adapter<KlasRecyclerAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val Checkbox: CheckBox

        init {
            Checkbox = itemView.findViewById(R.id.checkBox)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.group_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return klasLabel.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.Checkbox.text = klasLabel[position]
        holder.Checkbox.isChecked = klasSelected[position]
    }
}