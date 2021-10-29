package com.kolibreath.timetableapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class AddScheduleAdapter(
    private val fragments: ArrayList<AddScheduleFragment>,
    private val fa: FragmentActivity
): FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = AddScheduleFragment()

}
