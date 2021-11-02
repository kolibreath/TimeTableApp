package com.kolibreath.timetableapp.schedule.weeklyschedule

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import com.kolibreath.timetableapp.R
import com.kolibreath.timetableapp.endTimes
import com.kolibreath.timetableapp.startMorning
import com.kolibreath.timetableapp.startTimes

// 在课程表的左侧 表示当前课程安排的起始结束时间以及课程编号
class CourseTimeLayout(
    private val contxt: Context,
    private val attributeSet: AttributeSet
): ScheduleLayout(contxt, attributeSet){

    init {
        val numOfCourses = endTimes.size
        orientation = VERTICAL
        initViews(numOfCourses, startTimes, endTimes)
    }


    /**
     * 初始化侧边的View并且将这些View添加到View树中
     * 定义在课程开始时间之外的方格，不绘制数字
     */
    private fun initViews(numOfClass: Int, startTimes: Array<String>, endTimes: Array<String>){
        val numOfMorningTimes = startMorning.size
        for((index, i) in (0 until numOfClass).withIndex()){
            val view = LayoutInflater.from(contxt)
                .inflate(R.layout.view_course_schedule, this, false)

            // 定义
            val tvStartTime = view.findViewById<TextView>(R.id.tv_start_time)
            val tvEndTime = view.findViewById<TextView>(R.id.tv_end_time)
            val tvOrder = view.findViewById<TextView>(R.id.tv_order)

            tvStartTime.text = startTimes[i]
            tvEndTime.text = endTimes[i]

            if(index < numOfMorningTimes) tvOrder.text = ""
            else tvOrder.text = "${i+1-numOfMorningTimes}"

            addView(view)
        }
    }

}