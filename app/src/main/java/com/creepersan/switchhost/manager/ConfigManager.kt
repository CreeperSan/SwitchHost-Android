package com.creepersan.switchhost.manager

import android.content.Context
import com.creepersan.switchhost.application.SwitchHostApplication

object ConfigManager{
    private const val PREF_GLOBAL = "global"
    private const val KEY_GLOBAL_HOST_NAME = "host_name"

    private val mApplication = SwitchHostApplication.getInstance()
    private val mGlobalPref by lazy { mApplication.getSharedPreferences(PREF_GLOBAL, Context.MODE_PRIVATE) }

    fun getCurrentHostName():String{
        return mGlobalPref.getString(KEY_GLOBAL_HOST_NAME, "系统默认Host") ?: "系统默认Host"
    }

    fun setCurrentHostName(hostName:String){
        mGlobalPref.edit().putString(KEY_GLOBAL_HOST_NAME, hostName).commit()
    }

}