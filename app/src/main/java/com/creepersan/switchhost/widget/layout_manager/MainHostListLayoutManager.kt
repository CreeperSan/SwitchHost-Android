package com.creepersan.switchhost.widget.layout_manager

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import com.creepersan.switchhost.manager.SizeManager

class MainHostListLayoutManager(context: Context, spanCalculator:(pos:Int, widthCount:Int)->Int) : GridLayoutManager(context, getWidthItemCount()){

    companion object{
        private var mWidthItemCount = 0

        fun getWidthItemCount():Int{
            if (mWidthItemCount <= 0){
                mWidthItemCount = SizeManager.getScreenPxSize() / SizeManager.HOST_FILE_ITEM_WIDTH
                if (mWidthItemCount <= 0){
                    mWidthItemCount = 1
                }
            }
            return mWidthItemCount
        }
    }

    init {
        spanSizeLookup = object : SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                return spanCalculator.invoke(position, mWidthItemCount)
            }

        }
    }

}