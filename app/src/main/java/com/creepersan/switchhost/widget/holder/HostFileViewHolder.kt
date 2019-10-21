package com.creepersan.switchhost.widget.holder

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.creepersan.switchhost.R

class HostFileViewHolder(parent:ViewGroup) : BaseViewHolder(R.layout.item_host_file, parent){
    private val iconImageView = itemView.findViewById<ImageView>(R.id.itemHostFileIcon)
    private val titleTextView = itemView.findViewById<TextView>(R.id.itemHostFileTitle)

    fun setName(name:String){
        titleTextView.text = name
    }

    fun setOnClickListener(listener: View.OnClickListener){
        itemView.setOnClickListener(listener)
    }

    fun setOnLongClickListener(listener: View.OnLongClickListener){
        itemView.setOnLongClickListener(listener)
    }


}