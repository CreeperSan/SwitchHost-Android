package com.creepersan.switchhost.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.creepersan.switchhost.R
import com.creepersan.switchhost.application.SwitchHostApplication
import com.creepersan.switchhost.bean.HostFile
import com.creepersan.switchhost.bean.MainStartDrawerItem
import com.creepersan.switchhost.extension.gone
import com.creepersan.switchhost.extension.visible
import com.creepersan.switchhost.manager.ConfigManager
import com.creepersan.switchhost.manager.FileManager
import com.creepersan.switchhost.manager.PrefManager
import com.creepersan.switchhost.manager.RootManager
import com.creepersan.switchhost.widget.adapter.MainHostFileAdapter
import com.creepersan.switchhost.widget.adapter.MainStartDrawerAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.lang.Exception

class MainActivity : BaseActivity(), MainHostFileAdapter.OnHostFileClickListener {
    companion object{
        private const val REQUEST_CODE_IMPORT_HOST_FILE = 0

        private const val ID_DRAWER_EXIT = 0
        private const val ID_DRAWER_WELCOME = 1
    }

    private val mAdapter = MainHostFileAdapter(this)
    private val mDrawerAdapter = MainStartDrawerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initActionBar()
        initDrawerLayout()
        if (initHint()){
            initRecyclerView()
            initData()
            initFloatingActionButton()
        }else{
            supportActionBar?.hide()
        }
    }

    private fun initDrawerLayout(){
        mDrawerAdapter.addItems(MainStartDrawerItem(ID_DRAWER_WELCOME, R.drawable.ic_book, "版本说明"))
        mDrawerAdapter.addItems(MainStartDrawerItem(ID_DRAWER_EXIT, R.drawable.ic_exit, "退出"))
        mDrawerAdapter.setItemClickListener(object : MainStartDrawerAdapter.OnItemClickListener{
            override fun onItemClick(pos: Int, item: MainStartDrawerItem) {
                when(item.id){
                    ID_DRAWER_EXIT -> SwitchHostApplication.getInstance().exit()
                    ID_DRAWER_WELCOME -> toActivity(WelcomeActivity::class.java)
                }
            }
        })
        mainDrawerRecyclerView.layoutManager = LinearLayoutManager(this)
        mainDrawerRecyclerView.adapter = mDrawerAdapter
    }

    private fun initActionBar(){
        supportActionBar?.apply {
            this.setDefaultDisplayHomeAsUpEnabled(true)
            this.setDisplayHomeAsUpEnabled(true)
            this.setHomeAsUpIndicator(R.drawable.ic_menu_white)
        }
    }

    private fun initHint():Boolean{
        val stringBuilder = StringBuilder()
        if (!application.isInitSuccess(SwitchHostApplication.INIT_DIRECTORY)){
            stringBuilder.appendln("文件夹初始化失败")
        }
        if (!application.isInitSuccess(SwitchHostApplication.INIT_PERMISSION)){
            stringBuilder.appendln("应用权限初始化失败")
        }
        if (!application.isInitSuccess(SwitchHostApplication.INIT_ROOT)){
            stringBuilder.appendln("Root权限获取失败")
        }
        if (stringBuilder.isEmpty()){
            showContentLayout()
            return true
        }else{
            showHintLayout(R.drawable.ic_error_outline, stringBuilder.toString())
            return false
        }
    }

    private fun initData(){
        mAdapter.clearBackupHostFile()
        mAdapter.clearHostFile()
        mAdapter.addBackupHostFile(FileManager.getAllBackupHostFile())
        mAdapter.addHostFile(FileManager.getAllHostFile())
        mAdapter.refreshData()
        mAdapter.notifyDataSetChanged()
    }

    private fun initRecyclerView(){
        mAdapter.setOnHostFileClickListener(this)
        mainRecyclerView.layoutManager = mAdapter.getLayoutManager()
        mainRecyclerView.adapter = mAdapter
    }

    private fun initFloatingActionButton(){
        mainHintAddButton.setOnClickListener {
            toActivity(SelectHostFileActivity::class.java, requestCode = REQUEST_CODE_IMPORT_HOST_FILE )
        }
    }

    override fun onHostFileClick(hostFile: HostFile) {
        AlertDialog.Builder(this)
            .setTitle("切换Host")
            .setMessage("确认切换当前Host为 ${hostFile.name} 吗？${if (mAdapter.getBackupHostFileCount() <= 0){"你还没有备份当前Host文件！"}else{""}}")
            .setPositiveButton("确定"){ _, _ ->
                if (RootManager.replaceHostFile(hostFile)){
                    ConfigManager.setCurrentHostName(hostFile.name)
                    showSnack(mainCoordinatorLayout, "Host已切换至 ${hostFile.name}")
                    initData()
                }else{
                    showSnack(mainCoordinatorLayout, "Host切换失败")
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }

    override fun onHostFileLongClick(hostFile: HostFile): Boolean {
        val isCollection = mAdapter.isCollection(hostFile)
        AlertDialog.Builder(this)
            .setTitle(hostFile.name)
            .setItems(arrayOf(if (isCollection) "取消收藏" else "收藏", "删除")) { _, index ->
                when(index){
                    0 -> onCollectHostFileClick(hostFile, isCollection)
                    1 -> onDeleteHostFileClick(hostFile)
                }
            }
            .show()
        return true
    }

    private fun onDeleteHostFileClick(hostFile: HostFile){
        AlertDialog.Builder(this)
            .setTitle("删除Host")
            .setMessage("确认删除Host文件 ${hostFile.name} 吗？")
            .setPositiveButton("确定"){ _, _ ->
                if (FileManager.deleteHostFile(hostFile)){
                    showSnack(mainCoordinatorLayout, "已删除 ${hostFile.name}")
                }else{
                    showSnack(mainCoordinatorLayout, "删除 ${hostFile.name} 失败，请稍后重试")
                }
                initData()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun onCollectHostFileClick(hostFile: HostFile, isRemoveCollection:Boolean){
        val collectionHostFileList = mAdapter.getCollectionHostFileList()
        if (isRemoveCollection){
            // 取消收藏
            var hasChange = false
            val iterator = collectionHostFileList.iterator()
            while (iterator.hasNext()){
                val tmpHostFile = iterator.next()
                if (tmpHostFile == hostFile){
                    hasChange = true
                    iterator.remove()
                }
            }
            if (hasChange){
                PrefManager.setCollectionFileList(collectionHostFileList)
            }
        }else{
            // 添加收藏
            if(!collectionHostFileList.contains(hostFile)){
                collectionHostFileList.add(hostFile)
                PrefManager.setCollectionFileList(collectionHostFileList)
            }
        }
        mAdapter.refreshData()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuMainBackup -> {
                if (RootManager.backupSystemHostFile()){
                    showSnack(mainCoordinatorLayout, "当前Host备份成功")
                    initData()
                }else{
                    showSnack(mainCoordinatorLayout, "备份当前Host失败，请检查相关权限")
                }
            }
            android.R.id.home -> {
                toggleDrawer()
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_CODE_IMPORT_HOST_FILE -> {
                if (resultCode == Activity.RESULT_OK && data != null && data.hasExtra(SelectHostFileActivity.INTENT_KEY_SELECT_HOST_FILE_PATH)){
                    var count = 0
                    val pathSet = data.getStringArrayExtra(SelectHostFileActivity.INTENT_KEY_SELECT_HOST_FILE_PATH)
                    pathSet?.forEach {
                        val file = File(it)
                        val targetFile = File("${FileManager.getHostDirectory().path}/${file.name}")
                        if (file.exists() && file.isFile && !targetFile.exists()){
                            try {
                                file.copyTo(targetFile)
                                count += 1
                            }catch (e:Exception){}
                        }
                    }
                    initData()
                    showSnack(mainCoordinatorLayout, "已导入${count}个Host文件")
                }
            }
        }
    }




    private fun toggleDrawer(){
        if (mainDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mainDrawerLayout.closeDrawer(GravityCompat.START)
        }else{
            mainDrawerLayout.openDrawer(GravityCompat.START)
        }

    }

    private fun showHintLayout(iconResID:Int, hintText:String){
        mainHintLayout.visible()
        mainCoordinatorLayout.gone()
        mainRecyclerView.gone()

        mainHintImageView.setImageResource(iconResID)
        mainHintTextView.setText(hintText)
    }

    private fun showContentLayout(){
        mainCoordinatorLayout.visible()
        mainRecyclerView.visible()
        mainHintLayout.gone()
    }

}
