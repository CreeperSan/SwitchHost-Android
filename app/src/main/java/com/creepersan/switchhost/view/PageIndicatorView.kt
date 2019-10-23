package com.creepersan.switchhost.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
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
            mDotColor = typedArray.getColor(R.styleable.PageIndicatorView_dotColor, mDotColor)
            mDotRadius = typedArray.getDimensionPixelSize(R.styleable.PageIndicatorView_dotRadius, mDotRadius.toInt()).toFloat()
            mActiveDotColor = typedArray.getColor(R.styleable.PageIndicatorView_dotActiveColor, mActiveDotColor)
            mActiveDotRadius = typedArray.getDimensionPixelSize(R.styleable.PageIndicatorView_dotActiveRadius, mActiveDotRadius.toInt()).toFloat()
            mDotDistance = typedArray.getDimensionPixelSize(R.styleable.PageIndicatorView_dotDistance, mDotDistance.toInt()).toFloat()
            typedArray.recycle()
        }
        formatValue()
    }

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mCount = 1
    private var mPosition = 1f
    private var mDotColor = Color.parseColor("#99FFFFFF")
    private var mDotRadius = dp2px(4f)
    private var mActiveDotColor = Color.WHITE
    private var mActiveDotRadius = dp2px(6f)
    private var mDotDistance = dp2px(16f)

    fun setCount(count:Int){
        if (count >=  1){
            mCount = count
        }else{
            mCount = 1
        }
        requestLayout()
    }

    fun setProgress(progress:Float){
        mPosition = when{
            progress > mCount   -> mCount.toFloat()
            progress < 1        -> 1f
            else                -> progress
        }
        invalidate()
    }

    private fun dp2px(dp:Float):Float{
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val dotSizeLarge = Math.max(mDotRadius, mActiveDotRadius) * 2
        setMeasuredDimension(
            measureSide(widthMeasureSpec, paddingStart + paddingEnd + mDotDistance * mCount + dotSizeLarge),
            measureSide(heightMeasureSpec, paddingTop + paddingBottom +dotSizeLarge)
        )
    }

    private fun measureSide(spec:Int, defaultSize:Float):Int{
        val mode = MeasureSpec.getMode(spec)
        val size = MeasureSpec.getSize(spec)
        return when(mode){
            MeasureSpec.EXACTLY -> size
            else -> defaultSize.toInt()
        }
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        canvas ?: return
        if (mCount < 1){
            return
        }
        val rectPadding = RectF(paddingLeft.toFloat(), paddingTop.toFloat(), (width - paddingRight).toFloat(), (height - paddingBottom).toFloat())
        val largeDotSize = mDotRadius.coerceAtLeast(mActiveDotRadius)
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
                mDotRadius,
                mPaint
            )
        }
        // 绘制当前位置点
        mPaint.color = mActiveDotColor
        canvas.drawCircle(
            rectDraw.left + largeDotSize + (mPosition - 1) * mDotDistance,
            rectDraw.centerY(),
            mActiveDotRadius,
            mPaint
        )
    }


}