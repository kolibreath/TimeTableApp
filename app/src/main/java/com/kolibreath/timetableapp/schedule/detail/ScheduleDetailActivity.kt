package com.kolibreath.timetableapp.schedule.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kolibreath.timetableapp.R
import com.kolibreath.timetableapp.WeeklyScheduleDetail
import com.kolibreath.timetableapp.schedule.WeeklyScheduleDetailAdapter

class ScheduleDetailActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_detail)


        // 构建测试用例
        val course = WeeklyScheduleDetail(
            detailType = 0,
            name = "摆烂",
            location = "N636",
            time = "周一 8:00~9:00 (第1-2节)",
            week = "1~18周",
            teacher = "郭京蕾"
        )

        val course2 = WeeklyScheduleDetail(
            detailType = 1,
            location = "N517",
            time = "周一 5月29日 9:00~9:40",
            type = "运动",
            week = "18周",
            description = "这是一个简单的日程"
        )

        val rvDetails = findViewById<RecyclerView>(R.id.rv_details)
        val layoutManager = LinearLayoutManager(this)
        val adapter = WeeklyScheduleDetailAdapter(this, arrayListOf(course, course2))
        rvDetails.layoutManager = layoutManager
        rvDetails.adapter = adapter
    }

}