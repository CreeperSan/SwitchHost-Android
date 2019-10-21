package com.creepersan.switchhost.widget.holder

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.creepersan.switchhost.R

class MainStartDrawerViewHolder(parent:ViewGroup) : BaseViewHolder(R.layout.item_main_start_drawer_item, parent){
    private val iconImageView = itemView.findViewById<ImageView>(R.id.itemMainStartDrawerIcon)
    private val nameTextView = itemView.findViewById<TextView>(R.id.itemMainStartDrawerName)

    fun setOnClickListener(listener:View.OnClickListener){
        itemView.setOnClickListener(listener)
    }

    fun setItem(@DrawableRes icon:Int, name:String){
        iconImageView.setImageResource(icon)
        nameTextView.text = name
    }
}