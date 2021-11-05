package com.kolibreath.timetableapp

import java.io.Serializable
import java.util.*

// 开学时间 2021/9/14

// 课程
data class Course(
    val id: String,
    val name: String = "", // 课程名称
    val timeIds: String = "", // 课程时间段 使用逗号进行分割的字符串
    val courseTableId: String = "", // 课表 id
    val colorId: Int = 0,
    val dbId:Int? = 0
)

// todo equals 需要搬过去
// 课程时间段
data class CourseTime(
    val id: String,
    val place: String = "",
    val teacher: String = "",
    val dayOfWeek: Int, // 0 表示周一 7 表示周日
    var courseTime: String? = "", // 比如：1,2，表示第一节到第二节（和 customStartTime 二选一）
    val customStartTime: Date? = null, // 自定义开始和结束时间
    val customEndTime: Date? = null, // 自定义结束时间
    val weeks:String = "", // 1,2,3,4,5 那几周开始上课
    val courseId: String = "",
    val dbID: Int? = 0
){

    // data class 在Kotlin中重写的equals会比较在主构造函数中定义的每个参数的地址
    // https://stackoverflow.com/questions/37524422/equals-method-for-data-class-in-kotlin
    override fun equals(other: Any?): Boolean {
        if(other === this) return true
        if(other?.javaClass != this.javaClass) return false
        other as CourseTime
        if(other.id != this.id || other.place != this.place || other.teacher != this.teacher
            || other.dayOfWeek != this.dayOfWeek || other.courseTime != this.courseTime
            || other.customEndTime != this.customEndTime || other.customStartTime != this.customStartTime
            || other.weeks != this.weeks || other.courseId != this.courseId || other.dbID != this.dbID) return false
        return true
    }
}

// TODO 待办、日程领域模型
data class Schedule(
    val id: Int = 0,
    val title: String = "",
    val content: String = "",
    // 待办有两种类型：
    // 1. 时间点类型（只是提醒一下什么时候执行）
    // 2. 时间段类型，在什么样的时间段之间需要执行
    // isInterval = false 是时间点类型
    // isInterval = true  是时间段类型
    val isInterval: Boolean = false,
    // 如果没有设置开始时间，就默认为全天类型的事件
    // isAllDay = true 全天
    // isAllDay = false 不是全天
    val isAllDay: Boolean = false,
    val startTime: Date? = null,
    val endTime: Date? = null,
    // 重复规则:
    // 0 表示不重复
    // 1 表示基于日历规则重复
    // 2 表示基于当前学期周数重复
    val repeatMode: Int = 0,
    val cron: String? = null, // cron 表达式
    // 有两种类型：
    // 0 是日程（包括课程）
    // 1 是待办
    val kind: Int = 0,
    // 0 1 2 3 依次递增的优先级
    val priority: Int,
    val done: Boolean = false,
    val categoryId:Int = 0,
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
    val dbId: Int? = 0
)

// 课表元信息
class CourseTable(
    val id: String = "",
    val name: String = "",
    val semesterStartDate: Date? = null,
    val totalWeeks: Int = 0,
    val backgroundImg: String = "",
    val timeTableId: String = ""
)

// 根据待办/日程/课程生成的日程对象（仅客户端使用，服务端没这个东西）
// 这三个都使用这个部分
// 在日/周/月等视图中看到的日程展示都通过这个对象来查询
// 日程：根据重复等规则生成
// 课表：根据学期开始时间 + 课程列表生成
// 只读，仅供渲染用
class DerivedSchedule(
    val id: String = "",
    val name: String = "",
    val teacher: String = "",
    val place: String = "",
    val isInterval: Boolean = false,
    val startTime: Date? = null,
    val endTime: Date? = null,
    val kind: Int = 0,
    val priority: Int = 0,
    val done: Boolean = false,
    val categoryId: Int = 0,
    val cellColorId: Int = 0,
    val scheduleId: String = "",
    val courseId: Int = 0
)

/**
 * 日程详情有三种类型：
 * 1. 课程
 * 2. 日程 （用户自定义添加）
 * 3. divider ： 在显示的时候的分割线
 *
 * 课程类型必须填写的内容,eg:
 * WeeklyScheduleDetail(
    detailType = 0,
    name = "高等工程数学",
    location = "N636",
    time = "周一 8:00~9:00 (第1-2节)",
    week = "1~18周",
    teacher = "五十岚一辉"
)
 * 日程类型必须填写的内容，eg:
 *  WeeklyScheduleDetail(
        detailType = 1,
        location = "N517",
        time = "周一 5月29日 9:00~9:40",
        type = "运动",
        week = "18周",
        description = "这是一个简单的日程"
    )
 */
data class WeeklyScheduleDetail(
    val detailType: Int = 0, // 日程 = 1 课程 = 0 如果是divider -1课程占多数 默认为0，
    val name: String = "",   // 为日程时没有课程名称
    val location: String,
    // 格式样式:
    // 课程：周数 时间段 (节数)：如 周一 8:00~9:00 (第1-2节)
    // 日程: 周数 日期 时间段   如 周一 5月29日 9:00~9:40
    val time: String,
    val week: String, // 格式样式 1~18周
    val teacher: String = "",
    val type: String = "", // 日程的类型 如 运动 上课 ....
    val description: String = "",
    val note: String = "" // 自定义添加的备注信息
): Serializable


// todo 移动
// 添加到日程中的详细时间
data class ScheduleTime(
    val year: Int,
    val month: Int,
    val date: Int,
    val dayOfWeek: Int,
    val hour: Int,
    val minute: Int
)
//TODO 后续时间表Id

