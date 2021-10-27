package com.kolibreath.timetableapp

import android.content.Context
import android.view.Window
import androidx.viewpager2.widget.ViewPager2


/**
 * 选择日期的Dialog
 * @see DateSelectView
 * @see DateSelectViewAdapter
 */
class DatePickerDialog(
    private val ctxt: Context
): BaseBottomDialog(ctxt) {

    override fun onCreate(context: Context) {
        setContentView(R.layout.dialog_date_picker)
        val viewPager = findViewById<ViewPager2>(R.id.view_pager)
        viewPager.setCurrentItem(3, true)
        viewPager.adapter = DateSelectViewAdapter(context)
    }

    override fun setBackgroundDrawableRes(resId: Int, window: Window) {
//        window.setback

    }
}