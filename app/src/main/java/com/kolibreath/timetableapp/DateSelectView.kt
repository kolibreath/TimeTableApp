package com.kolibreath.timetableapp

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate

/**
 * 显示具体日期对应到具体的星期上，内容间隔参数和WeekTitleView保持一致。
 * 日历View的设计参考小米日历，会显示本月日历（黑色字体）灰色日期（下月字体）
 * @see WeekTitleView
 */
@RequiresApi(Build.VERSION_CODES.O)
class DateSelectView(
    private val contxt: Context,
    private val attributeSet: AttributeSet? = null
    ) : GridLayout(contxt)  {

    private val TITLE_VIEW_MARGIN = contxt.dp2px(12)
    // WeekSelectView中间的每一个View的边距
    private val INNER_MARGIN = contxt.dp2px(5).toInt()
    private val INNER_TEXT_WIDTH = (contxt.getScreenWidth() - (TITLE_VIEW_MARGIN + INNER_MARGIN) * 2) / 7 - 2 * INNER_MARGIN
    private val INNER_TEXT_HEIGHT = INNER_TEXT_WIDTH

    /**
     * 点击上个月的日期会滑动到上个月的日历，点击本月的日期会调出相应的点击事件
     * 一个月最多会有六行，也就是最多42个TextView
     */
    private val maxNumOfDates = 6*7
    private val tvDates = Array(maxNumOfDates){ TextView(contxt) }

    init {
        rowCount = 6
        columnCount = 7
    }

    /**
     * 根据指定的月份加载视图
     * @param month 表示月份的数字 1 表示1月
     */
    fun loadView(month: Int) {

        //todo review common practices of LocalDate
        val firstDay = LocalDate.of(LocalDate.now().year, month, 1)
        // 找到这个月的第一天
        val numOfDays = firstDay.lengthOfMonth()
//        val firstDay = now.with(TemporalAdjusters.firstDayOfMonth())
        val lineIndex = weekday2index(firstDay)

        // 生成上月末尾几天的日期
        val preMonthStrs = Array(lineIndex){
            firstDay.plusDays((- lineIndex + it).toLong()).dayOfMonth.toString()
        }
        // 生成下个月初几天的日期
        val nextMonthStrs = Array(maxNumOfDates - numOfDays - preMonthStrs.size) {
            val nextMonth = firstDay.plusMonths((1).toLong())
            nextMonth.plusDays(it.toLong()).dayOfMonth.toString()
        }

        val thisMonthStrs = Array(numOfDays){ "${(it + 1).toString()}"}
        // 找出 上个月 + 这个月 + 下个月 中在 【周日】位置的日期，这些日期需要加一个leftMargin
        val dates = preMonthStrs.toList() + thisMonthStrs + nextMonthStrs.toList()
        val sundays = dates.slice(dates.indices step 7) as ArrayList

        // sundays 表示所有列为周日的日期，将这些日期分成上月 本月 下个月三个部分
        val preSundays = ArrayList<String>()
        val thisSundays = ArrayList<String>()
        val nextSundays = ArrayList<String>()
        // 上个月日期中最多有一天会成为周日
        if(sundays[0] != "1") {
            preSundays.add(sundays[0])
            sundays.removeFirst()
        }
        // 本月区间内的日期都成为周日
        // 不可能出现本月和下个月中相同日期的某天都在周日的情况
        val temp = sundays.filter { it.toInt() in 1..numOfDays }.apply {
            if(this.last().toInt() <= 7) (this as ArrayList).removeLast()
            else this as ArrayList
        }
        // todo 直接使用removeAll
        temp.forEach { sundays.remove(it) }
//        sundays.removeAll(temp)
        thisSundays.addAll(temp)

        // 剩余的归类到下个月
        nextSundays.addAll(sundays)

        // 初始化空白TextView
        // 最多只会有一个需要移动的
        for (i in preMonthStrs.indices) {
            initTextView(preMonthStrs[i], i, Color.GRAY, preSundays.contains(preMonthStrs[i]))
        }

        // 下一个TextView在数组中的index = lineIndex + i
        // 使用第一天加一初始化后面的天数
        for(i in 0 until numOfDays) {
            val dateText = thisMonthStrs[i]
            val nextIndex = lineIndex + i
            initTextView(dateText, nextIndex, Color.BLACK, thisSundays.contains(dateText))
        }

        // 初始化下个月TextView
        for(i in nextMonthStrs.indices) {
            val nextIndex = preMonthStrs.size + numOfDays + i
            initTextView(nextMonthStrs[i], nextIndex, Color.GRAY, nextSundays.contains(nextMonthStrs[i]))
        }

    }

    /**
     * TextView添加上LayoutParams，空白的TextView没有文字
     * @param dateText TextView应该显示的文字 上个月的文字使用灰色表示，本月使用黑色表示
     * @param i 当前的TextView在array中的位置
     */
    private fun initTextView(dateText: String, i: Int, color: Int, needMargin: Boolean = false) {
        val textView = tvDates[i]
        // 规避ViewPager因为缓存问题造成的错误
        (textView.parent as ViewGroup?)?.let {
            removeView(textView)
        }
        addView(textView, INNER_TEXT_WIDTH.toInt(), INNER_TEXT_HEIGHT.toInt())
        val params = textView.layoutParams as GridLayout.LayoutParams

        if(needMargin) params.leftMargin = (INNER_MARGIN + TITLE_VIEW_MARGIN).toInt()
        else params.leftMargin = INNER_MARGIN
        params.rightMargin = INNER_MARGIN
        params.bottomMargin = INNER_MARGIN
        params.topMargin = INNER_MARGIN

        //todo define all color in color.xml
        textView.setTextColor(color)
        textView.gravity = Gravity.CENTER
        textView.text = dateText
    }

    private fun weekday2index(date: LocalDate): Int {
        return when(date.dayOfWeek) {
            DayOfWeek.SUNDAY -> 0
            DayOfWeek.MONDAY -> 1
            DayOfWeek.TUESDAY -> 2
            DayOfWeek.WEDNESDAY -> 3
            DayOfWeek.THURSDAY -> 4
            DayOfWeek.FRIDAY -> 5
            DayOfWeek.SATURDAY -> 6
        }
    }
}