package com.kolibreath.timetableapp.schedule.weeklyschedule

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kolibreath.timetableapp.*
import com.kolibreath.timetableapp.schedule.addschedule.AddScheduleActivity

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
    private lateinit var fabUtils: FloatingActionButton
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
                text = "第${
                    Preference(this@TimeTableFragment.requireContext()).get(
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
        courseTimeLayout = timeTable!!.findViewById(R.id.course_time_layout)
        locateTableContent()

        fabUtils = rootView.findViewById(R.id.fab_utils)

        // 单位:dp
        val initMargin = 20
        // 动画移动到的相对开始的位置
        val relativePos = 70

        fun paramsFactory(): RelativeLayout.LayoutParams {
            return RelativeLayout.LayoutParams(
                fabUtils.layoutParams.width,
                fabUtils.layoutParams.height
            ).apply {
                addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                rightMargin = this@TimeTableFragment.requireContext().dp2px(initMargin).toInt()
                bottomMargin = this@TimeTableFragment.requireContext().dp2px(initMargin).toInt()
            }
        }

        val fabLocate = FloatingActionButton(this.requireContext())
            .apply {
                visibility = View.INVISIBLE
                setOnClickListener {  locateTableContent() }
            }
        val fabAddSchedule = FloatingActionButton(this.requireContext())
            .apply {
                visibility = View.INVISIBLE
                setOnClickListener {
                    val intent = Intent(this@TimeTableFragment.requireActivity(), AddScheduleActivity::class.java)
                    this@TimeTableFragment.requireActivity().startActivity(intent)
                }
            }

        rootView as ViewGroup
        rootView.addView(fabLocate, paramsFactory())
        rootView.addView(fabAddSchedule, paramsFactory())

        // 点击fabUtils 弹出另外两个小控件
        fabUtils.apply {
            setOnClickListener{
                if(fabLocate.visibility == View.INVISIBLE && fabAddSchedule.visibility == View.INVISIBLE) {
                    fabLocate.visibility = View.VISIBLE
                    fabAddSchedule.visibility = View.VISIBLE
                    move(fab1 = fabLocate, fab2 = fabAddSchedule, - requireContext().dp2px(relativePos))

                    Log.d("TimeTable", "${fabLocate.right} ${fabUtils.right}")
                }else {
                    move(fab1 = fabLocate, fab2 = fabAddSchedule, requireContext().dp2px(relativePos))
                    this.postDelayed( {
                        fabLocate.visibility = View.INVISIBLE
                        fabAddSchedule.visibility = View.INVISIBLE
                    }, 600)
                }

            }
        }
    }

    private fun move(
        fab1: FloatingActionButton,
        fab2: FloatingActionButton,
        relativePos: Float)
    {
        AnimatorSet().apply {
            val translationX = transX(fab1, relativePos)
            val translationY = transY(fab2, relativePos)
            play(translationX).with(translationY)
        }.start()
    }

    private fun transX(view: View, relativePos: Float): ValueAnimator{
        val left = view.left
        val right = view.right
        return ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 500
            addUpdateListener {
                val curValue = ((it.animatedValue as Float) * relativePos).toInt()
                view.layout(left + curValue, view.top, right + curValue, view.bottom)
            }
        }
    }

    private fun transY(view: View, relativePos: Float): ValueAnimator{
        val top = view.top
        val bottom = view.bottom
        return ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 500
            addUpdateListener {
                val curValue = ((it.animatedValue as Float) * relativePos).toInt()
                view.layout(view.left, top + curValue , view.right, bottom + curValue)
            }
        }
    }


    private fun locateTableContent(){
        courseTimeLayout!!.scrollTo(0, (startMorning.size * COURSE_HEIGHT).toInt())
        tableContent!!.scrollTo(0, (startMorning.size * COURSE_HEIGHT).toInt())
    }

}