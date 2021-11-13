package com.kolibreath.timetableapp.schedule.addschedule

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.kolibreath.timetableapp.R
import com.kolibreath.timetableapp.ScheduleTime
import com.kolibreath.timetableapp.base.ui.RippleLayout
import com.kolibreath.timetableapp.num2WeekdayCn

class AddScheduleFragment: Fragment() {

    private lateinit var edtDescription: EditText
    private lateinit var edtType: EditText // todo popup type selection dialog here
    private lateinit var edtStartTime: EditText
    private lateinit var edtEndTime: EditText
    private lateinit var edtRepeat: EditText // todo set repeat mode here

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_add_schedule, container)
        initView(rootView)
        return rootView
    }

    private fun initView(rootView: View) {
        edtDescription = rootView.findViewById<EditText>(R.id.edt_add_schedule_description)
        edtType = rootView.findViewById<EditText>(R.id.edt_type)
        edtStartTime = rootView.findViewById(R.id.edt_start_time)
        edtEndTime = rootView.findViewById(R.id.edt_end_time)
        edtRepeat = rootView.findViewById(R.id.edt_repeat)

        rootView.findViewById<RippleLayout>(R.id.layout_start_time).apply {
            setOnClickListener {
                val dialog = ScheduleTimePickerDialogFragment(
                    titleName = "开始时间",
                    resId = R.layout.dialog_fragment_schedule_time_picker
                )
                dialog.show(
                    this@AddScheduleFragment.requireActivity().supportFragmentManager,
                    "ScheduleTimePickerDialogFragment_start_time"
                )
                dialog.setOnScheduleTimeSelectedListener(object :
                    ScheduleTimePickerDialogFragment.OnScheduleTimeSelectedListener {
                    override fun onScheduleTimeSelected(time: ScheduleTime) {
                         edtStartTime.text =
                            SpannableStringBuilder(
                                "${time.year}年${time.month}月${time.date}日 ${num2WeekdayCn(time.dayOfWeek-1)} ${time.hour} : ${time.minute}"
                            )
                    }
                })
            }
        }

        rootView.findViewById<RippleLayout>(R.id.layout_end_time).apply {
            setOnClickListener {
                val dialog = ScheduleTimePickerDialogFragment(
                    titleName = "结束时间",
                    resId = R.layout.dialog_fragment_schedule_time_picker
                )
                dialog.show(
                    this@AddScheduleFragment.requireActivity().supportFragmentManager,
                    "ScheduleTimePickerDialogFragment_end_time"
                )
                dialog.setOnScheduleTimeSelectedListener(object :
                    ScheduleTimePickerDialogFragment.OnScheduleTimeSelectedListener {
                    override fun onScheduleTimeSelected(time: ScheduleTime) {
                        edtEndTime.text =
                            SpannableStringBuilder(
                                "${time.year}年${time.month}月${time.date}日 ${num2WeekdayCn(time.dayOfWeek-1)} ${time.hour} : ${time.minute}"
                            )
                    }
                })
            }
        }

    }
}