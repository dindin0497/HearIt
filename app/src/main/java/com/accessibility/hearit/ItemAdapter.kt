package com.accessibility.hearit

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(private val items: MutableList<String>) :
    RecyclerView.Adapter<ItemAdapter.LogViewHolder>() {

        val TAG = "ItemAdapter"

    class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv: TextView = itemView.findViewById(R.id.tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return LogViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        holder.tv.text = items[position]
    }

    override fun getItemCount(): Int = items.size

    fun addItem(txt: String) {
        Log.i(TAG, "addItem $txt")
        items.add(txt)
        notifyItemInserted(items.size - 1)
    }

    fun updateItem(txt: String) {
        Log.i(TAG, "updateItem $txt")
        items.set(items.size-1,  txt)
        notifyItemChanged(items.size - 1)
    }

    fun clear(){
        items.clear()
        notifyDataSetChanged()
    }
}
