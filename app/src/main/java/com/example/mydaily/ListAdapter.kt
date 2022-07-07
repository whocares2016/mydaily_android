package com.example.mydaily

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ListAdapter(val activity: MainActivity, val dailyList: kotlin.collections.List<List>): RecyclerView.Adapter<ListAdapter.ViewHolder>(){
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val dailyTitle: TextView = view.findViewById(R.id.dailyTitle)
        val dailyTime: TextView = view.findViewById(R.id.dailyTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.daily_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val daily = dailyList[position]
//            Toast.makeText(parent.context, "you click view ${daily.title}",
//                Toast.LENGTH_SHORT).show()

            val id = daily.id
            val title = daily.title
            val time = daily.time
            val intent = Intent(activity, ListDetail::class.java)
            intent.putExtra("id", id)
            activity.startActivity(intent);
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val daily = dailyList[position]
        holder.dailyTitle.text = daily.title
        holder.dailyTime.text = daily.time
    }

    override fun getItemCount(): Int = dailyList.size
}