package com.creepersan.switchhost.manager

import android.util.Log
import com.creepersan.switchhost.bean.HostFile
import java.io.DataOutputStream
import java.io.File
import java.lang.Exception

object RootManager {

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
        }catch (e:Exception){
            e.printStackTrace()
            return false
        }
        return true
    }

    fun execs(cmds:Array<String>){
        val commandBuilder = StringBuilder()
        cmds.forEach { cmd ->
            commandBuilder.append(cmd).append(";")
        }
        exec(commandBuilder.toString())
    }

    fun replaceHostFile(hostFile:HostFile){
        val srcHostFilePath = hostFile.path
        val targetHostFilePath = FileManager.getSystemHostFile().path
        execs(arrayOf(
            "mount -o remount,rw /dev/block/mmcblk0p63 /system",
            "cp $srcHostFilePath $targetHostFilePath",
            "chmod 644 $targetHostFilePath",
            "mount -o remount,ro /dev/block/mmcblk0p63 /system"
        ))
    }

    fun backupSystemHostFile(){
        val srcHostFilePath = FileManager.getSystemHostFile().path
        val targetHostFilePath = File("${FileManager.getBackupDirectory().path}/hosts_${FormatManager.formatBackupFileTimes(System.currentTimeMillis())}")
        execs(arrayOf(
            "cp $srcHostFilePath $targetHostFilePath"
        ))
    }

}