package com.kolibreath.timetableapp

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.animation.TranslateAnimation
import android.widget.GridLayout
import android.widget.TextView

// 控制周数选择的View
// 在xml中，WeekSelectView设置状态为隐藏
class WeekSelectView(
    private val contxt: Context,
    private val attributeSet: AttributeSet
): GridLayout(contxt, attributeSet) {

    /**
     * 绘制中布局位置计算思路：
     * 假定一个学期最多有 7*3 = 21周，WeekSelectView距离左右两侧16dp
     * 每一个View之间相距12dp 通过以上可以计算出每一个View的大小
     */
    private val weekTextViews: Array<TextView> = Array(21){TextView(contxt)}
    // WeekSelectView 左右边距
    private val WEEK_SELECT_VIEW_MARGIN = contxt.dp2px(16)
    // WeekSelectView中间的每一个View的边距
    private val SELECTED_VIEW_MARGIN = contxt.dp2px(12).toInt()
    private val SELECTED_VIEW_HEIGHT = (contxt.getScreenWidth() - WEEK_SELECT_VIEW_MARGIN * 2) / 7 - SELECTED_VIEW_MARGIN * 2
    private val SELECTED_VIEW_WIDTH = SELECTED_VIEW_HEIGHT

    // 标记上一个选中的周数下标 默认设置为当前周
    private var lastSelectedWeekPos = Preference(contxt).get(CUR_WEEK,-1)-1

    private var onWeekSelectedListener: OnWeekSelectedListener? = null

    // slideStatus = false 表示没有出现，slideStatus = true表示下拉出现
    var slideStatus = WeekSelectViewStatus.CLOSED


    // 初始化GridLayout
    init {
        columnCount = 7
        rowCount = 3
        // 设置GridLayout的左右padding
        setPadding(contxt.dp2px(16).toInt(), 0, contxt.dp2px(16).toInt(), 0)
        // TODO 统一颜色规范
        setBackgroundColor(Color.WHITE)
        // 如果没有点击选择周数，将是隐藏的状态
        visibility = INVISIBLE

        // 初始化每一个View的位置 颜色 文字
        for(i in weekTextViews.indices) {
            val textView = weekTextViews[i]
            textView.text = "${i + 1}"
            textView.setTextColor(Color.BLACK)
            textView.gravity = Gravity.CENTER

            addView(textView, SELECTED_VIEW_HEIGHT.toInt(), SELECTED_VIEW_HEIGHT.toInt())

            val gridLayoutParams = textView.layoutParams as GridLayout.LayoutParams
            gridLayoutParams.leftMargin = SELECTED_VIEW_MARGIN
            gridLayoutParams.rightMargin = SELECTED_VIEW_MARGIN
            gridLayoutParams.topMargin = SELECTED_VIEW_MARGIN
            gridLayoutParams.bottomMargin = SELECTED_VIEW_MARGIN


            // 初始选中为当前周
            setSelectedWeek(Preference(contxt).get(CUR_WEEK,-1)-1)

            // 设置监听器
            textView.setOnClickListener {
                if (onWeekSelectedListener != null) {
                    onWeekSelectedListener!!.onWeekSelected(i+1)

                    setSelectedWeek(pos = i)
                    lastSelectedWeekPos = i
                    slideUp()
                }
            }
        }
    }

    fun slideUp() {
        slide(-contxt.dp2px(SELECTED_VIEW_HEIGHT.toInt() * 3).toInt())
        slideStatus = WeekSelectViewStatus.CLOSED
    }

    fun slideDown() {
        slide(0)
        slideStatus = WeekSelectViewStatus.OPEN
    }

    private fun slide(toY: Int) {
        val animation: TranslateAnimation = if (toY < 0) {
            TranslateAnimation(0f, 0f, 0f, toY.toFloat())
        } else {
            TranslateAnimation(
                0f,
                0f,
                -contxt.dp2px((SELECTED_VIEW_HEIGHT.toInt() + (contxt.dp2px(24)) * 3).toInt()),
                0f
            )
        }
        animation.duration = 250
        animation.fillAfter = true
        startAnimation(animation)
    }

    private fun setSelectedWeek(pos: Int){
        if (lastSelectedWeekPos !in 0..21) return

        val lastTextView = weekTextViews[lastSelectedWeekPos]
        lastTextView.setBackgroundColor(Color.WHITE)
        lastTextView.setTextColor(Color.BLACK)

        val textView = weekTextViews[pos]
        // TODO a background drawable is needed!
        textView.setBackgroundColor(contxt.resources.getColor(R.color.purple_200))
        textView.setTextColor(Color.WHITE)
    }

    fun setOnWeekSelectedListener(listener: OnWeekSelectedListener){
        onWeekSelectedListener = listener
    }

    // 回调将会把选择的具体周数（非下标传回）
    interface OnWeekSelectedListener {
        fun onWeekSelected(week:Int)
    }

    enum class WeekSelectViewStatus {
        OPEN, CLOSED
    }
}