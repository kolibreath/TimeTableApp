package com.kolibreath.timetableapp.schedule.weeklyschedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kolibreath.timetableapp.*
import com.kolibreath.timetableapp.base.LazyFragment

class TimeTableFragment(
    private val resId: Int = R.layout.fragment_timetable,
    private val weekNum: Int
): LazyFragment(resId){

    private var timeTable: TimeTable? = null
    private var tableContent: TableContent? = null
    private var courseTimeLayout: CourseTimeLayout? = null

    private var courses = ArrayList<Course>()
    private var courseTimeList = ArrayList<CourseTime>()


    //todo read from database
    private fun loadCourseTime() {
        // course1 courseTime 为 1 and 4
        val course1 = Course(
            id = "1",
            name = "摆烂",
            timeIds = "1,4",
            colorId = requireContext().resources.getColor(R.color.blue)
        )

        // course2 courseTime 为 3 and 5
        val course2 = Course(
            id = "2",
            name ="装逼之我有一个朋友在华为年薪100w",
            timeIds = "3,5",
            colorId = requireContext().resources.getColor(R.color.red)
        )

        // course3 courseTime 为 2
        val course3 = Course(
            id = "3",
            name = "学习",
            timeIds = "2",
            colorId = requireContext().resources.getColor(R.color.yellow)
        )

        val courseTime1 = CourseTime(
            id = "1",
            place = "N636",
            teacher = "郭京蕾",
            dayOfWeek = 0,
            courseTime = "1,2",
            courseId = "1",
            weeks = "1,2,3,4,5,6",
        )

        val courseTime4 = CourseTime(
            id = "4",
            place = "N636",
            teacher = "郭京蕾",
            dayOfWeek = 3,
            courseTime = "2,3",
            courseId = "1",
            weeks = "1,2,3,4,5,6",
        )


        val courseTime3 = CourseTime(
            id = "3",
            place = "N632",
            teacher = "沈显君",
            dayOfWeek = 1,
            courseTime = "5,6,7,8,9",
            courseId = "2",
            weeks = "1,2,3,4,5,6",
        )

        val courseTime5 = CourseTime(
            id = "5",
            place = "N632",
            teacher = "沈显君",
            dayOfWeek = 0,
            courseTime = "2,3",
            courseId = "2",
            weeks = "1,2,3,4,5,6",
        )

        val courseTime2 = CourseTime(
            id = "2",
            place = "N632",
            teacher = "赵卫中",
            dayOfWeek = 1,
            courseTime = "2,3",
            courseId = "3",
            weeks = "1,2,3,4,5,6",
        )

        val courseTime6= CourseTime(
            id = "6",
            place = "N632",
            teacher = "赵卫中",
            dayOfWeek = 5,
            customStartTime = string2Date("2021/9/25 9:00"),
            customEndTime = string2Date("2021/9/25 14:00"),
            courseId = "3",
            weeks = "1,2,3,4,5,6",
        )

        val courseTime7 = CourseTime(
            id = "7",
            place = "N636",
            teacher = "郭京蕾",
            dayOfWeek = 4,
            courseTime = "5,6",
            courseId = "1",
            weeks = "1,2,3,4,5,6",
        )

        val courseTime8 = CourseTime(
            id = "8",
            place = "N636",
            teacher = "郭京蕾",
            dayOfWeek = 4,
            courseTime = "5,6",
            courseId = "1",
            weeks = "1,2,3,4,5,6",
        )

        val courseTime9 = CourseTime(
            id = "9",
            place = "N632",
            teacher = "赵卫中",
            dayOfWeek = 1,
            courseTime = "5,6",
            courseId = "3",
            weeks = "1,2,3,4,5,6",
        )

        val courses = arrayListOf(course1, course2, course3)
        val courseTimes = arrayListOf(
            courseTime1, courseTime4, courseTime3,
            courseTime5, courseTime2, courseTime6,
            courseTime7, courseTime8, courseTime9)

        this.courses = courses
        this.courseTimeList = courseTimes

        tableContent?.let { it.courses = courses; it.courseTimes = courseTimes }
        tableContent?.updateCourses(
            courseTimeList = courseTimes,
            week = weekNum
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = LayoutInflater.from(context).inflate(resId, container, false)
        initView(rootView)
        loadCourseTime()
        return rootView
    }

    /**
     * 初始化视图，通过周数设置相关View
     * 加载App时，周数默认设置为当前周数，但是也可以通过[WeekSelectView]修改周数
     * todo disable WeekSelectView write sharedpreference
     */
    override fun initView(rootView: View){
        timeTable = rootView.findViewById(R.id.timetable)
        tableContent = rootView.findViewById(R.id.timetable_content)

        // 将CourseTimeLayout 和 TableContent定位到开始课程的位置
        courseTimeLayout = timeTable!!.findViewById(R.id.course_time_layout)
        locateTableContent()
    }

    fun locateTableContent(){
        courseTimeLayout!!.scrollTo(0, (startMorning.size * courseTimeHeight).toInt())
        tableContent!!.scrollTo(0, (startMorning.size * courseTimeHeight).toInt())
    }

    override fun initData() {
        loadCourseTime()
    }

    override fun initEvent() {}



    fun canScrollDown(): Boolean {
        // 上面有剩余的部分
        return tableContent!!.scrollY > 0
    }
}