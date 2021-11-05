package com.kolibreath.timetableapp.schedule.addschedule

import android.view.View
import com.kolibreath.timetableapp.R
import com.kolibreath.timetableapp.base.BaseBottomDialogFragment

class WeekPickerDialogFragment(
    private val resId: Int
): BaseBottomDialogFragment(resId) {

    private lateinit var weekPicker: WeekPicker
    private lateinit var onWeeksSelectedListener: OnWeeksSelectedListener

    override fun initChild(rootView: View) {
        weekPicker = rootView.findViewById(R.id.wp_dialog_fragment)
        // 设置确定和取消的回调
        val onCancelClickListener = View.OnClickListener {
           this@WeekPickerDialogFragment.dismiss()
        }
        val onConfirmClickListener = View.OnClickListener {
            this@WeekPickerDialogFragment.dismiss()
            onWeeksSelectedListener.onWeeksSelected(weekPicker.selectedWeeks)
        }

        weekPicker.setOnCancelClickListener(onCancelClickListener)
        weekPicker.setOnConfirmClickListener(onConfirmClickListener)

    }


    /**
     * @param onWeeksSelectedListener 传回点击周数组成的ArrayList
     */
    fun setOnWeeksSelectedListener(onWeeksSelectedListener: OnWeeksSelectedListener) {
        this.onWeeksSelectedListener = onWeeksSelectedListener
    }

    // 选中之后将WeekPickerDialogFragment中的数据传出的回调
    interface OnWeeksSelectedListener{
        fun onWeeksSelected(weeks: ArrayList<Int>)
    }
}