package com.creepersan.switchhost.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.creepersan.switchhost.R

class PageIndicatorView : View{

    constructor(context:Context):this(context, null)
    constructor(context:Context, attr:AttributeSet?):this(context, attr, 0)
    constructor(context:Context, attr:AttributeSet?, defStyle:Int):super(context, attr, defStyle){
        attr?.apply {
            val typedArray = context.obtainStyledAttributes(attr, R.styleable.PageIndicatorView)
            mCount = typedArray.getInteger(R.styleable.PageIndicatorView_count, mCount)
            mPosition = typedArray.getFloat(R.styleable.PageIndicatorView_position, mPosition)
            typedArray.recycle()
        }
        formatValue()
    }

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mCount = 6
    private var mPosition = 3f
    private var mDotColor = Color.RED
    private var mDotSize = 16f
    private var mActiveDotColor = Color.BLUE
    private var mActiveDotSize = 26f
    private var mDotDistance = 48f

    fun setCount(count:Int){
        if (count >=  1){
            mCount = count
        }else{
            mCount = 1
        }
    }

    fun setProgress(progress:Float){
        mPosition = when{
            progress > mCount   -> mCount.toFloat()
            progress < 1        -> 1f
            else                -> progress
        }
    }

    private fun formatValue(){
        if (mCount <  1){
            mCount = 1
        }
        mPosition = when{
            mPosition > mCount   -> mCount.toFloat()
            mPosition < 1        -> 1f
            else                 -> mPosition
        }
    }


    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        canvas ?: return
        if (mCount < 1){
            return
        }
        val rectPadding = RectF(paddingLeft.toFloat(), paddingTop.toFloat(), (width - paddingRight).toFloat(), (bottom - paddingBottom).toFloat())
        val largeDotSize = mDotSize.coerceAtLeast(mActiveDotSize)
        val rectDraw = RectF(
            rectPadding.centerX() - (((mCount.toFloat() - 1) / 2f) * mDotDistance) - largeDotSize,// L
            rectPadding.centerY() - largeDotSize,// T
            rectPadding.centerX() + (((mCount.toFloat() - 1) / 2f) * mDotDistance) + largeDotSize,// R
            rectPadding.centerY() + largeDotSize// B
        )
        // 绘制所有点
        mPaint.color = mDotColor
        for (i in 0 until mCount){
            canvas.drawCircle(
                rectDraw.left + largeDotSize + i * mDotDistance,
                rectDraw.centerY(),
                mDotSize,
                mPaint
            )
        }
        // 绘制当前位置点
        mPaint.color = mActiveDotColor
        canvas.drawCircle(
            rectDraw.left + largeDotSize + (mPosition - 1) * mDotDistance,
            rectDraw.centerY(),
            mActiveDotSize,
            mPaint
        )
    }


}