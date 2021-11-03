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

        // 从intent中解析数据
        val bundle = intent.extras!!.getBundle("detail_bundle")
        val details = bundle!!.getSerializable("details") as ArrayList<WeeklyScheduleDetail>

        val rvDetails = findViewById<RecyclerView>(R.id.rv_details)
        val layoutManager = LinearLayoutManager(this)
        val adapter = WeeklyScheduleDetailAdapter(this, details)
        rvDetails.layoutManager = layoutManager
        rvDetails.adapter = adapter
    }

}