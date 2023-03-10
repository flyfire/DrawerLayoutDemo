package com.solarexsoft.drawerlayoutdemo

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.customview.widget.ViewDragHelper
import kotlin.math.max
import kotlin.math.min


/*
 * Creadted by houruhou on 2022/12/20 17:59
 */
class LeftDrawerLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    companion object {
        const val MIN_DRAWER_MARGIN = 64
        const val MIN_FLING_VELOCITY = 400
        const val TAG = "LeftDrawerLayout"
    }

    private var minDrawerMargin = 0
    private lateinit var leftMenuView: View
    private lateinit var contentView: View
    private lateinit var dragger: ViewDragHelper
    private var leftMenuOnScreen = 0f

    init {
        val density = resources.displayMetrics.density
        val minVel = MIN_FLING_VELOCITY * density
        minDrawerMargin = (MIN_DRAWER_MARGIN * density + 0.5f).toInt()

        dragger = ViewDragHelper.create(this, 1.0f, object : ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                return child == leftMenuView
            }

            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                val newLeft = max(-child.width, min(left, 0))
                return newLeft
            }

            override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
                dragger.captureChildView(leftMenuView, pointerId)
            }

            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                val childWidth = releasedChild.width
                val offset = (childWidth + releasedChild.left).toFloat() / childWidth
                val childLeft = if (xvel > 0 || xvel == 0f && offset > 0.5f) 0 else -childWidth
                dragger.settleCapturedViewAt(childLeft, releasedChild.top)
                invalidate()
            }

            override fun onViewPositionChanged(
                changedView: View,
                left: Int,
                top: Int,
                dx: Int,
                dy: Int
            ) {
                val childWidth = changedView.width
                val offset = (childWidth + left).toFloat()/childWidth
                leftMenuOnScreen = offset
                changedView.visibility = if (offset == 0.0f) View.INVISIBLE else View.VISIBLE
                invalidate()
            }

            override fun getViewHorizontalDragRange(child: View): Int {
                return if (leftMenuView == child) child.width else 0
            }
        })
        dragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT)
        dragger.minVelocity = minVel
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        setMeasuredDimension(widthSize, heightSize)

        leftMenuView = getChildAt(1)
        val lp = leftMenuView.layoutParams as MarginLayoutParams
        val drawerWidthSpec = getChildMeasureSpec(
            widthMeasureSpec,
            minDrawerMargin + lp.leftMargin + lp.rightMargin,
            lp.width
        )
        val drawerHeightSpec = getChildMeasureSpec(
            heightMeasureSpec,
            lp.topMargin + lp.bottomMargin,
            lp.height
        )
        leftMenuView.measure(drawerWidthSpec, drawerHeightSpec)

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
        val menuLp = leftMenuView.layoutParams as MarginLayoutParams
        val menuWidth = leftMenuView.measuredWidth
        val childLeft = -menuWidth + (menuWidth * leftMenuOnScreen).toInt()
        leftMenuView.layout(
            childLeft,
            menuLp.topMargin,
            childLeft + menuWidth,
            menuLp.topMargin + leftMenuView.measuredHeight
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
        leftMenuOnScreen = 0.0f
        dragger.smoothSlideViewTo(leftMenuView, -leftMenuView.width, leftMenuView.top)
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