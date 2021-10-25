package com.kolibreath.timetableapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.widget.FrameLayout
import android.widget.Scroller

// 课程表主要内容部分
// 一天中课程最多12节，一周最多7天

/**
 * 加载TableContent之前必须要传入:
 * @see courses 课程
 * @see courseTimes 课程对应的上课时间
 */
class TableContent(
    private val contxt: Context,
    private val attributeSet: AttributeSet
): FrameLayout(contxt, attributeSet) {

    private val WEEK_DAY_WIDTH = contxt.dp2px(65)
    private val COURSE_HEIGHT = contxt.dp2px(57)
    private val COURSE_WIDTH = contxt.dp2px(62)

    private val scroller = Scroller(contxt)

    // 绘制课表中水平和垂直的线段
    private val path = Path()
    private val paint = Paint().apply { isAntiAlias = true }

    var courses = ArrayList<Course>()
    var courseTimes = ArrayList<CourseTime>()

    // 已经完成绘制的课程时间 避免重复绘制
    private val drawnCourseTimeList = ArrayList<CourseTime>()

    // 周数
    private var weekNum = -1

    /**
     * 指定了课程节数的课程List，即没有courseTime 没有设置的CourseTime的集合
     * @see assignCourseTime
     */
    private val assignCourseTimeList = ArrayList<CourseTime>()



    init {
        // FrameLayout 默认不执行onDraw
        setWillNotDraw(false)
    }


    override fun onDraw(canvas: Canvas) {
        Log.d("TableContent", "onDraw run")

        val numOfCourses = startTimes.size
        // 将背景涂成白色打底
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        canvas.drawRect(0f, 0f, WEEK_DAY_WIDTH * 7, COURSE_HEIGHT * numOfCourses, paint)

        // 绘制水平分割线
        for (i in 0 until numOfCourses) {
            path.moveTo(0f, (i + 1) * COURSE_HEIGHT)
            path.lineTo(WEEK_DAY_WIDTH * 7, (i + 1) * COURSE_HEIGHT)
        }
        // 绘制垂直分割线
        for (i in 0 until 7) {
            path.moveTo(WEEK_DAY_WIDTH * (i + 1), 0f)
            path.lineTo(WEEK_DAY_WIDTH * (i + 1), COURSE_HEIGHT * numOfCourses)
        }

        paint.color = Color.GRAY
        paint.strokeWidth = contxt.dp2px(1)
        paint.style = Paint.Style.STROKE
        canvas.drawPath(path, paint)

        path.reset()
    }

    /**
     * 从数据库中读取的CourseTime进行绘制,因为存在指定课程节数和指定课程时间两种情况，
     * 而检查课程之间有无覆盖使用课程节数最好，所以在检查之前对于指定课程时间指定节数
     * @see assignCourseTime
     */
    private fun addCourses() {
        courseTimes
            .filter { it.courseTime == null || it.courseTime!!.isEmpty() }
            .forEach { assignCourseTime(it)}

        for (curCourseTime in courseTimes) {
            if (drawnCourseTimeList.contains(curCourseTime)) continue
            val overlapCourses = getOverlapCourses(curCourseTime)
            // 绘制这些重复的courseTime
            for (overlapCourse in overlapCourses) addCourseView(overlapCourse)
            drawnCourseTimeList.addAll(overlapCourses)
        }
    }

    /**
     * 指定课程节数
     * @param courseTime 向其中写入courseTime 字符串 "1,2,3"
     */
    private fun assignCourseTime(courseTime: CourseTime){
        val startTime = "${getHourFromDate(courseTime.customStartTime!!)}:${getMinuteFromDate(
            courseTime.customStartTime
        )}"
        val endTime = "${getHourFromDate(courseTime.customEndTime!!)}:${getMinuteFromDate(
            courseTime.customEndTime
        )}"

        val startMinute = time2minute(startTime)
        val endMinute = time2minute(endTime)

        val startCourseNum = time2courseNum(startMinute)
        val endCourseNum= time2courseNum(endMinute)

        val builder = StringBuilder()
        for(i in startCourseNum .. endCourseNum) {
            if(i == endCourseNum) builder.append("${i+1}")
            else builder.append("${i+1},")
        }
        courseTime.courseTime = builder.toString()
        assignCourseTimeList.add(courseTime)
    }

    /**
     * 根据周数加载从外部传入的课程
     * @param courseTimeList
     * @param week
     */
    fun updateCourses(courseTimeList: ArrayList<CourseTime>, week: Int) {
        this.weekNum = week
        this.courseTimes = courseTimeList
        addCourses()
        // 画完一周的课程之后清空drawnCourses 以便之后改变周数
        drawnCourseTimeList.clear()
    }

    /**
     * 添加单个View
     */
    private fun addCourseView(courseTime: CourseTime) {
        val courseView = CourseView(contxt)
        // TODO 考虑完全重叠课程
//        courseView.tipOn = overlapCourse(course.id.toInt())

        courseView.setTextColor(Color.WHITE)
        courseView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
        courseView.setPadding(
            contxt.dp2px(10).toInt(), contxt.dp2px(8).toInt(),
            contxt.dp2px(10).toInt(), contxt.dp2px(8).toInt()
        )

        var during = 0f // 这一节课经过的时间所对应的一节课的比例
        var topMargin:Float = startMorning.size * COURSE_HEIGHT      // 这一节课相对于顶部的margin
        // TODO schedules before 8:00
        if (!assignCourseTimeList.contains(courseTime)) {
            // case1: 使用课程节数设置的时间
            val tempList = courseTime.courseTime!!.split(",").map { it.toInt() }
            val step = tempList[tempList.size - 1] - tempList[0] + 1
            topMargin += (tempList[0] - 1) * COURSE_HEIGHT.toInt()
            during = step.toFloat()
        }else {
            // case:2 手动设置的时间
            val startTime = "${getHourFromDate(courseTime.customStartTime!!)}:${getMinuteFromDate(
                courseTime.customStartTime
            )}"
            val endTime = "${getHourFromDate(courseTime.customEndTime!!)}:${getMinuteFromDate(
                courseTime.customEndTime
            )}"

            val startMinute = time2minute(startTime)
            val endMinute = time2minute(endTime)

            val startCourseNum = time2courseNum(startMinute)
            // 计算topMargin
            val topStep: Float = ((startMinute - time2minute(startTimes[startCourseNum])).toFloat() / courseInterval )
            topMargin += (startCourseNum * COURSE_HEIGHT + COURSE_HEIGHT * topStep).toInt()

            // 计算during
            // 1. 找到日程结束时间所在的格子
            // 2. 计算所占用的比例
            val endCourseNum =  time2courseNum(endMinute)
            val bottomStep: Float = 1 - ((- endMinute + time2minute(endTimes[endCourseNum])).toFloat() / courseInterval )
            during = (endCourseNum - startCourseNum).toFloat() + bottomStep
        }

        // 设置长宽
        val courseParams = LayoutParams(
            COURSE_WIDTH.toInt(),
            (COURSE_HEIGHT * during - contxt.dp2px(3)).toInt()
        )
        // 设置margin标记位置
        courseParams.setMargins(
            WEEK_DAY_WIDTH.toInt() * courseTime.dayOfWeek,
            topMargin.toInt(), 0, 0
        )
        val curWeek = weekNum in courseTime.weeks.split(",").map { it.toInt()}

        courseView.text = ellipseText(courseTime, curWeek)

        val color = if (curWeek) getCourseByTime(courseTime, courses)!!.colorId else contxt.resources.getColor(R.color.grey)
        courseView.setBackgroundColor(color)

        addView(courseView, courseParams)
    }

    // 将使用字符串表示的时间,如8:00 转换成对应的时间（分钟）8*60 + 0
    private fun time2minute(time: String): Int {
        val temp = time.split(":").map { it.toInt() }
        val hour = temp[0]
        val minute = temp[1]
        return hour * 60 + minute
    }


    /**
     * 找出当前的时间属于那一节课 并且返回下标。
     * 假设定义的最早的时间是早上6：00，如果有时间早于6：00，也会从6：00开始的方格中绘制.
     *
     */
    private fun time2courseNum(curMinute: Int): Int{
        // 小于早上六点，仍然绘制在第一格中
        if(curMinute < time2minute(startTimes[0])) return 0
        for(i in startTimes.indices) {
            if(curMinute >= time2minute(startTimes[i]) &&
                    curMinute <= time2minute(endTimes[i])) return i
        }
        return -1
    }

    /**
     *   将当前课程的开始结束时间同一转换成时间戳 ，查看当前课程和另外一节课程有无重叠
     *   重叠的课程判断标准
     *   1. 首先在相同的天(在相同的天，但是不在相同的周，绘制的时候也可能出现重叠的情况)
     *   2. (另外一节课的开始时间 >= 这一节课的结束时间 && 另外一节课的结束时间 <= 这一节课的结束时间)
     *   || (另外一节课的开始时间 <= 这一节课的结束时间 && 另外一节课的结束时间 >= 这一节课的结束时间)
     *   @param curCourseTime 当前的课程时间
     *   @param time 数据库中的另外一节课程时间
     */
    private fun check4overlap(curCourseTime: CourseTime, time: CourseTime): Boolean {
        val curTimes = courseTime2Stamp(curCourseTime)
        // 如果两节课不同一天
        if (time.dayOfWeek != curCourseTime.dayOfWeek) return false
        val times = courseTime2Stamp(curCourseTime)
        if ((times[0] >= curTimes[0] && (times[1] <= curTimes[1]) || times[0] <= curTimes[0] && times[1] >= curTimes[1]))
            return true

        return false
    }

    /**
     * 任务目标：
     * 1. 将当前的课程和数据库中的其他的课程时间向对比，找出所有重叠的课程
     * eg, A B C D E F G 中 A B 重叠 B C 重叠，但是A C 不直接重叠，这种情况会将A B C 放入一个集合并且作为返回值
     * 2. 避免重复绘制，出现重叠的课程被加入
     * @see drawnCourseTimeList
     *
     * @param  curCourseTime 查找所有可能出现的课程
     * @return  如果返回List size 大于1，说明当前courseTime存在重复
     */
    private fun getOverlapCourses(curCourseTime: CourseTime): List<CourseTime> {
        val overlap = ArrayList<CourseTime>().apply { add(curCourseTime) }
        for (time in courseTimes) {
            if (curCourseTime == time) continue
            if (check4overlap(curCourseTime, time)) overlap.add(time)
        }
        if (overlap.size == 1) return overlap

        // 找到overlap中跨度最长的课程时间，然后再次遍历courseTimeList，寻找是否还存在其他的重叠课程
        val maxCourseTime = overlap.maxByOrNull {
            val times = courseTime2Stamp(it)
            times[1] - times[0]
        }

        for (time in courseTimes) {
            if (curCourseTime == time) continue
            if (check4overlap(maxCourseTime!!, time)) {
                if (!overlap.contains(time)) overlap.add(time)
            }
        }
        return overlap

    }

    // 找到CourseTime对应的Course
    private fun getCourseByTime(courseTime: CourseTime, courses: List<Course>): Course? {
        for (course in courses) {
            if (course.id == courseTime.courseId) return course
        }
        return null
    }

    private fun ellipseText(courseTime: CourseTime, curWeek: Boolean): String {
        val course = getCourseByTime(courseTime, courses)!!
        val ellipseCourseName =
            if (curWeek) {
                if (course.name.length > 8) "${course.name.substring(0, 7)}..."
                else course.name
            } else {
                if (course.name.length > 3) "非本周${course.name.substring(0, 3)}..."
                else "非本周${course.name}"
            }
        return "$ellipseCourseName\n\n@${courseTime.place}\n${courseTime.teacher}"
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
            postInvalidate()
        }
    }

}