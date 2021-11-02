package com.kolibreath.timetableapp.base.ui

import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.viewpager2.widget.ViewPager2
import com.kolibreath.timetableapp.R
import com.kolibreath.timetableapp.base.BaseBottomDialogFragment
import java.time.LocalDate


/**
 * 选择日期的Dialog
 * @see DateSelectView
 * @see DateSelectViewAdapter
 */

class DatePickerDialogFragment(
    private val resId: Int
): BaseBottomDialogFragment(resId), View.OnClickListener {

    private var date: Int = 0
    private var month: Int = 0

    override fun initChild(rootView: View) {
        val viewPager = rootView.findViewById<ViewPager2>(R.id.vp_dialog_fragment_date_picker)

        // 初始化View
        val tvDate = rootView.findViewById<TextView>(R.id.tv_date)
        val tvDayOfWeek = rootView.findViewById<TextView>(R.id.tv_day_of_week)

        val tvCancel = rootView.findViewById<TextView>(R.id.tv_cancel)
        val tvConfirm = rootView.findViewById<TextView>(R.id.tv_confirm)

        viewPager.setCurrentItem(3, true)
        viewPager.adapter = DateSelectViewAdapter(
            requireActivity(),
            object: DateSelectViewAdapter.OnDateSelectConfirmListener{
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDateSelectConfirmListener(date: Int, month: Int) {
                    // fixme joda-time
                    val thisYear = LocalDate.now().year
                    val dateStr = "${thisYear}年${month}月${date}日"
                    tvDate.text = dateStr
                    val localDate = LocalDate.of(thisYear, month, date)
                    tvDayOfWeek.text = when(localDate.dayOfWeek.value) {
                        1 -> "周一"
                        2 -> "周二"
                        3 -> "周三"
                        4 -> "周四"
                        5 -> "周五"
                        6 -> "周六"
                        7 -> "周日"
                        else -> ""
                    }

                    this@DatePickerDialogFragment.date = date
                    this@DatePickerDialogFragment.month = month
                }
            }
        )
    }

    override fun onClick(v: View) {
        when(view?.id){
            R.id.tv_cancel -> dismiss()
            R.id.tv_confirm -> {
                dismiss()
                // todo write data into database
            }
        }
    }
}