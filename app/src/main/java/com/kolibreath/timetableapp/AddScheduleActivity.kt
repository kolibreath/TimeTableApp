package com.kolibreath.timetableapp

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import android.widget.Toast

import android.widget.Button

import android.widget.TextView
import com.kolibreath.timetableapp.wheelView.DatePicker
import com.kolibreath.timetableapp.wheelView.DatePickerDialogFragment


class AddScheduleActivity: FragmentActivity() {

    private val tabs = arrayOf("tab1", "tab2", "tab3")
    private val tabFragmentList = ArrayList<AddScheduleFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_schedule)

//        val tablayout = findViewById<TabLayout>(R.id.tl_add_schedule)
//        val viewPager = findViewById<ViewPager2>(R.id.vp_add_schedule)
//
//        for(i in tabs.indices) {
//            tablayout.addTab(tablayout.newTab().setText(tabs[i]))
//            tabFragmentList.add(AddScheduleFragment.newInstance(tabs[i]))
//        }
//
//        viewPager.adapter = AddScheduleAdapter(tabFragmentList,this)
//        TabLayoutMediator(
//            tablayout,
//            viewPager
//        ) { tab, position ->  tab.text = tabs[position]
//        }.attach()
        val dateTv = findViewById<TextView>(R.id.tv_date)
        val datePicker: DatePicker = findViewById(R.id.datePicker)
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            val datePickerDialogFragment =
                DatePickerDialogFragment()
            val listener =
                DatePickerDialogFragment.OnDateChooseListener { year, month, day ->
                    Toast.makeText(
                        applicationContext,
                        "$year-$month-$day",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            datePickerDialogFragment.setOnDateChooseListener(listener)
            datePickerDialogFragment.show(supportFragmentManager, "")
        }
        datePicker.setOnDateSelectedListener { year, month, day ->
            dateTv.text = "$year-$month-$day"
        }
    }
}