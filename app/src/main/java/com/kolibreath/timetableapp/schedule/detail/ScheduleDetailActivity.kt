package com.kolibreath.timetableapp.schedule.detail

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kolibreath.timetableapp.R
import com.kolibreath.timetableapp.WEEKLY_SCHEDULE_DETAILS
import com.kolibreath.timetableapp.WeeklyScheduleDetail
import com.kolibreath.timetableapp.base.LiveDataBus
import com.kolibreath.timetableapp.base.ui.ToolbarActivity
import com.kolibreath.timetableapp.schedule.WeeklyScheduleDetailAdapter

class ScheduleDetailActivity: ToolbarActivity(
    R.layout.activity_schedule_detail,
    R.string.schedule_detail
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val details = LiveDataBus.get<ArrayList<WeeklyScheduleDetail>>(
            WEEKLY_SCHEDULE_DETAILS
        ).value!!

        val rvDetails = findViewById<RecyclerView>(R.id.rv_details)
        val layoutManager = LinearLayoutManager(this)
        val adapter = WeeklyScheduleDetailAdapter(this, details)
        rvDetails.layoutManager = layoutManager
        rvDetails.adapter = adapter
    }

}