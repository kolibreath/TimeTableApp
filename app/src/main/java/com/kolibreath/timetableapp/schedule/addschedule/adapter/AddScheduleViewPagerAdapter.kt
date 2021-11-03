package com.kolibreath.timetableapp.schedule.addschedule.adapter

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kolibreath.timetableapp.schedule.addschedule.AddCourseFragment
import com.kolibreath.timetableapp.schedule.addschedule.AddScheduleFragment

class AddScheduleViewPagerAdapter(
    private val fa: FragmentActivity
): FragmentStateAdapter(fa) {
    private val fragments = SparseArray<Fragment>()

    init{
        fragments.put(0, AddCourseFragment())
        fragments.put(1, AddScheduleFragment())
    }

    override fun getItemCount(): Int = fragments.size()

    override fun createFragment(position: Int): Fragment = fragments[position]

}
