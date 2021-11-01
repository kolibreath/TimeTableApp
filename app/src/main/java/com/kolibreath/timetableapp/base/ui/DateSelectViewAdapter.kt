package com.kolibreath.timetableapp.base.ui

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.kolibreath.timetableapp.R
import com.kolibreath.timetableapp.num2MonthInCn

class DateSelectViewAdapter(
    private val context: Context
    ) : RecyclerView.Adapter<DateSelectViewAdapter.DateSelectViewHolder>() {

    private val numOfMonth = 12
//    // 保存上一个划过的DateSelectView 划走之后需要选定
    private var preDateSelectView: DateSelectView? = null
//    // 保存上一个确定的Date
//    private var preSelectedDate = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateSelectViewHolder {
        // 从resId加载组合而成的View
        val rootView =
            LayoutInflater.from(context).inflate(R.layout.view_date_picker, parent, false)
        val viewHolder = DateSelectViewHolder(rootView, object: DateSelectView.OnDateSelectedListener {
            override fun onDateSelected(date: Int) {
//                this@DateSelectViewAdapter.preSelectedDate = date
                Log.d("ADD", date.toString())
            }
        })

        preDateSelectView = viewHolder.dateSelectView
        preDateSelectView?.reset()
        return viewHolder
    }

    override fun getItemCount(): Int = numOfMonth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DateSelectViewHolder, position: Int) {
        preDateSelectView?.reset()
        holder.bind(position+1)
    }

    // todo inner and internal class of Kotlin, what is the difference?
// 只允许设置今年内的事件
    class DateSelectViewHolder internal constructor(
        private val rootView: View,
        private val listener: DateSelectView.OnDateSelectedListener
    ): RecyclerView.ViewHolder(rootView) {

        private val tvMonth: TextView = rootView.findViewById(R.id.tv_month)
        //todo 惊了 这里rootView findViewById找不到
        internal val dateSelectView: DateSelectView = (rootView as ViewGroup).getChildAt(2) as DateSelectView
//    private val dateSelectView: DateSelectView = rootView.findViewById(R.id.view_date_select)

        // month表示月份 1月用数字1表示
        //fixme using joda-time instead
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(month: Int){
            tvMonth.text = num2MonthInCn(month)
            dateSelectView.loadView(month)

            dateSelectView.setOnDateSelectedListener(listener)
        }

    }

}

