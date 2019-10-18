package com.creepersan.switchhost.manager

import android.os.Build
import com.creepersan.switchhost.application.SwitchHostApplication
import java.text.SimpleDateFormat
import java.util.*

object FormatManager {
    private val mSimpleFormatter = SimpleDateFormat("YYYY_MM_dd_HH_mm_ss", getLocale())

    private fun getLocale():Locale{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            SwitchHostApplication.getInstance().resources.configuration.locales[0]
        }else{
            SwitchHostApplication.getInstance().resources.configuration.locale
        }
    }

    fun formatBackupFileTimes(timeStamp:Long):String{
        return mSimpleFormatter.format(timeStamp)
    }

}