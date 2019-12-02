package com.creepersan.switchhost.bean

import android.graphics.Color
import androidx.annotation.ColorInt
import com.creepersan.switchhost.manager.SizeManager

data class WelcomeItem(
    var img : Int = 0,
    var hintString : String = "",
    var hintStringSizeSP : Float = 14f,
    @ColorInt var hintStringColor : Int = Color.BLACK,
    var buttonText : String = "",
    var buttonClickAction : (()->Unit)? = null
)
