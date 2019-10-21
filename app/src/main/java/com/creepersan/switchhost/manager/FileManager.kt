package com.creepersan.switchhost.manager

import android.os.Environment
import com.creepersan.switchhost.bean.HostFile
import java.io.File
import java.util.*

object FileManager {
    private val NAME_DIRECTORY_ROOT = "SwitchHost"
    private val NAME_DIRECTORY_HOST = "host"
    private val NAME_DIRECTORY_UPDATE = "update"
    private val NAME_DIRECTORY_BACKUP = "backup"

    fun getApplicationDirectory(): File {
        return File("${Environment.getExternalStorageDirectory().path}/$NAME_DIRECTORY_ROOT")
    }

    fun getHostDirectory():File{
        return File("${getApplicationDirectory().path}/$NAME_DIRECTORY_HOST")
    }

    fun getUpdateDirectory():File{
        return File("${getApplicationDirectory().path}/$NAME_DIRECTORY_UPDATE")
    }

    fun getBackupDirectory():File{
        return File("${getApplicationDirectory().path}/$NAME_DIRECTORY_BACKUP")
    }

    fun getSystemHostFile():File{
//        return File("/system/etc/${System.currentTimeMillis()}.host")
        return File("/system/etc/hosts")
//        return File("/storage/emulated/0/${System.currentTimeMillis()}.host")
    }

    fun initApplicationDirectory():Boolean{
        arrayListOf(
            getApplicationDirectory(),
            getHostDirectory(),
            getUpdateDirectory(),
            getBackupDirectory()
        ).forEach {  directory ->
            if (!initDirectory(directory)){
                return false
            }
        }
        return true
    }

    fun getAllHostFile(): ArrayList<HostFile> {
        val list = ArrayList<HostFile>()
        getHostDirectory().listFiles()?.forEach {
            val hostFile = HostFile.new(it.path)
            if (hostFile != null){
                list.add(hostFile)
            }
        }
        list.sortBy { hostFile -> hostFile.name.toLowerCase(Locale.getDefault()) }
        return list
    }

    fun getAllBackupHostFile(): ArrayList<HostFile>{
        val list = ArrayList<HostFile>()
        getBackupDirectory().listFiles()?.forEach {
            val hostFile = HostFile.new(it.path)
            if (hostFile != null){
                list.add(hostFile)
            }
        }
        list.sortBy { hostFile -> hostFile.name.toLowerCase(Locale.getDefault()) }
        return list
    }

    fun deleteHostFile(hostFile:HostFile):Boolean{
        val file = File(hostFile.path)
        if (!file.exists()) {
            return true
        }
        return file.delete()
    }

    fun hasBackupHostFile():Boolean{
        return getBackupDirectory().list()?.isNotEmpty() ?: false
    }

    /**
     * 初始化文件夹，有则使用，无则创建
     */
    private fun initDirectory(directory:File):Boolean{
        if (directory.exists()){
            return directory.isDirectory
        }else{
            return directory.mkdirs()
        }
    }

}