package com.creepersan.switchhost.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creepersan.switchhost.R
import com.creepersan.switchhost.widget.holder.BaseViewHolder
import kotlinx.android.synthetic.main.activity_select_host_file.*
import java.io.File
import java.util.*

class SelectHostFileActivity : BaseActivity(){
    private val mAdapter = ItemAdapter()
    private val mExternalStorageDirectory = Environment.getExternalStorageDirectory()
    private var mCurrentDirectory = mExternalStorageDirectory
    private var mSelectFileSet = HashSet<String>()

    companion object{
        const val INTENT_KEY_SELECT_HOST_FILE_PATH = "path_array"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_host_file)
        initActionBar()
        initListener()
        initListView()
        initData()
    }

    private fun initActionBar(){
        refreshTitle()
        supportActionBar?.apply {
            this.setDisplayHomeAsUpEnabled(true)
            this.setDefaultDisplayHomeAsUpEnabled(true)
        }
    }

    private fun initListener(){
        mAdapter.setOnItemClickListener { fileItem ->
            if (fileItem.isDirectory){
                if (mAdapter.setDirectory(fileItem.path)){
                    mCurrentDirectory = File(fileItem.path)
                    mSelectFileSet.clear()
                }else{
                    toast("无法打开此文件夹")
                }
                refreshTitle()
            }else{
                if (mSelectFileSet.contains(fileItem.path)){
                    mSelectFileSet.remove(fileItem.path)
                }else{
                    mSelectFileSet.add(fileItem.path)
                }
                refreshTitle()
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun initListView(){
        selectHostFileActivityRecyclerView.layoutManager = LinearLayoutManager(this)
        selectHostFileActivityRecyclerView.adapter = mAdapter
    }

    private fun initData(){
        mAdapter.setDirectory(mCurrentDirectory.path)
        refreshTitle()
    }

    private fun refreshTitle(){
        setTitle("已选择${mSelectFileSet.size}个文件")
    }

    override fun onBackPressed() {
        if (mExternalStorageDirectory.path == mCurrentDirectory.path){
            super.onBackPressed()
        }else{
            val parentPath = mCurrentDirectory?.parent
            if (parentPath == null){
                super.onBackPressed()
            }else{
                if (mAdapter.setDirectory(parentPath)){
                    mCurrentDirectory = File(parentPath)
                    mSelectFileSet.clear()
                }else{
                    super.onBackPressed()
                }
                refreshTitle()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_select_host_file, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.menuSelectHostFileImport -> {
                val dataIntent = Intent()
                dataIntent.putExtra(INTENT_KEY_SELECT_HOST_FILE_PATH, mSelectFileSet.toTypedArray())
                setResult(Activity.RESULT_OK, dataIntent)
                finish()
            }
        }
        return true
    }





    private data class FileItem(val path:String, val name:String, val isDirectory: Boolean)
    private inner class ItemAdapter : RecyclerView.Adapter<ItemViewHolder>(){
        private val mFileList = ArrayList<FileItem>()
        private var mClickListener : ((fileItem:FileItem)->Unit)? = null

        fun setDirectory(path: String):Boolean{
            val file = File(path)
            if (!file.exists() || file.isFile){
                return false
            }
            mFileList.clear()
            file.listFiles()?.forEach { tmpFile ->
                mFileList.add(FileItem(tmpFile.path, tmpFile.name, tmpFile.isDirectory))
            }
            mFileList.sortBy { it.name.toLowerCase(Locale.getDefault()) }
            notifyDataSetChanged()
            return true
        }

        fun setOnItemClickListener(onClick:((fileItem:FileItem)->Unit)?){
            mClickListener = onClick
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder = ItemViewHolder(parent)

        override fun getItemCount(): Int = mFileList.size

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val item = mFileList[position]
            holder.setItem(item.isDirectory, item.name, mSelectFileSet.contains(item.path))
            holder.setOnClickListener(View.OnClickListener {
                mClickListener?.invoke(item)
            })
        }

    }
    private class ItemViewHolder(parent:ViewGroup) : BaseViewHolder(R.layout.item_file_item, parent){
        private val iconImageView = itemView.findViewById<ImageView>(R.id.itemFileItemIcon)
        private val nameTextView = itemView.findViewById<TextView>(R.id.itemFileItemName)

        companion object{
            private val COLOR : Int = Color.parseColor("#FFCCCCCC")
        }

        fun setItem(isDirectory:Boolean, name:String, isSelect:Boolean=false){
            iconImageView.setImageResource(if (isDirectory)R.drawable.ic_folder else R.drawable.ic_file)
            nameTextView.text = name
            itemView.setBackgroundColor(if (isSelect) COLOR else Color.TRANSPARENT )
        }

        fun setOnClickListener(listener: View.OnClickListener){
            itemView.setOnClickListener(listener)
        }
    }

}