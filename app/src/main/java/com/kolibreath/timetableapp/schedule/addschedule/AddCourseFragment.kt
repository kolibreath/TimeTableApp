package com.kolibreath.timetableapp.schedule.addschedule

import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.kolibreath.timetableapp.R
import com.kolibreath.timetableapp.TAG_TIME_PICKER_DIALOG_FRAGMENT
import com.kolibreath.timetableapp.TAG_WEEK_PICKER_DIALOG_FRAGMENT

class AddCourseFragment: Fragment() {

    private lateinit var edtCourseName: EditText
    private lateinit var edtCourseType: EditText
    private lateinit var edtLocation: EditText
    private lateinit var edtTeacher: EditText
    private lateinit var edtPriority: EditText
    private lateinit var edtNote: EditText

    // 时间EditText不可以修改，点击之后直接弹出设置时间的Dialog
    private lateinit var edtWeekNum: EditText
    private lateinit var edtTime: EditText

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_add_course, container, false)
        initViews(rootView)
        return rootView
    }

    // 实例化这些View
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initViews(rootView: View){
        edtCourseName = rootView.findViewById(R.id.edt_add_course_name)
        edtCourseType = rootView.findViewById(R.id.edt_type)
        edtLocation = rootView.findViewById(R.id.edt_location)
        edtTeacher = rootView.findViewById(R.id.edt_teacher)
        edtPriority = rootView.findViewById(R.id.edt_priority)
        edtNote = rootView.findViewById(R.id.edt_note)

        edtWeekNum = rootView.findViewById<EditText>(R.id.edt_week_num).apply{
            inputType = InputType.TYPE_NULL
            setOnClickListener {
                val weekPickerDialogFragment =
                    WeekPickerDialogFragment(R.layout.dialog_fragment_week_picker)
                weekPickerDialogFragment.show(
                    this@AddCourseFragment.requireActivity().supportFragmentManager,
                    TAG_WEEK_PICKER_DIALOG_FRAGMENT
                )
                weekPickerDialogFragment.setOnWeeksSelectedListener(
                    object : WeekPickerDialogFragment.OnWeeksSelectedListener {
                        override fun onWeeksSelected(weeks: ArrayList<Int>) {
                            // 需要对周数进行处理
                            edtWeekNum.text = SpannableStringBuilder(weeks2str(weeks))
                        }
                    }
                )
            }
        }

        edtTime = rootView.findViewById<EditText>(R.id.edt_time).apply {
            inputType = InputType.TYPE_NULL
            setOnClickListener {
                val timePickerDialogFragment =
                    CourseTimePickerDialogFragment(R.layout.dialog_fragment_course_time_picker)
                timePickerDialogFragment.show(
                    this@AddCourseFragment.requireActivity().supportFragmentManager,
                    TAG_TIME_PICKER_DIALOG_FRAGMENT
                )
            }
        }


    }

    private fun weeks2str(weeks: ArrayList<Int>): String {
        // 判断是否是全部周数
        if (weeks.size == 21) return "全周"
        // 判断是否是由单周构成的
        val oddWeeksSize = 11
        val evenWeeksSize = 10
        if (oddWeeksSize == weeks.size) {
            var oddWeek = true
            for ((index, i) in (1..21 step 2).withIndex()) {
                if (weeks[index] != i) {
                    oddWeek = false
                    break
                }
            }
            if (oddWeek) return "单周"
        } else if (evenWeeksSize == weeks.size) {
            // 判断是否是双周
            var evenWeek = true
            for ((index, i) in (2..21 step 2).withIndex()) {
                if (weeks[index] != i) {
                    evenWeek = false
                    break
                }
            }
            if (evenWeek) return "双周"

        } else {
            // 如果都不是，使用逗号分割
            // 如 1 2 3 4 5 6 11 12 13 -> 第1-6周，11-13周
            val builder = StringBuilder()
            val result = ArrayList<Int>()
            result.add(weeks[0])
            for (i in 1 until weeks.size) {
                val week = weeks[i]
                if (week == result.last() + 1) {
                    result.add(week)
                } else {
                    builder.append("第${result.first()}-${result.last()}周，")
                    result.clear()
                    result.add(week)
                }
            }
            builder.append("第${result.first()}-${result.last()}周")
            return builder.toString()
        }
        return ""
    }
}