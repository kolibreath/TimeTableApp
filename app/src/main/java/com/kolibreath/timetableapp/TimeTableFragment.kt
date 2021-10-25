package com.kolibreath.timetableapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TimeTableFragment: BaseFragment(){

    companion object {
        fun newInstance(): TimeTableFragment = TimeTableFragment()
    }

    // todo make course_height width and week_width adaptive
    private var COURSE_HEIGHT = 0f

    private var timeTable: TimeTable? = null
    private var tableContent: TableContent? = null
    private var tvSwitchView: TextView? = null
    private var tvWeekSelect: TextView? = null
    private var ibWeekSelect: ImageButton? = null
    private var ibSchedule: ImageButton? = null
    private var weekSelectView: WeekSelectView? = null
    private var fabLocate: FloatingActionButton? = null
    private var courseTimeLayout: CourseTimeLayout? = null

    // todo remove later
    private var courses = ArrayList<Course>()
    private var courseTimeList = ArrayList<CourseTime>()


    //todo 需要改成从数据库中读取
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

        val courses = arrayListOf(course1, course2, course3)
        val courseTimes = arrayListOf(courseTime1, courseTime4, courseTime3, courseTime5, courseTime2, courseTime6)

        this.courses = courses
        this.courseTimeList = courseTimes

        tableContent?.let { it.courses = courses; it.courseTimes = courseTimes }
        tableContent?.let{ it.updateCourses(courseTimeList = courseTimes, week = Preference(this@TimeTableFragment.requireContext()).get(
            CUR_WEEK,-1)) }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        COURSE_HEIGHT = requireContext().dp2px(57)
        val rootView = LayoutInflater.from(context).inflate(R.layout.fragment_timetable, container, false)
        initView(rootView)
        loadCourseTime()
        return rootView
    }

    /**
     * 初始化视图，通过周数设置相关View
     * 加载App时，周数默认设置为当前周数，但是也可以通过[WeekSelectView]修改周数
     * todo disable WeekSelectView write sharedpreference
     */
    private fun initView(rootView: View){
        timeTable = rootView.findViewById(R.id.timetable)
        tableContent = rootView.findViewById(R.id.timetable_content)

        // 切换视图
        tvSwitchView = rootView.findViewById<TextView>(R.id.tv_switch_view).apply{
            //TODO 实现视图的切换
//            setOnClickListener(this@TimeTableFragment)
        }


        val wsvListener = View.OnClickListener {
            if (weekSelectView!!.slideStatus == WeekSelectView.WeekSelectViewStatus.OPEN){
                // 当前为出现
                weekSelectView!!.slideUp()
                // TODO 这里的invisible是否必要？
                weekSelectView!!.visibility =View.INVISIBLE
            }else {
                weekSelectView!!.slideDown()
                weekSelectView!!.visibility =View.VISIBLE
            }
        }

        // 控制WeekSelectView弹出和回收
        tvWeekSelect = rootView.findViewById<TextView>(R.id.tv_week_select)
                // todo preference should use application context
            .apply {
                setOnClickListener(wsvListener);
                text = "第${Preference(this@TimeTableFragment.requireContext()).get(
                CUR_WEEK,-1)}周"
            }
        ibWeekSelect = rootView.findViewById<ImageButton>(R.id.ib_week_select)
            .apply {setOnClickListener(wsvListener)}

        weekSelectView = rootView.findViewById<WeekSelectView>(R.id.view_week_select).apply {
            setOnWeekSelectedListener( object : WeekSelectView.OnWeekSelectedListener {
                override fun onWeekSelected(week: Int) {
                    tableContent!!.updateCourses(courseTimeList, week)
                    // todo imageButton direction
                    tvWeekSelect!!.text = "第${week}周"
                }
            })
        }

        // 将CourseTimeLayout 和 TableContent定位到开始课程的位置
        courseTimeLayout = timeTable!!.findViewById<CourseTimeLayout>(R.id.course_time_layout)
        locateTableContent()
        fabLocate = rootView.findViewById<FloatingActionButton>(R.id.fab_locate).apply {
            setOnClickListener{
                locateTableContent()
            }
        }

    }

    private fun locateTableContent(){
        courseTimeLayout!!.scrollTo(0, (startMorning.size * COURSE_HEIGHT).toInt())
        tableContent!!.scrollTo(0, (startMorning.size * COURSE_HEIGHT).toInt())
    }

}