package com.creepersan.switchhost.manager

import android.content.Context
import android.content.pm.PackageInfo
import android.os.Build

object SelfManager {
    private var packageInfo : PackageInfo? = null

    private fun getPackageInfo(context: Context):PackageInfo{
        if (packageInfo == null){
            packageInfo = context.applicationContext.packageManager.getPackageInfo(context.packageName, 0)
        }
        return packageInfo!!
    }

    fun getVersionCode(context: Context):Long{
        return getPackageInfo(context).longVersionCode
    }

    fun getVersionName(context: Context):String{
        return getPackageInfo(context).versionName
    }

}