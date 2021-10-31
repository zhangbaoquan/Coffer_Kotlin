package com.kc.coffer.widget;

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import androidx.core.view.NestedScrollingParent2
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

/**
 * author      : coffer
 * date        : 10/28/21
 * description : 实现RecycleView 粘性拖拽第二版
 */
open class NestScrollerContainer @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr), NestedScrollingParent2 {

    companion object {
        const val TAG = "haha_tag"

        /**
         * 最大超出的滑动距离
         */
        const val MAX_HEIGHT = 300
    }

    private val headerView: View
    private val footerView: View
    private var animator: ReboundAnimator? = null

    /**
     * 针对冗余fling期间，保证回弹动画只执行一次
     */
    private var isFirstRunAnim = false

    override fun onFinishInflate() {
        super.onFinishInflate()
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, MAX_HEIGHT)
        // 将头、尾视图插入
        addView(headerView, 0, layoutParams)
        addView(footerView, childCount, layoutParams)
        // 上移MAX_HEIGHT，隐藏头视图
        scrollBy(0, MAX_HEIGHT)
    }

    init {
        orientation = VERTICAL
        headerView = View(context)
        footerView = View(context)
    }


override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
    Log.i(TAG, "onStartNestedScroll  axes : $axes ,type : $type")
    // 返回true表示处理事件
    return target is RecyclerView
}

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        Log.i(TAG, "onNestedScrollAccepted  axes : $axes ,type : $type")
        if (animator == null) {
            // 初始化动画对象
            animator = ReboundAnimator()
        }
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        // 复位初始位置
        Log.i(TAG, "onStopNestedScroll type : $type")
        // 服务初始位置
        isFirstRunAnim = false
        if (scrollY != MAX_HEIGHT) {
            // 优化代码执行的效率
            animator?.startOfFloat(target, scaleY)
        }
    }

    override fun onNestedScroll(
        target: View, dxConsumed: Int, dyConsumed: Int,
        dxUnconsumed: Int, dyUnconsumed: Int, type: Int
    ) {
        // 这个方法仅在目标View可以滚动时调用。
        Log.i(
            NestScrollerContainer.TAG, "onNestedScroll type : " + type + " ，dxConsumed ：" + dxConsumed + "，dyConsumed ："
                    + dyConsumed + "，dxUnconsumed：" + dxUnconsumed + "，dyUnconsumed：" + dyUnconsumed
        )
        parent.requestDisallowInterceptTouchEvent(true)
        // type == TYPE_NON_TOUCH 表示Filing，这个是NestedScrollingParent2对NestedScrollingParent的
        // 扩展优化，让RecycleView 在Filing 下更加顺滑
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            //非手指触发的滑动，即Filing , 解决冗余fling问题
            if ((floor(scrollY.toDouble()) == 0.0 ||
                        ceil(scrollY.toDouble()) == (2 * NestScrollerContainer.MAX_HEIGHT).toDouble()) && !isFirstRunAnim
            ) {
                var startY = 0
                if (dyUnconsumed > 0) {
                    // 表示向上Filing，将尾视图滑动出来了
                    startY = 2 * NestScrollerContainer.MAX_HEIGHT
                }
                animator!!.startOfFloat(target, startY.toFloat())
                isFirstRunAnim = true
            }
            if (isFirstRunAnim) return
            // dy>0向下fling dy<0向上fling
            val showTop = dyUnconsumed < 0 && !target.canScrollVertically(-1)
            val showBottom = dyUnconsumed > 0 && !target.canScrollVertically(1)
            if (showTop || showBottom) {
                if (animator!!.isStarted) {
                    animator!!.pause()
                }
                scrollBy(0, damping(dyUnconsumed))
                if (animator!!.isPaused) {
                    //手动cancel 避免内存泄漏
                    animator!!.cancel()
                }
            }
            //调整错位
            adjust(dyUnconsumed, target)
        }
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        Log.i(NestScrollerContainer.TAG, "onNestedPreScroll type : $type")
        // 如果在自定义ViewGroup之上还有父View交给我来处理
        parent.requestDisallowInterceptTouchEvent(true)
        if (type == ViewCompat.TYPE_TOUCH) {
            Log.i(
                NestScrollerContainer.TAG,
                "onNestedPreScroll dy : $dy , getScrollY : $scrollY"
            )
            //手指触发的滑动 dy < 0向下scroll dy > 0向上scroll
            // canScrollVertically ：检测目标视图（RecycleView）是否可以在特定方向垂直滚动，负数表示向上滚
            // 当前的滚动如果是向下，并且子View已经不能向上滚动时（表示滑到顶了），表示要显示顶部，否则向下
            // 这里需要注意下，在初始化时，为了隐藏头视图，整个View已经向上滚动了MAX_HEIGHT（200），因此此时
            // 向上滚动时，getScrollY == MAX_HEIGHT（200)，在RecycleView自己的内容可以滚动时，getScrollY
            // 还是MAX_HEIGHT（200)，因为仅头视图在外面，当滚到底，即RecycleView滚上去显示尾视图时，
            // getScrollY == 头视图的高度 + 尾视图的高度
            val hiddenTop = dy > 0 && scrollY < NestScrollerContainer.MAX_HEIGHT && !target.canScrollVertically(-1)
            val showTop = dy < 0 && !target.canScrollVertically(-1)
            val hiddenBottom = dy < 0 && scrollY > NestScrollerContainer.MAX_HEIGHT && !target.canScrollVertically(1)
            val showBottom = dy > 0 && !target.canScrollVertically(1)
            animator?.let {
                if (hiddenTop || showTop || hiddenBottom || showBottom) {
                    if (it.isStarted) {
                        animator!!.pause()
                    }
                    scrollBy(0, damping(dy))
                    if (it.isPaused) {
                        //手动cancel 避免内存泄漏
                        it.cancel()
                    }
                    // 事件交给父View消耗，consumed[1]表示父View的y轴
                    consumed[1] = dy
                }
            }
            //调整错位
            adjust(dy, target)
        }
    }

    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        Log.i(TAG, "onNestedFling consumed : $consumed")
        return super.onNestedFling(target, velocityX, velocityY, consumed)
    }

    /**
     * 衰减可继续scroll或fling的距离
     */
    private fun damping(dy: Int): Int {
        //计算衰减系数,越大可继续scroll或fling的距离越短
        val i = (abs(MAX_HEIGHT - scrollY) * 0.01).toInt()
        return if (i < MAX_HEIGHT * 0.01) dy else dy / i
    }

    /**
     * 调整错位问题(强转精度损失造成的错位)
     */
    private fun adjust(condition1: Int, condition2: View) {
        if (condition1 > 0 && scrollY > MAX_HEIGHT && !condition2.canScrollVertically(-1)) {
            scrollTo(0, MAX_HEIGHT)
        }
        if (condition1 < 0 && scrollY < MAX_HEIGHT && !condition2.canScrollVertically(1)) {
            scrollTo(0, MAX_HEIGHT)
        }
    }

    /**
     * 限制滑动 移动y轴不能超出最大范围
     */
    override fun scrollTo(x: Int, y: Int) {
        var y = y
        if (y < 0) {
            y = 0
        } else if (y > MAX_HEIGHT * 2) {
            y = MAX_HEIGHT * 2
        }
        super.scrollTo(x, y)
    }

    /**
     * 回弹动画
     */
    private inner class ReboundAnimator : ValueAnimator() {
        private var target: View? = null
        private fun init() {
            this.interpolator = DecelerateInterpolator() //添加减速插值器
            this.duration = 260
            //添加值更新监听器
            addUpdateListener {
                val currValue = animatedValue as Float
                scrollTo(0, currValue.toInt())
                // 调整错位问题(强转精度损失造成的错位)
                if (scrollY > MAX_HEIGHT && !target!!.canScrollVertically(-1)) {
                    scrollTo(0, MAX_HEIGHT)
                }
                if (scrollY < MAX_HEIGHT && !target!!.canScrollVertically(1)) {
                    scrollTo(0, MAX_HEIGHT)
                }
            }
        }

        fun startOfFloat(target: View, startY: Float) {
            setFloatValues(startY, MAX_HEIGHT.toFloat())
            this.target = target
            start()
        }

        init {
            init()
        }
    }
}
