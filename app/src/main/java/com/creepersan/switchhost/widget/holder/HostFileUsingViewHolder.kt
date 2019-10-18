package com.creepersan.switchhost.widget.holder

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.creepersan.switchhost.R

class HostFileUsingViewHolder(parent:ViewGroup) : BaseViewHolder(R.layout.item_host_file_using, parent) {
    private val iconImageView = itemView.findViewById<ImageView>(R.id.itemHostFileUsingIcon)
    private val titleTextView = itemView.findViewById<TextView>(R.id.itemHostFileUsingTitle)

    fun setName(title:String){
        titleTextView.text = title
    }

}