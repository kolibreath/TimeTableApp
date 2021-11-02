package com.kolibreath.timetableapp.base.ui

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.kolibreath.timetableapp.R

class DateSelectViewAdapter(
    private val context: Context,
    private val onDateSelectConfirmListener: OnDateSelectConfirmListener? = null
    ) : RecyclerView.Adapter<DateSelectViewAdapter.DateSelectViewHolder>() {

    private val numOfMonth = 12
//    // 保存上一个确定的Date
    private var preSelectedDate = -1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateSelectViewHolder {
        val rootView =
            LayoutInflater.from(context).inflate(R.layout.view_date_picker, parent, false)

        return DateSelectViewHolder(
            rootView,
            object : DateSelectView.OnDateSelectedListener {
                override fun onDateSelected(date: Int, month: Int) {
                    preSelectedDate = date
                    onDateSelectConfirmListener?.onDateSelectConfirmListener(date, month)
                    // fixme notifyDataSetChanged force to call onBindView
                    // find some way to optimize it
                    notifyDataSetChanged()
                }
            })
    }

    override fun getItemCount(): Int = numOfMonth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DateSelectViewHolder, position: Int) {
        holder.bind(position+1, preSelectedDate)
    }

    // todo inner and internal class of Kotlin, what is the difference?
// 只允许设置今年内的事件
    class DateSelectViewHolder internal constructor(
        private val rootView: View,
        private val listener: DateSelectView.OnDateSelectedListener
    ): RecyclerView.ViewHolder(rootView) {

        private val dateSelectView: DateSelectView = (rootView as ViewGroup).getChildAt(1) as DateSelectView

        // month表示月份 1月用数字1表示
        //fixme using joda-time instead
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(month: Int, preSelectedDate: Int){
            dateSelectView.loadView(month, preSelectedDate)
            dateSelectView.setOnDateSelectedListener(listener)
        }

    }

    interface OnDateSelectConfirmListener {
        fun onDateSelectConfirmListener(date: Int, month: Int)
    }

}

