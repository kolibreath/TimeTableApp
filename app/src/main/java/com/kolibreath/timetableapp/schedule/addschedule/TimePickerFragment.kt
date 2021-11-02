package com.kolibreath.timetableapp.schedule.addschedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.kolibreath.timetableapp.R
import com.kolibreath.timetableapp.actualNumOfCourses

// 选择 周数 和 具体的时间
// 选择的结果为 周二 1 - 2 节 或 周二 8：00 - 9：00 使用ViewPager2提供两种选择方式
/**
 * @param position position = 0 表示通过节数选择 position = 1 表示通过具体时间选择
 */
class TimePickerFragment (
    private val position: Int
    ) : Fragment(), NumberPicker.OnValueChangeListener{

    private lateinit var tvWeek: TextView
    private lateinit var npWeek: NumberPicker
    private lateinit var npCourseNum1: NumberPicker
    private lateinit var npCourseNum2: NumberPicker

    private lateinit var onResultChangeListener: OnResultChangeListener

    private val weekdays = arrayOf("周一", "周二", "周三", "周四", "周五", "周六", "周日")

    // 课程节数
    private val times =
        if(position == 0)
            Array(actualNumOfCourses){ (it + 1).toString()}
        else
            arrayOf(
                "6:00", "6:30", "7:00", "7:30",
                "8:00", "8:30", "9:00", "9:30", "10:00", "10:30",
                "11:00", "11:30", "12:00", "12:30", "13:00", "13:30",
                "14:00", "14:30", "15:00", "15:30", "16:00", "16:30",
                "17:00", "17:30", "18:00", "18:30", "19:00", "19:30",
                "20:00", "20:30", "21:00", "21:30", "22:00", "22:30",
                "23:00", "23:30"
            )

    private var weekday = "周一"
    private var courseNum1 = if(position == 0) "1" else "6:00"
    private var courseNum2 = if(position == 0) "2" else "6:30"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_time_picker, container, false)
        initView(rootView)
        return rootView
    }

    private fun initView(rootView: View) {
        tvWeek = rootView.findViewById(R.id.tv_select_result)

        tvWeek.text = if(position == 0) "周一 1-2 节" else "周一 6:00 - 6:30"

        npWeek = rootView.findViewById<NumberPicker>(R.id.np_week).apply {
            displayedValues = weekdays
            minValue = 0
            maxValue = weekdays.size - 1
            value = 0
            wrapSelectorWheel = false // 设置不可滚动
            setOnValueChangedListener(this@TimePickerFragment)
            descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS // 设置不可编辑
        }
        

        npCourseNum1 = rootView.findViewById<NumberPicker>(R.id.np_course_num_1).apply {
            displayedValues = this@TimePickerFragment.times
            minValue = 0
            maxValue = this@TimePickerFragment.times.size - 1
            value = 0
            wrapSelectorWheel = false // 设置不可滚动
            descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
            setOnValueChangedListener(this@TimePickerFragment)
        }

        npCourseNum2 = rootView.findViewById<NumberPicker>(R.id.np_course_num_2).apply {
            displayedValues = this@TimePickerFragment.times
            minValue = 0
            maxValue = this@TimePickerFragment.times.size - 1
            value = 1
            wrapSelectorWheel = false // 设置不可滚动
            descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
            setOnValueChangedListener(this@TimePickerFragment)
        }

    }

    override fun onValueChange(picker: NumberPicker, oldVal: Int, newVal: Int) {

        when(picker.id) {
            R.id.np_week -> {
                weekday = weekdays[newVal]
            }

            R.id.np_course_num_1 -> {
                courseNum1 = this.times[newVal]
                // todo course num can not be the same and courseNum1 must not greater than courseNum2
            }

            R.id.np_course_num_2  -> {
                courseNum2 = this.times[newVal]
            }
        }

        tvWeek.text = if(position == 0) "$weekday $courseNum1-${courseNum2}节" else "$weekday $courseNum1-${courseNum2}"
        onResultChangeListener.onResultChange(
            weekday = weekday,
            courseNum1 = courseNum1,
            courseNum2 = courseNum2
        )
    }

    internal fun setOnResultChangeListener(onResultChangeListener: OnResultChangeListener){
        this.onResultChangeListener = onResultChangeListener
    }

    interface OnResultChangeListener{
        fun onResultChange(weekday: String, courseNum1: String, courseNum2: String)
    }

    companion object {
        //todo why new instance?
        fun newInstance(position: Int) = TimePickerFragment(position)
    }
}