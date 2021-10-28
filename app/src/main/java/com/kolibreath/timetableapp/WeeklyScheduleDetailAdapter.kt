package com.kolibreath.timetableapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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
        addButtons(belowId, rootView as RelativeLayout, {}, {})

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
        addButtons(belowId, rootView as RelativeLayout, {}, {})

        tvDescription.text = detail.description
        tvLocation.text = detail.location
        tvWeek.text = detail.week
        tvType.text = detail.type
    }

    /**
     * 动态向Item中添加两个Button，因为不确定是否需要显示note
     */
    private fun addButtons(belowId: Int,
                           rootView: RelativeLayout,
                           editCallback: (View)-> Unit,
                           deleteCallback: (View) -> Unit ){

        val context = rootView.context
        // 垂直方向上的margin
        val vertMargin = context.dp2px(16).toInt()
        // 布局类似于:
        // margin editButton margin margin deleteButton margin
        val margin = context.dp2px(20).toInt()
        val width = ((context.getScreenWidth() - 4 * margin) / 2).toInt()

        // edit button
        val editButton = Button(rootView.context).apply { setOnClickListener(editCallback); text = "编辑" }
        val editLayoutParams = RelativeLayout.LayoutParams(width, RelativeLayout.LayoutParams.WRAP_CONTENT).apply {
            addRule(RelativeLayout.BELOW, belowId)
            leftMargin = margin
            this.topMargin = vertMargin
            bottomMargin = vertMargin
        }
        rootView.addView(editButton, editLayoutParams)

        // delelte button
        val deleteButton = Button(rootView.context).apply { setOnClickListener(deleteCallback); text = "删除"}
        val deleteLayoutParams = RelativeLayout.LayoutParams(width, RelativeLayout.LayoutParams.WRAP_CONTENT).apply {
            addRule(RelativeLayout.BELOW, belowId)
            leftMargin = margin + width + margin + margin
            this.topMargin = vertMargin
            bottomMargin = vertMargin
        }
        rootView.addView(deleteButton, deleteLayoutParams)
    }


}