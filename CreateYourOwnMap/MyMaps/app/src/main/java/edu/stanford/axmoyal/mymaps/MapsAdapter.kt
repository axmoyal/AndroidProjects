package edu.stanford.axmoyal.mymaps

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import android.util.Log
import android.view.View
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import edu.stanford.axmoyal.mymaps.Models.UserMap

private const val TAG="MapsAdapter"

class MapsAdapter(val context: Context, val userMaps: List<UserMap>, val onClickListener: OnClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnClickListener{

        fun onItemClick(position: Int)
        fun onItemLongClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v= LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount()=userMaps.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val userMap=userMaps[position]
        val textViewTitle=holder.itemView.findViewById<TextView>(android.R.id.text1)
        textViewTitle.text=userMap.title

        holder.itemView.setOnClickListener{
            onClickListener.onItemClick(position)
        }
        holder.itemView.setOnLongClickListener{
            onClickListener.onItemLongClick(position)
            return@setOnLongClickListener true
        }

    }

}