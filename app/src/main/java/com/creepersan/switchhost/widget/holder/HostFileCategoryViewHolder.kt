package com.creepersan.switchhost.widget.holder

import android.view.ViewGroup
import android.widget.TextView
import com.creepersan.switchhost.R

class HostFileCategoryViewHolder(parent:ViewGroup):BaseViewHolder(R.layout.item_host_file_category, parent) {
    val titleTextView = itemView.findViewById<TextView>(R.id.itemHostFileCategoryTitle)

    fun setTitle(title:String){
        titleTextView.text = title
    }

}