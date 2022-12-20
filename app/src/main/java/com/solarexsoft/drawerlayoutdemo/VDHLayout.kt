package com.solarexsoft.drawerlayoutdemo

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.customview.widget.ViewDragHelper


/*
 * Creadted by houruhou on 2022/12/20 17:03
 */
class VDHLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    lateinit var dragger: ViewDragHelper
    lateinit var dragView: View
    lateinit var autoBackView: View
    lateinit var edgeTrackView: View
    val autoBackOriginPos = Point()

    init {
        dragger = ViewDragHelper.create(this, 1.0f, object :ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                return child == dragView || child == autoBackView
            }

            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                return left
            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                return top
            }

            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                if (releasedChild == autoBackView) {
                    dragger.settleCapturedViewAt(autoBackOriginPos.x, autoBackOriginPos.y)
                    invalidate()
                }
            }

            override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
                dragger.captureChildView(edgeTrackView, pointerId)
            }

            override fun getViewHorizontalDragRange(child: View): Int {
                return measuredWidth - child.measuredWidth
            }

            override fun getViewVerticalDragRange(child: View): Int {
                return measuredHeight - child.measuredHeight
            }
        })
        dragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return dragger.shouldInterceptTouchEvent(ev)
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

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        autoBackOriginPos.x = autoBackView.left
        autoBackOriginPos.y = autoBackView.top
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        dragView = getChildAt(0)
        autoBackView = getChildAt(1)
        edgeTrackView = getChildAt(2)
    }
}