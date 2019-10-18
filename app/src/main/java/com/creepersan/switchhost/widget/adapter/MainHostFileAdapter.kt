package com.creepersan.switchhost.widget.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creepersan.switchhost.bean.HostFile
import com.creepersan.switchhost.bean.HostFileCategory
import com.creepersan.switchhost.bean.HostFileUsing
import com.creepersan.switchhost.widget.holder.BaseViewHolder
import com.creepersan.switchhost.widget.holder.HostFileCategoryViewHolder
import com.creepersan.switchhost.widget.holder.HostFileUsingViewHolder
import com.creepersan.switchhost.widget.holder.HostFileViewHolder
import com.creepersan.switchhost.widget.layout_manager.MainHostListLayoutManager
import java.lang.RuntimeException
import java.util.ArrayList

class MainHostFileAdapter(context:Context) : RecyclerView.Adapter<BaseViewHolder>(){
    companion object{
        private const val VIEW_TYPE_CATEGORY = 0
        private const val VIEW_TYPE_FILE = 1
        private const val VIEW_TYPE_USING = 2
    }

    private var mCurrentHostFile : HostFile? = null
    private var mItemList = ArrayList<Any>()
    private var mBackupHostFileList = ArrayList<HostFile>()
    private var mHostFileList = ArrayList<HostFile>()
    private var mOnClickListener : OnHostFileClickListener? = null
    private var mGridLayoutManager = MainHostListLayoutManager(context){ pos, widthCount ->
        when (mItemList[pos]) {
            is HostFile -> 1
            is HostFileCategory -> widthCount
            is HostFileUsing -> widthCount
            else -> throw TypeNotFoundException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = mItemList[position]
        return when{
            item is HostFile -> VIEW_TYPE_FILE
            item is HostFileCategory -> VIEW_TYPE_CATEGORY
            item is HostFileUsing -> VIEW_TYPE_USING
            else -> throw TypeNotFoundException()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when(viewType){
            VIEW_TYPE_CATEGORY -> HostFileCategoryViewHolder(parent)
            VIEW_TYPE_USING -> HostFileUsingViewHolder(parent)
            VIEW_TYPE_FILE -> HostFileViewHolder(parent)
            else -> throw RuntimeException("不明的类型")
        }
    }

    override fun getItemCount(): Int {
        return mItemList.size
    }

    override fun onBindViewHolder(holderTmp: BaseViewHolder, position: Int) {
        val item = mItemList[position]
        when{
            item is HostFileCategory -> {
                val holder = holderTmp as HostFileCategoryViewHolder
                holder.setTitle(item.name)
            }
            item is HostFileUsing -> {
                val holder = holderTmp as HostFileUsingViewHolder
                holder.setName(item.hostFile?.name ?: "未知Host文件")
            }
            item is HostFile -> {
                val holder = holderTmp as HostFileViewHolder
                holder.setName(item.name)
                holder.setOnClickListener(View.OnClickListener {
                    mOnClickListener?.onHostFileClick(item)
                })
            }
            else ->{
                Log.e("TAG", "POSITION ERROR -> POSITION ${position}  HOSTLIST_SIZE -> ${mHostFileList.size}  BACKUP_HOSTFILE_SIZE -> ${mBackupHostFileList.size}")
            }
        }
    }

    fun setOnHostFileClickListener(listener:OnHostFileClickListener){
        this.mOnClickListener = listener
    }

    fun getLayoutManager():GridLayoutManager{
        return mGridLayoutManager
    }

    fun addBackupHostFile(file:Collection<HostFile>){
        mBackupHostFileList.addAll(file)
    }

    fun addHostFile(file: Collection<HostFile>){
        mHostFileList.addAll(file)
    }

    fun clearBackupHostFile(){
        mBackupHostFileList.clear()
    }

    fun clearHostFile(){
        mHostFileList.clear()
    }

    fun refreshData(){
        mItemList.clear()

        mItemList.add(HostFileCategory("正在使用"))
        mItemList.add(HostFileUsing(mCurrentHostFile))
        mItemList.add(HostFileCategory("Host"))
        mItemList.addAll(mHostFileList)
        mItemList.add(HostFileCategory("备份"))
        mItemList.addAll(mBackupHostFileList)
    }

    interface OnHostFileClickListener{
        fun onHostFileClick(hostFile:HostFile)
    }

    class TypeNotFoundException : Exception("找不到对应的类型")
}