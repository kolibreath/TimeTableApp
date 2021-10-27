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
    private val INNER_TEXT_WIDTH = (contxt.getScreenWidth() - TITLE_VIEW_MARGIN * 2) / 7 - INNER_MARGIN * 2
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

        // 找到这个月的第一天
        val now = LocalDate.now()
        val numOfDays = now.lengthOfMonth()
        //todo review common practices of LocalDate
        val firstDay = LocalDate.of(now.year, month, 1)
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

        // 初始化空白TextView
        for (i in preMonthStrs.indices) {
            initTextView(preMonthStrs[i], i, Color.GRAY)
        }

        // 下一个TextView在数组中的index = lineIndex + i
        // 使用第一天加一初始化后面的天数
        for(i in 0 until numOfDays) {
            val dateText = firstDay.plusDays(i.toLong()).dayOfMonth.toString()
            val nextIndex = lineIndex + i
            initTextView(dateText, nextIndex, Color.BLACK)
        }

        // 初始化下个月TextView
        for(i in nextMonthStrs.indices) {
            val nextIndex = preMonthStrs.size + numOfDays + i
            initTextView(nextMonthStrs[i], nextIndex, Color.GRAY)
        }

    }

    /**
     * TextView添加上LayoutParams，空白的TextView没有文字
     * @param dateText TextView应该显示的文字 上个月的文字使用灰色表示，本月使用黑色表示
     * @param i 当前的TextView在array中的位置
     */
    private fun initTextView(dateText: String, i: Int, color: Int) {
        val textView = tvDates[i]
        (textView.parent as ViewGroup?)?.let {
            removeView(textView)
        }
        addView(textView, INNER_TEXT_WIDTH.toInt(), INNER_TEXT_HEIGHT.toInt())
        val params = textView.layoutParams as GridLayout.LayoutParams
        params.leftMargin = INNER_MARGIN
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