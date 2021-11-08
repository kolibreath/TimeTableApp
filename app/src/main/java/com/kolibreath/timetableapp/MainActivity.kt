package com.kolibreath.timetableapp

import android.os.Build
import android.os.Bundle
import android.view.Window
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kolibreath.timetableapp.schedule.weeklyschedule.ExportDialogFragment
import com.kolibreath.timetableapp.schedule.weeklyschedule.ImportDialogFragment
import com.kolibreath.timetableapp.schedule.weeklyschedule.TimeTableFragment
import com.kolibreath.timetableapp.schedule.weeklyschedule.TimeTableFragmentViewAdapter
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

}
