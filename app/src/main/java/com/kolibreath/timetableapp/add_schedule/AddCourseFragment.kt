package com.kolibreath.timetableapp.add_schedule

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.kolibreath.timetableapp.R
import com.kolibreath.timetableapp.wheelView.date.DatePickerDialogFragment

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
        return view
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

        edtWeekNum = rootView.findViewById(R.id.edt_week_num)
        edtTime = rootView.findViewById<EditText>(R.id.edt_time).apply {
            focusable = EditText.NOT_FOCUSABLE
            isFocusableInTouchMode = false
//            setOnClickListener {
//                val dateTv = findViewById<TextView>(R.id.tv_date)
//                val datePicker: DatePicker = findViewById(R.id.datePicker)
//                val button: Button = findViewById(R.id.button)
//                button.setOnClickListener {
//                    val datePickerDialogFragment =
//                        DatePickerDialogFragment()
//                    val listener =
//                        DatePickerDialogFragment.OnDateChooseListener { year, month, day ->
//                            Toast.makeText(
//                                applicationContext,
//                                "$year-$month-$day",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    datePickerDialogFragment.setOnDateChooseListener(listener)
//                    datePickerDialogFragment.show(supportFragmentManager, "")
//                }
//                datePicker.setOnDateSelectedListener { year, month, day ->
//                    dateTv.text = "$year-$month-$day"
//                }
//            }
        }

    }
}