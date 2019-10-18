package com.creepersan.switchhost.widget.holder

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder(@LayoutRes layout:Int, parent:ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))