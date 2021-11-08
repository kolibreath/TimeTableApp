package com.kolibreath.timetableapp.schedule.weeklyschedule

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TimeTableFragmentViewAdapter(
    fa: FragmentActivity
): FragmentStateAdapter(fa) {

    // todo the use of sparseArray
    // 进行懒加载的思路：
    // ViewPager2 默认会懒加载，如果不设置offScreenLimit不会加载当前View两端
    // Fragment只有可见才会执行onResume方法
    // 但是传入的Fragment还是需要实例化，防止Fragment的反复创建和销毁
    private val fragments = SparseArray<Fragment>()

    init{
        // 一个学期最多21周
        for(i in 0 until 21) fragments.put(i, TimeTableFragment(weekNum = i + 1))
    }

    override fun getItemCount(): Int = fragments.size()

    override fun createFragment(position: Int): Fragment = fragments[position]
}