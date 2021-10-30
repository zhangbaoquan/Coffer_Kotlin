package com.kc.coffer.widget;

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.kc.coffer.model.PieData
import com.kc.coffer.util.dipToPixel

/**
 * author      : coffer
 * date        : 10/27/21
 * description :
 */
open class PieView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mColors = arrayOf(
        Color.RED, Color.BLACK,
        Color.YELLOW, Color.BLUE,
        Color.GREEN, Color.GRAY
    )

    private val mPaint: Paint
    private val mData: MutableList<PieData>
    private var mWidth = 0
    private var mHeight = 0
    private var mSize = 0

    /**
     * 开始角度
     */
    private var mStartAngle = 0

    init {
        mData = ArrayList()
        mSize = dipToPixel(context, 80f)
        mPaint = Paint()
        mPaint.style = Paint.Style.FILL
        mPaint.isAntiAlias = true
        for (i in 0 until 6){
            val data = PieData()
            data.color = mColors[i]
            data.angle = 60
            mData.add(data)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        mWidth = if (widthMode == MeasureSpec.EXACTLY) {
            // 精确测量
            widthSize
        } else {
            // 最大值
            mSize
        }

        mHeight = if (heightMode == MeasureSpec.EXACTLY) {
            heightSize
        } else {
            mSize
        }
        setMeasuredDimension(mWidth, mHeight)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val rectF = RectF(
            0f, 0f, mWidth.toFloat(),
            mHeight.toFloat()
        )
        for (i in mData.indices) {
            val endAngle = mData[i].angle
            mPaint.color = mData[i].color
            canvas.drawArc(rectF, mStartAngle.toFloat(), endAngle.toFloat(), true, mPaint)
            mStartAngle += endAngle
        }
    }
}
