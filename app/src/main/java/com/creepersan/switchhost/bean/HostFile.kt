package com.creepersan.switchhost.bean

import com.creepersan.switchhost.manager.FileManager
import java.io.File

class HostFile private constructor(var path:String,var type:Int){
    companion object{
        const val TYPE_PLAN = 0
        const val TYPE_BACKUP = 1
        const val TYPE_SYSTEM = 2

        fun new(path:String):HostFile?{
            val file = File(path)
            if (!file.exists() || file.isDirectory){
                return null
            }
            return when{
                file.parent == FileManager.getBackupDirectory().path -> HostFile(path, TYPE_BACKUP)
                file.parent == FileManager.getHostDirectory().path -> HostFile(path, TYPE_PLAN)
                file.path == FileManager.getSystemHostFile().path -> HostFile(path, TYPE_SYSTEM)
                else -> null
            }

        }

    }

    var name : String
    var size : Long

    init {
        val hostFile = File(path)
        name = hostFile.name
        size = hostFile.length()
    }

    override operator fun equals(other:Any?):Boolean{
        return other is HostFile? && path == other?.path
    }

}