package com.kolibreath.timetableapp.base.ui

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.kolibreath.timetableapp.R
import com.kolibreath.timetableapp.base.BaseBottomDialogFragment


/**
 * 选择日期的Dialog
 * @see DateSelectView
 * @see DateSelectViewAdapter
 */

class DatePickerDialogFragment(
    private val resId: Int
): BaseBottomDialogFragment(resId) {
    override fun initChild(rootView: View) {
        val viewPager = rootView.findViewById<ViewPager2>(R.id.vp_dialog_fragment_date_picker)
        viewPager.setCurrentItem(3, true)
        viewPager.adapter = DateSelectViewAdapter(requireActivity())
    }
}