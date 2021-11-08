package com.kolibreath.timetableapp.schedule.addschedule

import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.kolibreath.timetableapp.R
import com.kolibreath.timetableapp.ScheduleTime
import com.kolibreath.timetableapp.base.ui.dialogfragment.BaseBottomDialogFragment
import com.kolibreath.timetableapp.num2WeekdayCn
import com.shawnlin.numberpicker.NumberPicker
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class ScheduleTimePickerDialogFragment(
    private val titleName: String,
    private val resId: Int
): BaseBottomDialogFragment(resId), NumberPicker.OnValueChangeListener {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initChild(rootView: View) {
        initView(rootView)
    }

    private lateinit var npDate: NumberPicker
    private lateinit var npHour: NumberPicker
    private lateinit var npMinute: NumberPicker

    private lateinit var tvTitle: TextView
    private lateinit var tvCurTime: TextView

    private lateinit var tvConfirm: TextView
    private lateinit var tvCancel: TextView

    private var onScheduleTimeSelectedListener: OnScheduleTimeSelectedListener? = null

    @RequiresApi(Build.VERSION_CODES.O)
    private val curYear = LocalDate.now().year

    // 生成以当前天数为中心的前后100天 总共201天
    @RequiresApi(Build.VERSION_CODES.O)
    private val dates = getDaysBackAndDaysNext()
    private var curMonth: Int = 0     // 从1开始
    private var curDayOfWeek: Int = 0 // 从1开始
    private var curDateOfMonth:Int = 0
    private var curDayOfWeekCn: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    private val curDate = run {
        val now = LocalDate.now()
        val month = now.month.value
        val dayOfMonth = now.dayOfMonth
        this.curDayOfWeek = now.dayOfWeek.value-1
        this.curDayOfWeekCn = num2WeekdayCn(curDayOfWeek)
        this.curMonth = month
        this.curDayOfWeek = now.dayOfWeek.value
        this.curDateOfMonth = dayOfMonth

        "${month}月${dayOfMonth}日 $curDayOfWeekCn"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val curDateIndex = dates.indexOf(curDate)

    private val hours = Array(24)  { (it + 1).toString() }
    private val minutes = Array(60){ (it + 1).toString() }

    @RequiresApi(Build.VERSION_CODES.O)
    private var curHour = run {
        val now = LocalTime.now()
        (now.hour + 1).toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private var curMinute = run {
        val now = LocalTime.now()
        now.minute.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initView(rootView: View){
        tvCurTime = rootView.findViewById(R.id.tv_time)

        npDate = rootView.findViewById<NumberPicker>(R.id.np_date)
            .apply {
                setOnValueChangedListener(this@ScheduleTimePickerDialogFragment)
                minValue = 1
                maxValue = dates.size
                displayedValues = dates
                value = curDateIndex + 1
            }

        npHour = rootView.findViewById<NumberPicker>(R.id.np_hour)
            .apply {
                val curHourIndex = hours.indexOf(curHour)
                setOnValueChangedListener(this@ScheduleTimePickerDialogFragment)
                minValue = 1
                maxValue = hours.size
                displayedValues = hours
                value = curHourIndex + 1
            }

        npMinute = rootView.findViewById<NumberPicker>(R.id.np_minute)
            .apply {
                val curMinuteIndex = minutes.indexOf(curMinute)
                setOnValueChangedListener(this@ScheduleTimePickerDialogFragment)
                minValue = 1
                maxValue = minutes.size
                displayedValues = minutes
                value = curMinuteIndex + 1
            }

        tvTitle = rootView.findViewById(R.id.tv_title)
        tvTitle.text = titleName
        tvCurTime.text = "${curYear}年${curDate} ${curHour} : $curMinute"

        tvConfirm = rootView.findViewById(R.id.tv_confirm)
        tvCancel = rootView.findViewById(R.id.tv_cancel)

        tvCancel.setOnClickListener { dismiss() }
        tvConfirm.setOnClickListener {
            onScheduleTimeSelectedListener?.onScheduleTimeSelected(
                ScheduleTime(
                    year = curYear,
                    month = curMonth,
                    date = curDateOfMonth,
                    dayOfWeek = curDayOfWeek,
                    hour = curHour.toInt(),
                    minute = curMinute.toInt()
                )
            )
            dismiss()
        }
    }


    // fixme joda-time
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDaysBackAndDaysNext(): Array<String> {
        val result = Array(201){""}
        val now = LocalDate.now()
        var day = now.plus((-100).toLong(), ChronoUnit.DAYS)
        for(i in 0 until 201) {
            day = day.plusDays(1.toLong())
            val month = day.month.value
            val dayOfMonth = day.dayOfMonth
            val dayOfWeekCn = num2WeekdayCn(day.dayOfWeek.value-1)
            val string = "${month}月${dayOfMonth}日 $dayOfWeekCn"
            result[i] = string
        }
        return result
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
        val curYear = LocalDate.now().year

        when(picker?.id) {
            R.id.np_date -> {
                val string = dates[newVal - 1]
                Regex("""([0-9]+)[\u4E00-\u9FA5]([0-9]+)[\u4E00-\u9FA5] ([\u4E00-\u9FA5]{2})""").find(string)?.let {
                    val (month, date, week) = it.destructured
                    this@ScheduleTimePickerDialogFragment.curMonth = month.toInt()
                    this@ScheduleTimePickerDialogFragment.curDateOfMonth = date.toInt()
                    this@ScheduleTimePickerDialogFragment.curDayOfWeekCn = week
                    this@ScheduleTimePickerDialogFragment.curDayOfWeek = when(week) {
                        "周一" -> 1
                        "周二" -> 2
                        "周三" -> 3
                        "周四" -> 4
                        "周五" -> 5
                        "周六" -> 6
                        "周日" -> 7
                        else -> 0
                    }
                }
            }

            R.id.np_hour -> {
                curHour = hours[newVal - 1]
            }

            R.id.np_minute -> {
                curMinute = minutes[newVal - 1]
            }
        }

        tvCurTime.text = "${curYear}年${curMonth}月${curDateOfMonth}日 $curDayOfWeekCn $curHour : $curMinute"

    }

    internal fun setOnScheduleTimeSelectedListener(listener: OnScheduleTimeSelectedListener) {
        this.onScheduleTimeSelectedListener = listener
    }

    interface OnScheduleTimeSelectedListener {
        fun onScheduleTimeSelected(time: ScheduleTime)
    }
}