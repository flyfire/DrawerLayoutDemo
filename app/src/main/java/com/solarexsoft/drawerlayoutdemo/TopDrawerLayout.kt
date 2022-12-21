package com.solarexsoft.drawerlayoutdemo

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.customview.widget.ViewDragHelper
import kotlin.math.max
import kotlin.math.min


/*
 * Creadted by houruhou on 2022/12/21 14:54
 */
class TopDrawerLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    companion object {
        const val MIN_DRAWER_MARGIN = 300
        const val MIN_FLING_VELOCITY = 400
        const val TAG = "TopDrawerLayout"
    }

    private var minDrawerMargin = 0
    private lateinit var topMenuView: View
    private lateinit var contentView: View
    private lateinit var dragger: ViewDragHelper
    private var topMenuOnScreen = 0f

    init {
        val density = resources.displayMetrics.density
        val minVel = MIN_FLING_VELOCITY * density
        minDrawerMargin = (MIN_DRAWER_MARGIN * density + 0.5f).toInt()

        dragger = ViewDragHelper.create(this, 1.0f, object : ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                return child == topMenuView
            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                val newTop = max(-child.height, min(top, 0))
                return newTop
            }

            override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
                dragger.captureChildView(topMenuView, pointerId)
            }

            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                Log.d(
                    TAG,
                    "onViewReleased() called with: releasedChild = $releasedChild, xvel = $xvel, yvel = $yvel"
                )
                val childHeight = releasedChild.height
                val offset = (childHeight + releasedChild.top).toFloat() / childHeight
                Log.d(TAG, "onViewReleased: offset = $offset, releasedChild top = ${releasedChild.top}, releasedChild height = ${releasedChild.height}")
                val childTop = if (yvel > 0 || yvel == 0f && offset > 0.5f) 0 else -childHeight
                dragger.settleCapturedViewAt(releasedChild.left, childTop)
                invalidate()
            }

            override fun onViewPositionChanged(
                changedView: View,
                left: Int,
                top: Int,
                dx: Int,
                dy: Int
            ) {
                Log.d(
                    TAG,
                    "onViewPositionChanged() called with: changedView = $changedView, left = $left, top = $top, dx = $dx, dy = $dy"
                )
                val childHeight = changedView.height
                val offset = (childHeight + top).toFloat()
                topMenuOnScreen = offset
                changedView.visibility = if (offset == 0.0f) View.INVISIBLE else View.VISIBLE
                invalidate()
            }

            override fun getViewVerticalDragRange(child: View): Int {
                return if (topMenuView == child) child.height else 0
            }
        } )
        dragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP)
        dragger.minVelocity = minVel
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        setMeasuredDimension(widthSize, heightSize)

        topMenuView = getChildAt(1)
        val menuLp = topMenuView.layoutParams as MarginLayoutParams
        val drawerWidthSpec = getChildMeasureSpec(
            widthMeasureSpec,
            menuLp.leftMargin + menuLp.rightMargin,
            menuLp.width
        )
        val drawerHeightSpec = getChildMeasureSpec(
            heightMeasureSpec,
            minDrawerMargin + menuLp.topMargin + menuLp.bottomMargin,
            menuLp.height
        )
        topMenuView.measure(drawerWidthSpec, drawerHeightSpec)

        contentView = getChildAt(0)
        val contentLp = contentView.layoutParams as MarginLayoutParams
        val contentWidthSpec = MeasureSpec.makeMeasureSpec(
            widthSize - contentLp.leftMargin - contentLp.rightMargin,
            MeasureSpec.EXACTLY
        )
        val contentHeightSpec = MeasureSpec.makeMeasureSpec(
            heightSize - contentLp.topMargin - contentLp.bottomMargin,
            MeasureSpec.EXACTLY
        )
        contentView.measure(contentWidthSpec, contentHeightSpec)
    }
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val contentLp = contentView.layoutParams as MarginLayoutParams
        contentView.layout(
            contentLp.leftMargin,
            contentLp.topMargin,
            contentLp.leftMargin + contentView.measuredWidth,
            contentLp.topMargin + contentView.measuredHeight
        )
        val menuLp = topMenuView.layoutParams as MarginLayoutParams
        val menuHeight = topMenuView.measuredHeight
        val menuTop = -menuHeight + (menuHeight * topMenuOnScreen).toInt()
        topMenuView.layout(
            menuLp.leftMargin,
            menuTop,
            menuLp.leftMargin + topMenuView.measuredWidth,
            menuTop + menuHeight
        )
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val shouldInterceptTouchEvent = dragger.shouldInterceptTouchEvent(ev)
        return shouldInterceptTouchEvent
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        dragger.processTouchEvent(event)
        return true
    }

    override fun computeScroll() {
        if (dragger.continueSettling(true)) {
            invalidate()
        }
    }

    fun closeDrawer() {
        topMenuOnScreen = 0.0f
        dragger.smoothSlideViewTo(topMenuView, topMenuView.left, -topMenuView.height)
    }

    override fun generateDefaultLayoutParams(): MarginLayoutParams {
        return MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return MarginLayoutParams(p)
    }
}