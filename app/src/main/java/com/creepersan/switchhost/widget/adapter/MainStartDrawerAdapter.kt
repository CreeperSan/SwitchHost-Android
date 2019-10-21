package com.creepersan.switchhost.widget.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.creepersan.switchhost.bean.MainStartDrawerItem
import com.creepersan.switchhost.widget.holder.MainStartDrawerViewHolder
import java.util.ArrayList

class MainStartDrawerAdapter : RecyclerView.Adapter<MainStartDrawerViewHolder>() {
    private val mItemList = ArrayList<MainStartDrawerItem>()
    private var mListener : OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainStartDrawerViewHolder {
        return MainStartDrawerViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return mItemList.size
    }

    fun clearItems(){
        mItemList.clear()
    }

    fun addItems(vararg item:MainStartDrawerItem){
        mItemList.addAll(item)
        notifyDataSetChanged()
    }

    fun setItemClickListener(listener:OnItemClickListener?){
        mListener = listener
    }

    override fun onBindViewHolder(holder: MainStartDrawerViewHolder, position: Int) {
        val item = mItemList[position]
        holder.setItem(item.icon, item.name)
        holder.setOnClickListener(View.OnClickListener {
            mListener?.onItemClick(position, item)
        })
    }

    interface OnItemClickListener{
        fun onItemClick(pos:Int, item:MainStartDrawerItem)
    }

}