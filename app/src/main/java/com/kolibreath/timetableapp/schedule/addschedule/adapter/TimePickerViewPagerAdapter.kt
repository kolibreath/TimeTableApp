package com.kolibreath.timetableapp.schedule.addschedule.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kolibreath.timetableapp.schedule.addschedule.TimePickerFragment

/**
 * Viewpager 在自定义日程中控制以节数或者是以具体时间为单位选择
 */

// todo late init fragments
class TimePickerViewPagerAdapter(
    private val fragments: ArrayList<TimePickerFragment>,
    private val fragmentActivity: FragmentActivity
): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}
