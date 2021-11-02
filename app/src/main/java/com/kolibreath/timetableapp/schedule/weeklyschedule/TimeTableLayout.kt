package com.kolibreath.timetableapp.schedule.weeklyschedule

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import android.widget.Scroller

/**
 * 包括了中间TableContent，左侧课程时间CourseTimeLayout和上边WeekLayout三个部分的视图
 */
class TimeTableLayout(
    private val contxt: Context,
    private val attributeSet: AttributeSet
): RelativeLayout(contxt, attributeSet) {

    private val scroller = Scroller(contxt)

    override fun computeScroll() {
        if(scroller.computeScrollOffset()){
            // TODO 这个地方是ScrollTo还是scrollBy?
            scrollBy(scroller.currX, scroller.currY)
            postInvalidate()
        }
    }
}