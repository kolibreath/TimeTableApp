package com.kolibreath.timetableapp.schedule.addschedule

import android.view.View
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kolibreath.timetableapp.R
import com.kolibreath.timetableapp.base.ui.dialogfragment.BaseBottomDialogFragment
import com.kolibreath.timetableapp.schedule.addschedule.adapter.TimePickerViewPagerAdapter

/**
 * 包装了TimePickerFragment的弹出框
 */
class CourseTimePickerDialogFragment(
    private val resId: Int
): BaseBottomDialogFragment(resId), View.OnClickListener {

    private lateinit var btnCancel: TextView
    private lateinit var btnConfirm: TextView

    //tab layout and view pager
    private lateinit var tablayout: TabLayout
    private lateinit var viewPager: ViewPager2

    // 通过回调传回来的数据
    private var weekday = ""
    private var courseNum1 = ""
    private var courseNum2 = ""

    override fun initChild(rootView: View) {
        btnCancel = rootView.findViewById(R.id.tv_cancel)
        btnConfirm = rootView.findViewById(R.id.tv_confirm)

        tablayout = rootView.findViewById(R.id.tl_dialog_fragment_time_picker)
        viewPager = rootView.findViewById(R.id.vp_dialog_fragment_time_picker)

        val tabFragmentList = ArrayList<CourseTimePickerFragment>()
        val tabs = arrayOf("节数", "时间")

        for (i in tabs.indices) {
            tablayout.addTab(tablayout.newTab().setText(tabs[i]))
            tabFragmentList.add(CourseTimePickerFragment.newInstance(i).apply {
                setOnResultChangeListener(object: CourseTimePickerFragment.OnResultChangeListener {
                    override fun onResultChange(
                        weekday: String,
                        courseNum1: String,
                        courseNum2: String
                    ) {
                        this@CourseTimePickerDialogFragment.weekday = weekday
                        this@CourseTimePickerDialogFragment.courseNum1 = courseNum1
                        this@CourseTimePickerDialogFragment.courseNum2 = courseNum2
                    }
                })
            })
        }

        viewPager.adapter = TimePickerViewPagerAdapter(tabFragmentList, requireActivity())
        TabLayoutMediator(
            tablayout,
            viewPager
        ) { tab, position ->
            tab.text = tabs[position]
        }.attach()

    }



    override fun onClick(v: View?) {
        // todo implement event for user completes selecting weeks
        // 还需要检查返回的数据的合法性
    }


}