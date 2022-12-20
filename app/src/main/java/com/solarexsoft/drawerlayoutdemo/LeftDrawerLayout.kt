package com.solarexsoft.drawerlayoutdemo

import android.content.Context
import android.util.AttributeSet
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
                changedView.visibility = if (offset == 0) View.INVISIBLE else View.VISIBLE
                invalidate()
            }

            override fun getViewHorizontalDragRange(child: View): Int {
                return if (leftMenuView == child) child.width else 0
            }
        })
        dragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT)
        dragger.minVelocity = minVel
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }

}