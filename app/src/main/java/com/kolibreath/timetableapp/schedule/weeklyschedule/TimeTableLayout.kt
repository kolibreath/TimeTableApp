package com.kolibreath.timetableapp.schedule.weeklyschedule

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.RelativeLayout
import kotlin.math.abs

/**
 * 包括了中间TableContent，左侧课程时间CourseTimeLayout和上边WeekLayout三个部分的视图
 * 需要在TimeTableLayout中处理和ViewPager2的滑动冲突
 */
class TimeTableLayout(
    private val contxt: Context,
    private val attributeSet: AttributeSet
): RelativeLayout(contxt, attributeSet) {

    /**
     * 1.判断触摸事件是否是滑动以及滑动的方向
     * 2.判断子View是否能向该方向滑动
     * 3.子View可滑动时，调用 requestDisallowInterceptTouchEvent(true) 禁止拦截事件
     */
    private var lastX = 0f
    private var lastY = 0f
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = ev.x
                lastY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = ev.x - lastX
                val dy = ev.y - lastY
                var hasScrollView = false
                val canScrollDown = (getChildAt(0) as TableContent).scrollY > 0
                if (!canScrollDown && dy < 0) hasScrollView = true
                if (canScrollDown  && dy > 0) hasScrollView = true

                val r = abs(dy) / abs(dx)
                if (r > 0.4f && hasScrollView) { // 比例可调整
                    requestDisallowInterceptTouchEvent(true)
                }
            }
            MotionEvent.ACTION_UP -> {
                requestDisallowInterceptTouchEvent(false)
            }
            MotionEvent.ACTION_CANCEL -> {
                requestDisallowInterceptTouchEvent(false)
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}