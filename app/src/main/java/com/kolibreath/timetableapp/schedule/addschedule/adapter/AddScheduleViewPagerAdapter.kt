package com.kolibreath.timetableapp.schedule.addschedule.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kolibreath.timetableapp.schedule.addschedule.AddScheduleFragment

class AddScheduleViewPagerAdapter(
    private val fragments: ArrayList<AddScheduleFragment>,
    private val fa: FragmentActivity
): FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = fragments[position]

}
