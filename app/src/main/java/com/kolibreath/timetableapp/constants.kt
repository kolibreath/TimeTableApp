package com.kolibreath.timetableapp

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 官方通知的开学日期可能并不是周一，我们需要将这个日期转化成周一来计算
 * 比如2021/09/14是周二 但是我们需要找到这一周的周一
 * @see officialSchoolTime 官方公布的开学日期
 * @see schoolStartTime 实际上在课表中进行推导的【开学日期】，一定是周一
 */
val officialSchoolTime = "2021/09/14"
@RequiresApi(Build.VERSION_CODES.O)
// 找到当前日期最近的星期一
val schoolStartTime =
    run {
        val tempList = decomposeTime(officialSchoolTime)
        // 找到当前日期最近的星期一
        var localDate = LocalDate.of(tempList[0], tempList[1], tempList[2])
        localDate =  localDate.with {
            // 当前日期
            // 正常情况下，每次增加一天
            val dayToMinus = when (java.time.DayOfWeek.of(it.get(java.time.temporal.ChronoField.DAY_OF_WEEK))) {
                java.time.DayOfWeek.MONDAY -> 0
                java.time.DayOfWeek.TUESDAY -> -1
                java.time.DayOfWeek.WEDNESDAY -> -2
                java.time.DayOfWeek.THURSDAY -> -3
                java.time.DayOfWeek.FRIDAY -> -4
                java.time.DayOfWeek.SATURDAY -> -5
                java.time.DayOfWeek.SUNDAY -> -6
                else -> 1
            }

            it.plus(dayToMinus.toLong(), java.time.temporal.ChronoUnit.DAYS);
        }
        localDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
    }

/**
 * 因为匣子2中的课程界面现在增强称为【周视图】界面，需要添加自己定义的日程
 * 可能存在一些课程早于早上八点开课的时间，这部分的课程会被绘制到TableContent的上面。
 * 假设定义的最早的时间是早上6：00，如果有时间早于6：00，也会从6：00开始的方格中绘制。
 * @see TableContent.time2courseNum
 */
// 定义早上课程开始之前的时间段
val startMorning = arrayOf("06:00", "07:00")
val endMorning = arrayOf("07:00", "08:00")

// 定义晚上的时间段
val startNight = arrayOf("")
val endNight = arrayOf("")

// 课程开始和结束的时间
val startTimes = startMorning + arrayOf(
    "08:00", "09:00", "10:10", "11:10",
    "13:30", "14:30", "15:40", "16:40",
    "18:30", "19:30", "20:30")

val  endTimes = endMorning + arrayOf(
    "08:50", "09:50", "11:00", "12:00",
    "14:20", "15:20", "16:30", "17:30",
    "19:20", "20:20", "21:20")

// 每一天的实际课程数量
val actualNumOfCourses = startTimes.size - startMorning.size - startNight.size

// 一节课的时间
val courseInterval = 50f