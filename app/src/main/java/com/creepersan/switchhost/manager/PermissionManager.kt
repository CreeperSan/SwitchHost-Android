package com.creepersan.switchhost.manager

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.creepersan.switchhost.activity.BaseActivity
import com.creepersan.switchhost.application.SwitchHostApplication
import java.util.ArrayList

object PermissionManager {
    private val context get() = SwitchHostApplication.getInstance()

    private val PERMISSION_ARRAY = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    fun hasPermission():Boolean{
        PERMISSION_ARRAY.forEach {  permission ->
            val permissionFlag = hasPermission(permission)
            if(!permissionFlag){
                return false
            }
        }
        return true
    }

    fun hasPermission(permission:String):Boolean{
        val permissionFlag = ContextCompat.checkSelfPermission(context, permission)
        return permissionFlag == PackageManager.PERMISSION_GRANTED
    }

    fun requirePermission(activity:BaseActivity, requestCode:Int){
        val needPermissionList = ArrayList<String>()
        PERMISSION_ARRAY.forEach {  permission ->
            if (!hasPermission(permission)){
                needPermissionList.add(permission)
            }
        }
        ActivityCompat.requestPermissions(activity, needPermissionList.toTypedArray(), requestCode)
    }
}