package com.creepersan.switchhost.application

import android.app.Application
import com.creepersan.switchhost.manager.FileManager
import com.creepersan.switchhost.manager.PermissionManager
import com.creepersan.switchhost.manager.RootManager
import kotlin.system.exitProcess

class SwitchHostApplication : Application() {
    companion object{
        const val INIT_DIRECTORY = 0b0001
        const val INIT_PERMISSION = 0b0010
        const val INIT_ROOT = 0b0100

        private lateinit var mInstance : SwitchHostApplication
        fun getInstance():SwitchHostApplication{
            return mInstance
        }
    }

    private var mInitStateFlag = 0b0000

    override fun onCreate() {
        super.onCreate()
        initInstance()
        initEnvironment()
    }

    private fun initInstance(){
        mInstance = this
    }

    private fun initEnvironment(){
        // 初始化文件夹
        setInitFlag(INIT_DIRECTORY, FileManager.initApplicationDirectory())
        // 初始化权限
        setInitFlag(INIT_PERMISSION, PermissionManager.hasPermission())
        // 初始化Root
        setInitFlag(INIT_ROOT, RootManager.hasRootPermission())
    }

    /**
     * 是否初始化成功
     */
    fun isInitSuccess(initType:Int):Boolean{
        return mInitStateFlag and initType > 0
    }

    fun setInitFlag(initFlag:Int, state:Boolean){
        mInitStateFlag = if (state){
            mInitStateFlag or initFlag
        }else{
            mInitStateFlag and (initFlag.inv())
        }
    }

    fun exit(){
        exitProcess(0)
    }



}