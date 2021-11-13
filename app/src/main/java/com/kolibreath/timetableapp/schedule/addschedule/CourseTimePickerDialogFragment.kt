package com.kolibreath.timetableapp.schedule.addschedule

import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kolibreath.timetableapp.R
import com.kolibreath.timetableapp.base.ui.dialogfragment.BaseBottomDialogFragment
import com.kolibreath.timetableapp.dp2px
import com.kolibreath.timetableapp.schedule.addschedule.adapter.TimePickerViewPagerAdapter

/**
 * 该弹出框包括两个部分，一个节数选择 一个时间选择
 * 具体的UI包装在ViewPager中，使用CourseTimePickerFragment实现
 */
class CourseTimePickerDialogFragment(
    private val resId: Int
): BaseBottomDialogFragment(resId) {

    //tab layout and view pager
    private lateinit var tablayout: TabLayout
    private lateinit var viewPager: ViewPager2

    // 通过回调传回来的数据
    private var weekday = ""
    private var courseNum1 = ""
    private var courseNum2 = ""

    override fun initChild(rootView: View) {

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

        // fixme 为什么直接使用include 显示的Layout在ViewPager2上面？
        val includeRootView = LayoutInflater.from(requireActivity()).inflate(R.layout.layout_buttons, null, false)
        includeRootView.findViewById<TextView>(R.id.btn_cancel).apply {
            setOnClickListener { dismiss() }
        }
        includeRootView.findViewById<TextView>(R.id.btn_confirm).apply {
            setOnClickListener {
                // todo add new schedule to database
                dismiss()
            }
        }
        val vertMargin = requireActivity().dp2px(20).toInt()
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(RelativeLayout.BELOW, R.id.vp_dialog_fragment_time_picker)
            topMargin = vertMargin
        }
        (rootView as RelativeLayout).addView(includeRootView, params)
    }
}