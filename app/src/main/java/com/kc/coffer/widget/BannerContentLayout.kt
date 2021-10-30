package com.kc.coffer.widget;

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import com.kc.coffer.R
import com.kc.coffer.util.dipToPixel
import java.lang.Exception
import java.lang.ref.WeakReference
import java.util.ArrayList
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

/**
 * author      : coffer
 * date        : 10/27/21
 * description :
 */
class BannerContentLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr) {

    companion object {
        private const val TAG = "BannerContentLayout_TAG"

        /**
         * 放手之后banner归位动画时长
         */
        private const val ANIMATION_DURATION: Long = 350

        /**
         * 轮播时间间隔
         */
        private const val BANNER_TIME = 3000

        private const val MSG_SCROLL = 1
    }

    /**
     * indicator 默认色
     */
    private var mIndicatorDefaultColor = 0

    /**
     * indicator 选中色
     */
    private var mIndicatorSelectedColor = 0

    /**
     * 当前位置的indicator颜色
     */
    private var mCurIndicatorColor = 0
    private var mNextIndicatorColor = 0
    private var mPreIndicatorColor = 0

    /**
     * indicator 半径
     */
    private var mIndicatorRadius = 0

    /**
     * 选中的indicator 半径
     */
    private var mSelectedIndicatorRadius = 0f
    private var mCurIndicatorRadius = 0f
    private var mNextIndicatorRadius = 0f
    private var mPreIndicatorRadius = 0f

    /**
     * indicator 之间 padding
     */
    private var mIndicatorPadding = 0

    /**
     * indicator 距离底部bottom
     */
    private var mIndicatorBottom = 0

    /**
     * indicator 绘制位置y
     */
    private var mIndicatorCircleY = 0
    private val mIndicatorPaint: Paint

    /**
     * 放手之后banner归位动画时长
     */
    private var mAnimationDuration: Long = 0

    /**
     * 轮播时间间隔
     */
    private var mBannerDuration = 0

    /**
     * 当前位置
     */
    private var mCurPosition = 0

    /**
     * 下一个view的位置
     */
    private var mNextPosition = 0

    /**
     * 上一个view的位置
     */
    private var mPrePosition = 0
    private var mItemCount = 0
    private val mItemViews: MutableList<View>

    private var mTouchSlop = 0

    /**
     * 滚动距离
     */
    private var mScrollX = 0
    private var mRunning = false
    private var mCanPlay = false
    private val mAnimator: ValueAnimator
    private var mVisible = false
    private var mUserPresent = true
    private val mHandler: BannerHandler
    private var mReceiverRegister = false

    /**
     * 记录回调的位置
     */
    private var mBannerChangePosition = -1
    private var mBannerChangedListener: OnBannerChangedListener? = null


    init {
        mTouchSlop = ViewConfiguration.get(context).scaledPagingTouchSlop
        mIndicatorDefaultColor = context.resources.getColor(R.color.banner_indicator_default)
        mIndicatorSelectedColor = context.resources.getColor(R.color.banner_indicator_selected)
        mIndicatorRadius = dipToPixel(context.resources, 3)
        mSelectedIndicatorRadius = mIndicatorRadius.toFloat()
        mIndicatorPadding = dipToPixel(context.resources, 4)
        mIndicatorBottom = dipToPixel(context.resources, 4)

        mIndicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mAnimationDuration = ANIMATION_DURATION
        mBannerDuration = BANNER_TIME

        mAnimator = ValueAnimator.ofFloat(0f, 1f)
        mAnimator.duration = mAnimationDuration
        mAnimator.interpolator = DecelerateInterpolator()
        mHandler = BannerHandler(this)
        mItemViews = ArrayList<View>()
    }

    private fun handleScroll() {
        if (mScrollX == 0) {
            mScrollX = -1
            updateViewPosition(mScrollX)
            animToPosition()
        }
    }

    private fun preDraw() {
        mPrePosition = if (mCurPosition - 1 < 0) mItemViews.size - 1 else mCurPosition - 1
        mNextPosition = if (mCurPosition + 1 > mItemViews.size - 1) 0 else mCurPosition + 1
        updateViewPosition(mScrollX)
        updateIndicator(mScrollX)
        invalidate()
    }

    private fun updateViewPosition(scrollX: Int) {
        if (mCurPosition >= 0 && mCurPosition < mItemViews.size) {
            val curView = mItemViews[mCurPosition]
            curView.layout(
                scrollX,
                paddingTop,
                curView.measuredWidth + scrollX,
                height - paddingBottom
            )
            curView.invalidate()
            if (scrollX > 0 && mPrePosition >= 0 && mPrePosition < mItemViews.size) {
                val preView = mItemViews[mPrePosition]
                preView.layout(
                    curView.left - preView.measuredWidth,
                    curView.top, curView.left, curView.bottom
                )
                preView.invalidate()
            }
            if (scrollX < 0 && mNextPosition >= 0 && mNextPosition < mItemViews.size) {
                val nextView = mItemViews[mNextPosition]
                nextView.layout(
                    curView.right, curView.top,
                    nextView.left + nextView.measuredWidth, curView.bottom
                )
                nextView.invalidate()
            }
            mBannerChangedListener?.let {
                val positionOffset = abs(scrollX * 1f / width)
                var nextPosition = if (scrollX > 0) mCurPosition - 1 else mCurPosition + 1
                if (nextPosition >= mItemViews.size) {
                    nextPosition = 0
                } else if (nextPosition < 0) {
                    nextPosition = mItemViews.size - 1
                }
                it.onBannerScrolled(
                    mCurPosition,
                    nextPosition,
                    positionOffset
                )
            }
        }
    }

    private fun updateIndicator(scrollX: Int) {
        val absScrollX = abs(scrollX)
        val fraction = absScrollX * 1f / width
        if (fraction == 0f) {
            //半径
            mCurIndicatorRadius = mSelectedIndicatorRadius
            mPreIndicatorRadius = mIndicatorRadius.toFloat()
            mNextIndicatorRadius = mIndicatorRadius.toFloat()

            //颜色
            mCurIndicatorColor = mIndicatorSelectedColor
            mPreIndicatorColor = mIndicatorDefaultColor
            mNextIndicatorColor = mIndicatorDefaultColor
        } else {
            val radiusChangedSize = (mSelectedIndicatorRadius - mIndicatorRadius) * fraction
            if (scrollX > 0) {
                mPreIndicatorRadius = mIndicatorRadius + radiusChangedSize
                mCurIndicatorRadius = mSelectedIndicatorRadius - radiusChangedSize
                mNextIndicatorRadius = mIndicatorRadius.toFloat()
                mPreIndicatorColor =
                    getColor(fraction, mIndicatorDefaultColor, mIndicatorSelectedColor)
                mCurIndicatorColor =
                    getColor(fraction, mIndicatorSelectedColor, mIndicatorDefaultColor)
                mNextIndicatorColor = mIndicatorDefaultColor
            } else if (scrollX < 0) {
                mPreIndicatorRadius = mIndicatorRadius.toFloat()
                mCurIndicatorRadius = mSelectedIndicatorRadius - radiusChangedSize
                mNextIndicatorRadius = mIndicatorRadius + radiusChangedSize
                mPreIndicatorColor = mIndicatorDefaultColor
                mCurIndicatorColor =
                    getColor(fraction, mIndicatorSelectedColor, mIndicatorDefaultColor)
                mNextIndicatorColor =
                    getColor(fraction, mIndicatorDefaultColor, mIndicatorSelectedColor)
            }
        }
    }

    private fun getCurView(): View? {
        return if (mCurPosition >= 0 && mCurPosition < mItemViews.size) {
            mItemViews[mCurPosition]
        } else null
    }

    private fun getPreView(): View? {
        return if (mPrePosition >= 0 && mPrePosition < mItemViews.size) {
            mItemViews[mPrePosition]
        } else null
    }

    private fun getNextView(): View? {
        return if (mNextPosition >= 0 && mNextPosition < mItemViews.size) {
            mItemViews[mNextPosition]
        } else null
    }

    /**
     * 放手后滑动到期望位置
     */
    private fun animToPosition() {
        val curView = getCurView()
        if (curView != null && curView.left != 0) {
            val originalX = mScrollX
            val deltaX: Int = if (curView.left > 0) {
                val preView = getPreView()
                if (preView == null) 0 else -preView.left
            } else {
                val nextView = getNextView()
                if (nextView == null) 0 else -nextView.left
            }
            mAnimator.removeAllUpdateListeners()
            mAnimator.removeAllListeners()
            mAnimator.addUpdateListener { animation ->
                val animatorValue = animation.animatedValue as Float
                mScrollX = (originalX + animatorValue * deltaX).toInt()
                preDraw()
            }
            mAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    mCurPosition = if (deltaX < 0) {
                        mNextPosition
                    } else {
                        mPrePosition
                    }
                    callBannerSelected(mCurPosition)
                    mScrollX = 0
                    preDraw()
                }
            })
            mAnimator.start()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!mReceiverRegister) {
            val filter = IntentFilter()
            filter.addAction(Intent.ACTION_SCREEN_OFF)
            filter.addAction(Intent.ACTION_USER_PRESENT)
            try {
                context.registerReceiver(mReceiver, filter)
                mReceiverRegister = true
            } catch (e: Exception) {
                Log.e(TAG, e.message)
            }
        }
        mVisible = visibility == VISIBLE
        updateRunning()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (mReceiverRegister) {
            try {
                context.unregisterReceiver(mReceiver)
                mReceiverRegister = false
            } catch (e: Exception) {
                Log.e(TAG, e.message)
            }
        }
        updateRunning()
//        for (int i = 0; i < mItemViews.size(); i++) {
//            if (mBannerAnimators.get(i) != null && mBannerAnimators.get(i).isRunning()) {
//                updatePaint(1.0f, i);
//            }
//        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        preDraw()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val childCount = mItemViews.size
        var child: View
        for (i in 0 until childCount) {
            child = mItemViews[i]
            val lp = child.layoutParams as MarginLayoutParams
            val childWidthMeasureSpec: Int
            if (lp.width == LayoutParams.MATCH_PARENT) {
                val width = max(
                    0, measuredWidth
                            - paddingLeft - paddingRight
                            - lp.leftMargin - lp.rightMargin
                )
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    width, MeasureSpec.EXACTLY
                )
            } else {
                childWidthMeasureSpec = getChildMeasureSpec(
                    widthMeasureSpec,
                    paddingLeft + paddingRight +
                            lp.leftMargin + lp.rightMargin,
                    lp.width
                )
            }
            val childHeightMeasureSpec: Int
            if (lp.height == LayoutParams.MATCH_PARENT) {
                val height = max(
                    0, (measuredHeight
                            - paddingTop - paddingBottom
                            - lp.topMargin - lp.bottomMargin)
                )
                childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    height, MeasureSpec.EXACTLY
                )
            } else {
                childHeightMeasureSpec = getChildMeasureSpec(
                    heightMeasureSpec,
                    (paddingTop + paddingBottom +
                            lp.topMargin + lp.bottomMargin),
                    lp.height
                )
            }
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        preDraw()
        mIndicatorCircleY = height - mIndicatorBottom
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (mCanPlay) {
            drawIndicator(canvas)
        }
    }

    private fun drawIndicator(canvas: Canvas) {
        val itemCount = mItemViews.size
        val indicatorRectWidth =
            itemCount * mIndicatorRadius * 2 + (itemCount - 1) * mIndicatorPadding
        val startIndicator = width / 2 - indicatorRectWidth / 2
        for (i in 0 until itemCount) {
            mIndicatorPaint.color = getTempColor(i)
            canvas.drawCircle(
                (startIndicator + mIndicatorRadius + i * (mIndicatorPadding + 2 * mIndicatorRadius)).toFloat(),
                mIndicatorCircleY.toFloat(), getRadius(i), mIndicatorPaint
            )
        }
    }

    private fun getTempColor(i: Int): Int {
        return when (i) {
            mCurPosition -> mCurIndicatorColor
            mNextPosition -> mNextIndicatorColor
            mPrePosition -> mPreIndicatorColor
            else -> mIndicatorDefaultColor
        }
    }


    private fun getRadius(i: Int): Float {
        return when (i) {
            mCurPosition -> mCurIndicatorRadius
            mNextPosition -> mNextIndicatorRadius
            mPrePosition -> mPreIndicatorRadius
            else -> mIndicatorRadius.toFloat()
        }
    }

    override fun addView(child: View) {
        super.addView(child)
        mItemViews.add(child)
        mItemCount = mItemViews.size
        if (mItemCount > 1) {
            mCanPlay = true
        }
        requestLayout()
        updateRunning()
    }

    override fun removeAllViews() {
        super.removeAllViews()
        mItemViews.clear()
        mCurPosition = 0
        mPrePosition = 0
        mNextPosition = 0
    }

    override fun onScreenStateChanged(screenState: Int) {
        super.onScreenStateChanged(screenState)
        mVisible = screenState == SCREEN_STATE_ON
        updateRunning()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        mVisible = visibility == VISIBLE
        updateRunning()
    }

    private val mDownPoint = Point()
    private val mMovePoint = Point()
    private var mIsDragging = false
    private var mLastX = 0
    private var mLastActionX = 0
    private var mLastActionY = 0
    private var mHandleEvent = true
    private var mHasComputeHandleEvent = false

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        if (mCanPlay) {
//            PluginRely.enableGesture(false);
        }
        //动画过程中禁止点击
        if (mAnimator.isRunning) {
            return false
        }
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mHasComputeHandleEvent = false
                mDownPoint[x] = y
                mIsDragging = false
                mScrollX = 0
                mLastX = x
                mLastActionX = x
                mLastActionY = y
                if (mCanPlay) {
                    mHandler.removeMessages(MSG_SCROLL)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                mMovePoint[x] = y
                //判断是滑动还是点击
                if (calculateA2B(mDownPoint, mMovePoint) >= mTouchSlop) {
                    mIsDragging = true
                }
                if (mIsDragging && !mHasComputeHandleEvent) {
                    mHandleEvent = abs(x - mLastActionX) > abs(y - mLastActionY)
                    mHasComputeHandleEvent = true
                    return true
                }
            }
            MotionEvent.ACTION_UP -> {
                if (mCanPlay && !mHandler.hasMessages(MSG_SCROLL)) {
                    mHandler.sendEmptyMessageDelayed(MSG_SCROLL, mBannerDuration.toLong())
                }
            }
            else -> {
            }
        }
        return super.onInterceptTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mCanPlay) {
//            PluginRely.enableGesture(false);
        }
        //动画过程中禁止点击
        if (mAnimator.isRunning) {
            return false
        }
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDownPoint[x] = y
                mIsDragging = false
                mScrollX = 0
                mLastX = x
                mLastActionX = x
                mLastActionY = y
                if (mCanPlay) {
                    mHandler.removeMessages(MSG_SCROLL)
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = x - mLastX
                mMovePoint[x] = y
                //判断是滑动还是点击
                if (calculateA2B(mDownPoint, mMovePoint) >= mTouchSlop) {
                    mIsDragging = true
                }
                if (mIsDragging && !mHasComputeHandleEvent) {
                    mHandleEvent = abs(x - mLastActionX) > abs(y - mLastActionY)
                    mHasComputeHandleEvent = true
                }
                if (mCanPlay) {
                    parent.requestDisallowInterceptTouchEvent(mHandleEvent)
                }
                mLastActionX = x
                mLastActionY = y
                if (mIsDragging && mCanPlay && mHandleEvent) {
                    mScrollX = dx
                    preDraw()
                    return true
                }
            }
            MotionEvent.ACTION_UP -> {
                mHasComputeHandleEvent = false
                if (mIsDragging && mScrollX != 0) {
                    animToPosition()
                }
                if (mCanPlay && !mHandler.hasMessages(MSG_SCROLL)) {
                    mHandler.sendEmptyMessageDelayed(MSG_SCROLL, mBannerDuration.toLong())
                }
                if (mIsDragging) return true
                mIsDragging = false
                mLastX = 0
                mLastActionX = 0
                mLastActionY = 0
                mHasComputeHandleEvent = false
                if (mCanPlay) {
//                    PluginRely.enableGesture(true);
                    if (!mHandler.hasMessages(MSG_SCROLL)) {
                        mHandler.sendEmptyMessageDelayed(MSG_SCROLL, mBannerDuration.toLong())
                    }
                }
            }
            MotionEvent.ACTION_OUTSIDE, MotionEvent.ACTION_CANCEL -> {
                mIsDragging = false
                mLastX = 0
                mLastActionX = 0
                mLastActionY = 0
                mHasComputeHandleEvent = false
                if (mCanPlay) {
                    if (!mHandler.hasMessages(MSG_SCROLL)) {
                        mHandler.sendEmptyMessageDelayed(MSG_SCROLL, mBannerDuration.toLong())
                    }
                }
            }
            else -> {
            }
        }
        return super.onTouchEvent(event)
    }

    private fun updateRunning() {
        if (this.mBannerChangePosition != mCurPosition) {
            callBannerSelected(mCurPosition)
        }
        val running = mVisible && mUserPresent && mCanPlay
        if (running != mRunning) {
            mRunning = running
            mHandler.removeMessages(MSG_SCROLL)
            if (running) {
                mHandler.sendEmptyMessageDelayed(MSG_SCROLL, mBannerDuration.toLong())
            }
        }
    }

    private fun callBannerSelected(position: Int) {
        mBannerChangedListener?.let {
            mBannerChangePosition = position
            it.onBannerSelected(position)
        }

    }

    /**
     * >> 相当于 shr
     * << 相当于 shl
     * &  相当于 and
     * |  相当于 or
     */
    private fun getColor(fraction: Float, startValue: Int, endValue: Int): Int {
        // int startA = (startValue >> 24) & 0xff;
        val startA = startValue shr 24 and 0xff
        val startR = startValue shr 16 and 0xff
        val startG = startValue shr 8 and 0xff
        val startB = startValue and 0xff
        val endA = endValue shr 24 and 0xff
        val endR = endValue shr 16 and 0xff
        val endG = endValue shr 8 and 0xff
        val endB = endValue and 0xff
        return startA + (fraction * (endA - startA)).toInt() shl 24 or (
                startR + (fraction * (endR - startR)).toInt() shl 16) or (
                startG + (fraction * (endG - startG)).toInt() shl 8) or
                startB + (fraction * (endB - startB)).toInt()
    }

    private fun calculateA2B(startPoint: Point, endPoint: Point): Int {
        val xDist = startPoint.x - endPoint.x
        val yDist = startPoint.y - endPoint.y
        return sqrt((xDist * xDist + yDist * yDist).toDouble()).toInt()
    }

    open fun setOnBannerChangedListener(listener: OnBannerChangedListener) {
        mBannerChangedListener = listener
    }

    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            //PluginLogUtils.i("== onReceive action:"+action+" mUserPresent:"+mUserPresent);
            if (action == Intent.ACTION_SCREEN_OFF) {
                mUserPresent = false
                updateRunning()
            } else if (action == Intent.ACTION_USER_PRESENT) {
                mUserPresent = true
                updateRunning()
            }
        }
    }

    /**
     * 这里的写法表示的是静态内部类。如果想要表达非静态内部类，需要在class前声明一个inner关键字
     */
    private class BannerHandler constructor(bannerContentLayout: BannerContentLayout) : Handler() {

        private val mWeakBannerLayout: WeakReference<BannerContentLayout>?
        private val mRect: Rect

        init {
            mWeakBannerLayout = WeakReference(bannerContentLayout)
            mRect = Rect()
        }

        override fun handleMessage(msg: Message) {
            if (msg.what == MSG_SCROLL
                && mWeakBannerLayout != null
            ) {
                val layout = mWeakBannerLayout.get()
                if (layout != null && layout.parent != null && layout.mRunning) {
                    if (layout.getLocalVisibleRect(mRect)
                        && mRect.left == layout.left && mRect.right == layout.right
                    ) {
                        layout.handleScroll()
                    }
                    sendEmptyMessageDelayed(
                        MSG_SCROLL,
                        layout.mBannerDuration.toLong()
                    )
                }
            }

        }
    }

//    private class BannerHandler constructor(bannerContentLayout: BannerContentLayout) :
//        Handler() {
//        private val mWeakBannerLayout: WeakReference<BannerContentLayout>?
//        private val mRect: Rect
//        override fun handleMessage(msg: Message) {
//            if (msg.what == BannerContentLayout.MSG_SCROLL
//                && mWeakBannerLayout != null
//            ) {
//                val layout = mWeakBannerLayout.get()
//                if (layout != null && layout.parent != null && layout.mRunning) {
//                    if (layout.getLocalVisibleRect(mRect)
//                        && mRect.left == layout.left && mRect.right == layout.right
//                    ) {
//                        layout.handleScroll()
//                    }
//                    sendEmptyMessageDelayed(
//                        BannerContentLayout.MSG_SCROLL,
//                        layout.mBannerDuration.toLong()
//                    )
//                }
//            }
//        }
//
//        init {
//            mWeakBannerLayout = WeakReference(bannerContentLayout)
//            mRect = Rect()
//        }
//    }

    interface OnBannerChangedListener {
        /**
         * @param position       当前位置
         * @param nextPosition   下一个 要到达的位置
         * @param positionOffset 偏移百分比
         */
        fun onBannerScrolled(position: Int, nextPosition: Int, positionOffset: Float)
        fun onBannerSelected(position: Int)
    }
}
