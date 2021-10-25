package com.kolibreath.timetableapp

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.Scroller

// 表示课程开始结束时间的CourseScheduleView
// 和星期所对应的WeekdayView共同的父类
open class ScheduleLayout(
    contxt: Context,
    attributeSet: AttributeSet
): LinearLayout(contxt, attributeSet) {

    val scroller: Scroller = Scroller(contxt)

    override fun computeScroll() {
        if(scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
            postInvalidate()
        }
    }
}