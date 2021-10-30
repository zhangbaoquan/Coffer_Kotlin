package com.kc.coffer.widget;

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * author      : coffer
 * date        : 10/29/21
 * description : 实现内部Item的滑动缩放
 */
open class StretchRecycleView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attributeSet, defStyleAttr) {

    companion object {
        const val TAG = "StretchRecycleView_tag"
    }

    private lateinit var linearLayoutManager: LinearLayoutManager
    private var offsetY = 0
    private var viewHeight = 0

    override fun setLayoutManager(layout: LayoutManager?) {
        super.setLayoutManager(layout)
        linearLayoutManager = layout as LinearLayoutManager
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        if (state == 0) {
            val position = linearLayoutManager.findFirstVisibleItemPosition()
            val view = linearLayoutManager.findViewByPosition(position)
            var top: Int
            var offset = 0
            view?.let {
                top = it.top
                if (viewHeight == 0) {
                    viewHeight = it.height
                }
                offset = when {
                    top == 0 -> return
                    -top < viewHeight / 2 -> top
                    else -> viewHeight + top
                }
            }
            smoothScrollBy(0, offset)
        }
    }

    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        offsetY += dy

        val first = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
        val last = linearLayoutManager.findLastCompletelyVisibleItemPosition()
        val firstView = linearLayoutManager.findViewByPosition(first)
        firstView?.let {
            if (viewHeight == 0) {
                viewHeight = it.height
            }
            var offseta = it.top
            var sx = 1f + offseta.toFloat() / viewHeight
            val view = linearLayoutManager.findViewByPosition(first + 1)
            view?.let { its ->
                if (offsetY == 0) {
                    its.scaleX = 2f
                }
            }
            it.scaleX = sx
            val lastView = linearLayoutManager.findViewByPosition(last)
            lastView?.let { itt ->
                // 这里我将默认的lambda参数名it换成itt，避免与最外层的lambda参数it产生歧义
                offseta = height - itt.bottom
                sx = 1f + offseta.toFloat() / viewHeight
                itt.scaleX = sx
            }
        }
    }
}
