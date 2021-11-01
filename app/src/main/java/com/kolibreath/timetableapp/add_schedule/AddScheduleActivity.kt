package com.kolibreath.timetableapp.add_schedule

import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import com.kolibreath.timetableapp.R
import com.kolibreath.timetableapp.base.ui.DatePickerDialogFragment


class AddScheduleActivity: FragmentActivity() {

    private val tabs = arrayOf("tab1", "tab2", "tab3")
    private val tabFragmentList = ArrayList<AddScheduleFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_schedule)




        val button = findViewById<Button>(R.id.test)
        button.setOnClickListener {
            // 选择周数的DialogFragment测试
//            val dialogFragment = WeekPickerDialogFragment(R.layout.dialog_fragment_week_picker)
//            val onWeeksSelectedListener = object: WeekPickerDialogFragment.OnWeeksSelectedListener{
//                override fun onWeeksSelected(weeks: ArrayList<Int>) {
//                   Log.d("AddScheduleActivity", weeks.toString())
//                }
//            }
//            dialogFragment.setOnWeeksSelectedListener(onWeeksSelectedListener)
//            dialogFragment.show(supportFragmentManager, "")

            // 选择具体时间的ViewPager+DialogFragment 测试
//            val timePickerDialogFragment = TimePickerDialogFragment(R.layout.dialog_fragment_time_picker)
//            timePickerDialogFragment.show(supportFragmentManager, "")

            // 具体日期选择的DatePickerDialogFragment测试
            val datePickerDialogFragment = DatePickerDialogFragment(R.layout.dialog_fragment_date_picker)
            datePickerDialogFragment.show(supportFragmentManager, "")
        }
    }
}