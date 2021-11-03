package com.kolibreath.timetableapp.schedule.addschedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.kolibreath.timetableapp.R

class AddScheduleFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_add_schedule, container, false)
        return rootView
    }

    override fun onStart() {
        super.onStart()
//        val label = requireArguments().getString("label")
        val textView = requireView().findViewById<TextView>(R.id.tv_bg)
        textView.text = "测试"
    }

}