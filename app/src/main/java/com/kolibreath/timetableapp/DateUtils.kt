package com.kolibreath.timetableapp

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

//fixme remove the use of Date SimpleDateFormat and Calendar use joda-time instead
/**
 * 获取开学时间对应的年份 月份 和具体日期
 * @see schoolStartTime
 * @return 返回由以上三者组成的List {年份 月份 日期 }
 */
fun decomposeTime(schoolStartTime: String): ArrayList<Int> = schoolStartTime
    .split("/").map { it.toInt() }.toList() as ArrayList<Int>

// 获取当前的月份，并且以数字的形式展现，如（1月，2月...）
fun getCurrMonth():String  = Calendar.getInstance().get(Calendar.MONTH).toString()

// 获取当前的日期，如10/12 10/13..
fun getCurrDay():String{
    val date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()
    val month = getCurrMonth()
    return "$month/$date"
}

/**
 * 根据当前时间获取当前的星期，如Monday Tuesday
 * @return 当前的日期所对应的时间
 */
fun getCurWeekdayEn(): String {
    return when(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)){
        Calendar.MONDAY -> "Monday"
        Calendar.TUESDAY -> "Tuesday"
        Calendar.WEDNESDAY -> "Wednesday"
        Calendar.THURSDAY -> "Thursday"
        Calendar.FRIDAY -> "Friday"
        Calendar.SATURDAY -> "Saturday"
        Calendar.SUNDAY -> "Sunday"
        else -> {""}
    }
}

fun getCurWeekdayCn(): String {
    return when(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)){
        Calendar.MONDAY -> "周一"
        Calendar.TUESDAY -> "周二"
        Calendar.WEDNESDAY -> "周三"
        Calendar.THURSDAY -> "周四"
        Calendar.FRIDAY -> "周五"
        Calendar.SATURDAY -> "周六"
        Calendar.SUNDAY -> "周日"
        else -> {""}
    }
}

/**
 * 根据数字获取当前的星期
 * @param number 从0开始
 */
fun num2WeekdayEn(number: Int): String {
    return when(number){
        0 -> "Monday"
        1 -> "Tuesday"
        2 -> "Wednesday"
        3 -> "Thursday"
        4 -> "Friday"
        5 -> "Saturday"
        6 -> "Sunday"
        else -> {""}
    }
}

fun num2WeekdayCn(number: Int): String {
    return when(number){
        0 -> "周一"
        1 -> "周二"
        2 -> "周三"
        3 -> "周四"
        4 -> "周五"
        5 -> "周六"
        6 -> "周日"
        else -> {""}
    }
}


/**
 * 格局月份数字转化成对应的中文
 * @param number 从1开始
 */
fun num2MonthInCn(number: Int): String {
    return when(number) {
        1 -> "一月"
        2 -> "二月"
        3 -> "三月"
        4 -> "四月"
        5 -> "五月"
        6 -> "六月"
        7 -> "七月"
        8 -> "八月"
        9 -> "九月"
        10 -> "十月"
        11 -> "十一月"
        12 -> "十二月"
        else -> ""
    }
}

/**
 * 获取当前周数开始的周一到周日转化的字符串List，如
 * 第一周 9/14 9/15 9/16 9/17 9/18 9/19 9/20
 * @param weekNum 当前的周数 第一周为 1
 */
@RequiresApi(Build.VERSION_CODES.O)
fun getWholeWeek4CurWeekNum(weekNum: Int): ArrayList<String> {
    val tempList = decomposeTime(schoolStartTime)
    val arrayList = ArrayList<String>()
    var localDate = LocalDate.of(tempList[0], tempList[1], tempList[2])
    localDate = localDate.plusWeeks((weekNum - 1).toLong())
    localDate = localDate.plusDays((-1).toLong())
    for (i in 0 until 7) {
        localDate = localDate.plusDays(1)
        arrayList.add(localDate.format(DateTimeFormatter.ofPattern("MM/dd")))
    }
    return arrayList
}

/**
 * 根据开学日期计算当前的周数
 * @return 返回当前周数的值 第一周是1
 */
fun getCurrWeekNum(): Int{
    val date = Date()
    val schoolTimeStamp =  SimpleDateFormat("yyyy/MM/dd").parse(schoolStartTime).time
    val dateStamp = date.time

    // 一周总共的毫秒数
    val weekMills = 7 * 24 * 60 * 60 * 1000
    return ((dateStamp - schoolTimeStamp) / weekMills).toInt()
}

/**
 * 将当前的课程转换成long类型时间戳
 */
fun courseTime2Stamp(courseTime: CourseTime): Array<Long>{
    return if(courseTime.courseTime != null) {
        val tempList = courseTime.courseTime!!.split(",").map { it.toInt() }
        val before = startMorning.size
        val startTime = startTimes[before + tempList[0]-1]
        val endTime = endTimes[before + tempList[tempList.size-1]-1]

        val startTimeString = "$schoolStartTime $startTime"
        val endTimeString = "$schoolStartTime $endTime"

        val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm")
        val start = formatter.parse(startTimeString).time
        val end = formatter.parse(endTimeString).time
        arrayOf(start, end)
    }else{
        arrayOf(courseTime.customStartTime!!.time, courseTime.customEndTime!!.time)
    }
}

fun getHourFromDate(date: Date): Int {
    val calendar = Calendar.getInstance().apply { time = date }
    return calendar.get(Calendar.HOUR_OF_DAY)
}

fun getMinuteFromDate(date: Date): Int {
    val calendar = Calendar.getInstance().apply { time = date }
    return calendar.get(Calendar.MINUTE)
}

// String类型的日期变成Date 2021/9/25 9:00
fun string2Date(string: String): Date {
    return SimpleDateFormat("yyyy/MM/dd HH:mm").parse(string)
}


