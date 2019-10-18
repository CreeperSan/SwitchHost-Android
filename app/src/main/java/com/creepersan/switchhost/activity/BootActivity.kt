package com.creepersan.switchhost.activity

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.creepersan.switchhost.application.SwitchHostApplication
import com.creepersan.switchhost.manager.FileManager
import com.creepersan.switchhost.manager.PermissionManager

class BootActivity : BaseActivity(){
    companion object{
        private const val REQUEST_CODE_PERMISSION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPermission()
    }

    private fun initPermission(){
        if (PermissionManager.hasPermission()){
            onCheckPermissionFinish()
        }else{
            PermissionManager.requirePermission(this, REQUEST_CODE_PERMISSION)
        }
    }

    private fun onCheckPermissionFinish(){
        if (!application.isInitSuccess(SwitchHostApplication.INIT_DIRECTORY)){
            application.setInitFlag(SwitchHostApplication.INIT_DIRECTORY, FileManager.initApplicationDirectory())
        }
        toActivity(MainActivity::class.java, true)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION){
            grantResults.forEach { permissionGrantResult ->
                if (permissionGrantResult != PackageManager.PERMISSION_GRANTED){
                    application.setInitFlag(SwitchHostApplication.INIT_PERMISSION, false)
                    onCheckPermissionFinish()
                    return
                }
            }
            application.setInitFlag(SwitchHostApplication.INIT_PERMISSION, true)
            onCheckPermissionFinish()
            return
        }
    }

}