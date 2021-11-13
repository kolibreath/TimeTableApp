package com.kolibreath.timetableapp.schedule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.kolibreath.timetableapp.R
import com.kolibreath.timetableapp.WeeklyScheduleDetail
import com.kolibreath.timetableapp.dp2px

/**
 * 加载详情类型的Adapter 主要有三种类型：
 * 1. 课程
 * 2. 日程
 * 3. 课程和日程之间的divider position为单数
 */
class WeeklyScheduleDetailAdapter(
    private val contxt: Context,
    private val details: ArrayList<WeeklyScheduleDetail>
): RecyclerView.Adapter<WeeklyDetailViewHolder>() {

    // 添加了divider的details
    private val genDetails = generateDetails()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyDetailViewHolder {
        val resId = when(viewType){
            // divider
            -1 -> R.layout.item_divider
            0  -> R.layout.item_course_detail
            1  -> R.layout.item_schedule_detail
            else -> -1 // 不会出现的情况
        }
        // todo 这里是通过View#setTag的方式还是通过ViewHolder传值比较好呢？
        val rootView = LayoutInflater.from(contxt).inflate(resId, parent, false)
        return WeeklyDetailViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: WeeklyDetailViewHolder, position: Int) {
        when(genDetails[position].detailType) {
            0 -> holder.bindCourseView(genDetails[position])
            1 -> holder.bindScheduleView(genDetails[position])
            else -> {}
        }
    }

    override fun getItemCount(): Int = genDetails.size
    /**
     * 有三种类型的item
     */
    override fun getItemViewType(position: Int): Int = genDetails[position].detailType

    /**
     * 将Divider添加到details list中
     */
    private fun generateDetails(): ArrayList<WeeklyScheduleDetail>{
        if(details.size == 1) return details
        val newDetails = ArrayList<WeeklyScheduleDetail>()
        for(detail in details) {
            newDetails.add(detail)
            newDetails.add(
                WeeklyScheduleDetail(
                    detailType = -1,
                    location = "",
                    time = "",
                    week = ""
                )
            )
        }
        // 剔除最后一个
        newDetails.removeLast()
        return newDetails
    }
}

class WeeklyDetailViewHolder(
    private val rootView: View
): RecyclerView.ViewHolder(rootView) {

    internal fun bindCourseView( detail: WeeklyScheduleDetail) {
        val tvCourseName = rootView.findViewById<TextView>(R.id.tv_course_name)
        val tvLocation = rootView.findViewById<TextView>(R.id.tv_location)
        val tvTime = rootView.findViewById<TextView>(R.id.tv_course_time)
        val tvWeek = rootView.findViewById<TextView>(R.id.tv_week)
        val tvTeacher = rootView.findViewById<TextView>(R.id.tv_course_teacher)

        // 如果当前detail设置了备注
        if(detail.note.isNotEmpty()) {
            val tvNote = rootView.findViewById<TextView>(R.id.tv_note).apply {
                visibility = View.VISIBLE
                text = detail.note
            }
            val ibNote = rootView.findViewById<ImageButton>(R.id.ib_note).apply { visibility = View.VISIBLE }
        }

        val belowId = if(detail.note.isNotEmpty()) R.id.tv_note else R.id.tv_course_teacher
        // todo edit and delete callbacks
        addButtons(belowId, rootView as ConstraintLayout, {}, {})


        tvCourseName.text = detail.name
        tvLocation.text = detail.location
        tvTime.text = detail.time
        tvWeek.text = detail.week
        tvTeacher.text = detail.teacher
    }

    internal fun bindScheduleView(detail: WeeklyScheduleDetail) {
        val tvDescription = rootView.findViewById<TextView>(R.id.tv_description)
        val tvLocation = rootView.findViewById<TextView>(R.id.tv_location)
        val tvWeek = rootView.findViewById<TextView>(R.id.tv_time)
        val tvType = rootView.findViewById<TextView>(R.id.tv_type)

        // 如果当前detail设置了备注
        if(detail.note.isNotEmpty()) {
            val tvNote = rootView.findViewById<TextView>(R.id.tv_note).apply {
                visibility = View.VISIBLE
                text = detail.note
            }
            val ibNote = rootView.findViewById<ImageButton>(R.id.ib_note).apply { visibility = View.VISIBLE }
        }

        val belowId = if(detail.note.isNotEmpty()) R.id.tv_note else R.id.tv_type
        // todo edit and delete callbacks
        addButtons(belowId, rootView as ConstraintLayout, {}, {})

        tvDescription.text = detail.description
        tvLocation.text = detail.location
        tvWeek.text = detail.week
        tvType.text = detail.type
    }

    /**
     * 动态向Item中添加两个Button，因为不确定是否需要显示note
     */
    private fun addButtons(belowId: Int,
                           rootView: ConstraintLayout,
                           editCallback: (View)-> Unit,
                           deleteCallback: (View) -> Unit ){

        val vertMargin = rootView.context.dp2px(24).toInt()
        val params = ConstraintLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT).apply {
            topToBottom = belowId
            leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
            this.topMargin = vertMargin
            bottomMargin = vertMargin
        }
        val buttonsLayout = LayoutInflater.from(rootView.context).inflate(R.layout.layout_buttons, null, false)
        buttonsLayout.findViewById<TextView>(R.id.btn_cancel).apply {
            text = "删除"
            setOnClickListener(deleteCallback)
        }
         buttonsLayout.findViewById<TextView>(R.id.btn_confirm).apply {
            text = "编辑"
            setOnClickListener(editCallback)
        }
        rootView.addView(buttonsLayout, params)
    }


}