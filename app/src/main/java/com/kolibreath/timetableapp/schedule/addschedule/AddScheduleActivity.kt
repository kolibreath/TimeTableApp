package com.kolibreath.timetableapp.schedule.addschedule

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kolibreath.timetableapp.R
import com.kolibreath.timetableapp.schedule.addschedule.adapter.AddScheduleViewPagerAdapter


class AddScheduleActivity: FragmentActivity() {

    private val tabs = arrayOf("课程", "日程")

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_schedule)

        tabLayout = findViewById(R.id.tl_add_schedule)
        viewPager = findViewById(R.id.vp_add_schedule)

        for (i in tabs.indices) {
            tabLayout.addTab(tabLayout.newTab().setText(tabs[i]))
        }

        viewPager.adapter = AddScheduleViewPagerAdapter(this)
        TabLayoutMediator(
            tabLayout,
            viewPager
        ) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }
}