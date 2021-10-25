package com.kolibreath.timetableapp

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.RelativeLayout
import android.widget.Scroller

class TimeTable(
    private val contxt: Context,
    private val attributeSet: AttributeSet
): RelativeLayout(contxt, attributeSet) {

    // 表示当前View的长宽信息
    private val WEEK_DAY_WIDTH = contxt.dp2px(65)
    private val COURSE_TIME_HEIGHT = contxt.dp2px(57)
    private val COURSE_TIME_WIDTH = contxt.dp2px(62)
    private val LITTLE_VIEW_WIDTH = contxt.dp2px(49)
    private val LITTLE_VIEW_HEIGHT = contxt.dp2px(41)

    private var courseTimeLayout: CourseTimeLayout
    private var weekLayout: WeekLayout
    private var timeTableContent: TableContent
    private var timeTableLayout: TimeTableLayout

    private val scroller = Scroller(contxt)

    init {
        val rootView = LayoutInflater.from(contxt).inflate(R.layout.view_timetable, this)
        courseTimeLayout = rootView.findViewById(R.id.course_time_layout) as CourseTimeLayout
        weekLayout = rootView.findViewById(R.id.week_layout) as WeekLayout
        timeTableContent = rootView.findViewById(R.id.timetable_content)
        timeTableLayout = rootView.findViewById(R.id.layout_table)
    }

    // 计算手指的位移欧氏距离距离
    private fun euclideanDistance(x1: Float, x2: Float, y1: Float, y2: Float): Float =
        (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)


    // 点击事件的分发交给RelativeLayout处理
    // 只需要考虑触摸事件
    private var startX = 0f // 触摸事件开始的点击位置
    private var startY = 0f
    private var lastX = 0f
    private var lastY = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val outerHeight = (COURSE_TIME_HEIGHT * startMorning.size).toInt()
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y

                lastX = event.x
                lastY = event.y
            }

            MotionEvent.ACTION_MOVE -> {
                val scrolledX = timeTableContent.scrollX
                val scrolledY = timeTableContent.scrollY

                var distanceX = 0
                var distanceY = 0

                // 思路，水平和垂直滑动都需要确定滑动的边界
                // 1. 水平滑动的左边界：
                // 2. 水平滑动的右边界：
                // 当向左滑动的长度大于宽度减去屏幕宽度时
                val diffX = scrolledX + lastX - event.x
                val constraintWidth =  WEEK_DAY_WIDTH * 7 + LITTLE_VIEW_WIDTH - contxt.getScreenWidth()
                distanceX = when {
                    diffX < 0 -> 0
                    diffX > constraintWidth ->
                        (WEEK_DAY_WIDTH * 7 + LITTLE_VIEW_WIDTH - contxt.getScreenWidth() - scrolledX).toInt()
                    else -> (lastX - event.x).toInt()

                }
                val diffY = scrolledY + lastY - event.y
                val constraintHeight = contxt.dp2px(50)
                distanceY = when {
                    diffY < 0 -> 0
                    diffY >= 0 && diffY <= outerHeight  -> (lastY - event.y).toInt()
                    diffY > outerHeight -> 0
                    else -> 0
                }

                weekLayout.scrollBy(distanceX, 0)
                courseTimeLayout.scrollBy(0, distanceY)
                timeTableContent.scrollBy(distanceX, distanceY)

                lastX = event.x
                lastY = event.y
            }

            MotionEvent.ACTION_UP   -> {
                // 如果是有下滑Refresh再进行处理
            }
        }

        return true
    }

}