package com.creepersan.switchhost.activity

import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.creepersan.switchhost.R
import com.creepersan.switchhost.application.SwitchHostApplication
import com.creepersan.switchhost.bean.HostFile
import com.creepersan.switchhost.extension.gone
import com.creepersan.switchhost.extension.visible
import com.creepersan.switchhost.manager.FileManager
import com.creepersan.switchhost.manager.RootManager
import com.creepersan.switchhost.widget.adapter.MainHostFileAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MainHostFileAdapter.OnHostFileClickListener {

    private val mAdapter = MainHostFileAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initActionBar()
        initDrawerLayout()
        if (initHint()){
            initData()
            initRecyclerView()
            initFloatingActionButton()
        }
    }

    private fun initDrawerLayout(){

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
    }

    private fun initRecyclerView(){
        mAdapter.setOnHostFileClickListener(this)
        mainRecyclerView.layoutManager = mAdapter.getLayoutManager()
        mainRecyclerView.adapter = mAdapter
    }

    private fun initFloatingActionButton(){
        mainHintAddButton.setOnClickListener {
            Snackbar.make(mainCoordinatorLayout, "?", Snackbar.LENGTH_SHORT).show()
        }
    }






    override fun onHostFileClick(hostFile: HostFile) {
        RootManager.replaceHostFile(hostFile)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuMainBackup -> {
                RootManager.backupSystemHostFile()
            }
            android.R.id.home -> {
                toggleDrawer()
            }
        }
        return true
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
