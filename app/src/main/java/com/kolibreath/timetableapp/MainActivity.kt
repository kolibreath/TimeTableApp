package com.kolibreath.timetableapp

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.TranslateAnimation
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kolibreath.timetableapp.schedule.addschedule.AddScheduleActivity
import com.kolibreath.timetableapp.schedule.weeklyschedule.*
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    private lateinit var tvCurWeek: TextView
    private lateinit var tvCurDate: TextView
    private lateinit var ibAddSchedule: ImageButton
    private lateinit var ibImportSchedule: ImageButton
    private lateinit var ibExportSchedule: ImageButton
    private lateinit var ibList: ImageButton
    private lateinit var fabLocate: FloatingActionButton
    private lateinit var viewPager: ViewPager2

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)

        Preference(this).put(CUR_WEEK, getCurrWeekNum())

        initViews()

    }

    //fixme joda-time
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initViews() {
        tvCurWeek = findViewById(R.id.tv_cur_week)
        tvCurDate = findViewById(R.id.tv_cur_date)
        ibAddSchedule = findViewById(R.id.ib_add_schedule)
        ibImportSchedule = findViewById(R.id.ib_import_schedule)
        ibExportSchedule = findViewById(R.id.ib_export_schedule)
        ibList = findViewById(R.id.ib_list)
        fabLocate = findViewById(R.id.fab_locate)

        viewPager = findViewById(R.id.main_view_pager)
        setViewPagerTouchSlop()
        viewPager.adapter = TimeTableFragmentViewAdapter(this)
        viewPager.offscreenPageLimit = 2

        fabLocate.setOnClickListener {
            val curFragment = this@MainActivity.supportFragmentManager
                .findFragmentByTag("f${viewPager.currentItem}") as TimeTableFragment
            curFragment.locateTableContent()
        }

        // todo implement import and export
        ibImportSchedule.setOnClickListener {
            ImportDialogFragment(R.layout.dialog_fragment_import)
                .show(this@MainActivity.supportFragmentManager, "import")
        }

        ibExportSchedule.setOnClickListener {
            ExportDialogFragment(R.layout.dialog_fragment_export)
                .show(this@MainActivity.supportFragmentManager, "export")
        }

        // 设置当前日期
        tvCurDate.text = run {
            val now = LocalDate.now()
            "${now.month.value}月${now.dayOfMonth}日 ${num2WeekdayCn(now.dayOfWeek.value-1)}"
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
             tvCurWeek.text = "第${position+1}周"
            }
        })

        // 弹出的选择不能使用Window，因为Window会覆盖Toolbar_layout 影响用户体验
        // 使用View模拟Window弹出的效果
        val settingView = LayoutInflater.from(this)
            .inflate(R.layout.view_time_table_settings, null, false).apply{
                background = ColorDrawable(Color.WHITE)
                visibility = View.GONE
            }
        val settingViewParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        settingViewParams.topMargin = resources.getDimension(R.dimen.toolbar_height)
            .toInt()

        val rootView = viewPager.parent as ViewGroup
        rootView.addView(settingView, settingViewParams)

        var canOpen = true

        val shadeView = findViewById<View>(R.id.shade_view).apply {
            setOnClickListener {
                if (visibility == View.VISIBLE) {
                    settingView.slideUp()
                    visibility = View.GONE
                    settingView.visibility = View.GONE
                    canOpen = true
                }
            }
        }

        ibList.setOnClickListener {
            canOpen = if(canOpen) {
                settingView.slideDown()
                shadeView.visibility = View.VISIBLE
                settingView.visibility = View.VISIBLE
                false
            }else {
                settingView.slideUp()
                shadeView.visibility = View.GONE
                settingView.visibility = View.GONE
                true
            }
        }

        ibAddSchedule.setOnClickListener {
            val intent = Intent(this@MainActivity, AddScheduleActivity::class.java)
            this@MainActivity.startActivity(intent)
        }
    }


    // 反射修改ViewPager2横向滑动的灵敏度
    private fun setViewPagerTouchSlop() {
        val recyclerViewField = viewPager::class.java.getDeclaredField("mRecyclerView")
        recyclerViewField.isAccessible = true
        val recyclerView =  recyclerViewField.get(viewPager)

        val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
        touchSlopField.isAccessible = true
        val touchSlop = touchSlopField.get(recyclerView) as Int
        touchSlopField.set(recyclerView, touchSlop * 3)
    }

    private fun View.slideUp() {
        slide(-dp2px(this.height).toInt())
    }

    private fun View.slideDown() {
        slide(0)
    }

    private fun View.slide(toY: Int) {
        val animation: TranslateAnimation = if (toY < 0) {
            TranslateAnimation(0f, 0f, 0f, toY.toFloat())
        } else {
            TranslateAnimation(0f, 0f, -dp2px(this.height), 0f)
        }
        animation.duration = 250
        animation.fillAfter = true
        this.startAnimation(animation)
    }

}
