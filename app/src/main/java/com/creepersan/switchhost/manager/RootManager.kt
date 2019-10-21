package com.creepersan.switchhost.manager

import android.util.Log
import com.creepersan.switchhost.bean.HostFile
import java.io.DataOutputStream
import java.io.File
import java.lang.Exception

object RootManager {
    const val RESULT_CODE_SUCCESS = 0

    fun hasRootPermission():Boolean{
        return exec("exit")
    }

    fun exec(cmd:String):Boolean{
        try {
            val process = Runtime.getRuntime().exec("su")
//            val process = Runtime.getRuntime().exec("/system/bin/sh") // 8.1 或以上使用
            val outStream = process.outputStream
            val dataOutStream = DataOutputStream(process.outputStream)
            dataOutStream.writeBytes(cmd)
            dataOutStream.flush()
            dataOutStream.close()
            dataOutStream.close()
            outStream.close()
            val code = process.waitFor()
            Log.e("TAG", "root waitFor($code)")
            return code == RESULT_CODE_SUCCESS
        }catch (e:Exception){
            e.printStackTrace()
            return false
        }
    }

    fun execs(cmds:Array<String>):Boolean{
        val commandBuilder = StringBuilder()
        cmds.forEach { cmd ->
            commandBuilder.append(cmd).append(";")
        }
        return exec(commandBuilder.toString())
    }

    fun replaceHostFile(hostFile:HostFile):Boolean{
        val srcHostFilePath = hostFile.path
        val targetHostFilePath = FileManager.getSystemHostFile().path
        return execs(arrayOf(
            "mount -o remount,rw /dev/block/mmcblk0p63 /system",
            "cp $srcHostFilePath $targetHostFilePath",
            "chmod 644 $targetHostFilePath",
            "mount -o remount,ro /dev/block/mmcblk0p63 /system"
        ))
    }

    fun backupSystemHostFile():Boolean{
        val srcHostFilePath = FileManager.getSystemHostFile().path
        val targetHostFilePath = File("${FileManager.getBackupDirectory().path}/hosts_${FormatManager.formatBackupFileTimes(System.currentTimeMillis())}")
        return execs(arrayOf(
            "cp $srcHostFilePath $targetHostFilePath"
        ))
    }

}