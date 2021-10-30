package com.kc.coffer.widget;

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * author      : coffer
 * date        : 10/27/21
 * description :
 */
open class AdTipView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {

    // 边框的颜色
    private var mStrokeColor = 0

    // 边框的宽度
    private var mStrokeWidth = 0
    private var mPaint: Paint = Paint()

    private var mWidth = 0
    private var mHeight = 0

    init {
        mPaint.color = Color.BLACK
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = 10f
    }

    @Override
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        if (widthMode == MeasureSpec.EXACTLY){
            mWidth = widthSize
        }else{
            if (widthMode == MeasureSpec.AT_MOST){
                mWidth = 100
            }
        }
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (heightMode == MeasureSpec.EXACTLY){
            mHeight = heightSize
        }else{
            if (heightMode == MeasureSpec.AT_MOST){
                mHeight = 100
            }
        }
        setMeasuredDimension(mWidth,mHeight)
    }

    @Override
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(0f,0f,mWidth.toFloat(),mHeight.toFloat(),mPaint)
        canvas.drawLine(mWidth.toFloat(),0f,0f,mHeight.toFloat(),mPaint)
    }
}
