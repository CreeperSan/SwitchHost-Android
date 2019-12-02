package com.creepersan.switchhost.manager

import android.content.Context
import com.creepersan.switchhost.application.SwitchHostApplication
import com.creepersan.switchhost.bean.HostFile
import java.util.ArrayList
import java.util.HashSet

object PrefManager{
    private const val PREF_COLLECTION = "collection.xml"
    private const val COLLECTION_PATHS = "paths"

    private val mContext by lazy { SwitchHostApplication.getInstance() }

    private val mCollectionPref = mContext.getSharedPreferences(PREF_COLLECTION, Context.MODE_PRIVATE)

    fun getCollectionFileList():ArrayList<HostFile>{
        val hostFileSet =  mCollectionPref.getStringSet(COLLECTION_PATHS, HashSet<String>()) ?: HashSet()
        val list = ArrayList<HostFile>()
        hostFileSet.forEach {  path ->
            val hostFile = HostFile.new(path)
            if (hostFile!=null){
                list.add(hostFile)
            }
        }
        return list
    }

    fun setCollectionFileList(hostFileCollection: Collection<HostFile>){
        val pathSet = HashSet<String>()
        hostFileCollection.forEach { hostFile ->
            pathSet.add(hostFile.path)
        }
        mCollectionPref
            .edit()
            .putStringSet(COLLECTION_PATHS, pathSet)
            .apply()
    }

}