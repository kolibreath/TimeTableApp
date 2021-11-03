package com.kolibreath.timetableapp.schedule.weeklyschedule

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import com.kolibreath.timetableapp.R
import com.kolibreath.timetableapp.getCurrWeekNum
import com.kolibreath.timetableapp.getWholeWeek4CurWeekNum
import com.kolibreath.timetableapp.num2WeekdayEn

// 课程表顶部显示当前的时间的View
class WeekLayout(
    private val contxt: Context,
    private val attributeSet: AttributeSet
): ScheduleLayout(contxt, attributeSet) {

    init {
        orientation = HORIZONTAL
        initView()
    }

    // 初始化一周七天的View
    private fun initView(){
        val list = getWholeWeek4CurWeekNum(getCurrWeekNum())
        for(i in 0 until 7){
            val view = LayoutInflater.from(contxt).inflate(R.layout.view_week, this, false)

            val tvWeekday = view.findViewById(R.id.tv_weekday) as TextView
            val tvDate = view.findViewById(R.id.tv_date) as TextView

            tvWeekday.text = num2WeekdayEn(i)
            tvDate.text = list[i]

            addView(view)
        }
    }
}