package com.kolibreath.timetableapp

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

class DateSelectViewAdapter(
    private val context: Context
    ) : RecyclerView.Adapter<DateSelectViewHolder>() {

    private val numOfMonth = 12

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateSelectViewHolder {
        // 从resId加载组合而成的View
        val rootView =
            LayoutInflater.from(context).inflate(R.layout.view_date_picker, parent, false)
        return DateSelectViewHolder(rootView)
    }

    override fun getItemCount(): Int = numOfMonth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DateSelectViewHolder, position: Int) {
        holder.bind(position+1)
    }


}

// todo inner and internal class of Kotlin, what is the difference?
// 只允许设置今年内的事件
class DateSelectViewHolder internal constructor(
    val rootView: View
): RecyclerView.ViewHolder(rootView) {

    private val tvMonth: TextView = rootView.findViewById(R.id.tv_month)
    //todo 惊了 这里rootView findViewById找不到
    private val dateSelectView: DateSelectView = (rootView as ViewGroup).getChildAt(2) as DateSelectView
//    private val dateSelectView: DateSelectView = rootView.findViewById(R.id.view_date_select)

    // month表示月份 1月用数字1表示
    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(month: Int){
        tvMonth.text = num2MonthInCn(month)
        dateSelectView.loadView(month)
    }
}