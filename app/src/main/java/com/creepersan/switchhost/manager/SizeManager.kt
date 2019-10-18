package com.creepersan.switchhost.manager

import android.content.Context
import android.util.TypedValue
import android.view.WindowManager
import com.creepersan.switchhost.R
import com.creepersan.switchhost.application.SwitchHostApplication

object SizeManager {
    private val context get() = SwitchHostApplication.getInstance()

    val HOST_FILE_ITEM_WIDTH by lazy { getSize(R.dimen.hostFileItem_width) }

    fun getScreenPxSize():Int{
        return context.resources.displayMetrics.widthPixels
    }

    fun dp2px(dp:Float):Float{
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
    }

    private fun getSize(id:Int):Int{
        return context.resources.getDimensionPixelSize(id)
    }

}