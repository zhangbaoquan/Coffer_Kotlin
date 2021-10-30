package com.kc.coffer.widget;

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.kc.coffer.util.dipToPixel
import com.kc.coffer.util.dipToPixel2

/**
 * author      : coffer
 * date        : 10/27/21
 * description :
 */
open class DrawGraphView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mPaint: Paint
    private val mTextPaint: Paint
    private var mWidth = 0
    private var mHeight = 0

    private var mSize = 0
    private var mRoundSize = 0

    private val mPicture: Picture

    init {
        // 初始化一些大小
        mSize = dipToPixel(context, 80f)
        mRoundSize = dipToPixel(context, 8f)

        // 设置画笔属性：黑色、抗锯齿、镂空

        // 设置画笔属性：黑色、抗锯齿、镂空
        mPaint = Paint()
        mPaint.color = Color.BLACK
        mPaint.strokeWidth = dipToPixel2(context, 1f)
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE

        // 设置文字画笔属性：大小8dp、红色、对齐居中、抗锯齿、镂空
        mTextPaint = Paint()
        mTextPaint.textSize = dipToPixel2(context, 10f)
        mTextPaint.color = Color.RED
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        // Picture看作是一个录制Canvas操作的录像机
        mPicture = Picture()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        if (widthMode == MeasureSpec.EXACTLY) {
            // 精确测量
            mWidth = widthSize
        } else {
            // 最大值
            mWidth = mSize
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize
        } else {
            mHeight = mSize
        }
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawTest(canvas)
    }

    private fun drawTest(canvas: Canvas) {
        // 绘制矩形
        val rectF = RectF(
            0f, 0f, mWidth.toFloat(),
            mHeight.toFloat()
        )
        // 绘制圆角矩形
        canvas.drawRoundRect(rectF, mRoundSize.toFloat(), mRoundSize.toFloat(), mPaint)

        // 绘制文字（居中）,首先需要计算基准线,y = 矩形中心y值 + 矩形中心与基线的距离,
        // 文字高度的一半 - 基线到文字底部的距离（也就是bottom）
        val fontMetrics = mTextPaint.fontMetrics
        val distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
        val baseLine = rectF.centerY() + distance
        canvas.drawText("coffer", rectF.centerX(), baseLine, mTextPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }
}
