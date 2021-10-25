package com.kolibreath.timetableapp

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import android.widget.Scroller

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