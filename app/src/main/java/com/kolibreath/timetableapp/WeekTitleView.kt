package com.kolibreath.timetableapp

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView

/**
 * 显示月份中日期对应的周数从周日开始
 * 日 一 二 三 四 五 六
 */
class WeekTitleView(
    private val contxt: Context,
    private val attributeSet: AttributeSet
    ): LinearLayout(contxt, attributeSet) {

    private val tvDaysOfWeek = Array<TextView>(7){TextView(contxt)}
    private val dayString = arrayOf("日", "一", "二", "三", "四", "五", "六")

    // 左右边距
    private val TITLE_VIEW_MARGIN = contxt.dp2px(3)
    // WeekSelectView中间的每一个View的边距
    private val INNER_MARGIN = contxt.dp2px(12).toInt()
    private val INNER_TEXT_WIDTH = (contxt.getScreenWidth() - TITLE_VIEW_MARGIN * 2) / 7 - INNER_MARGIN * 2
    private val INNER_TEXT_HEIGHT = INNER_TEXT_WIDTH

    init {
        orientation = HORIZONTAL

        // 设置每一个TextView
        for((i, textView) in tvDaysOfWeek.withIndex()) {
            this.addView(textView, INNER_TEXT_WIDTH.toInt(), INNER_TEXT_HEIGHT.toInt())

            val layoutParams = textView.layoutParams as LinearLayout.LayoutParams

            layoutParams.leftMargin = INNER_MARGIN
            layoutParams.rightMargin = INNER_MARGIN
            layoutParams.topMargin = INNER_MARGIN
            layoutParams.bottomMargin = INNER_MARGIN

            textView.layoutParams = layoutParams
            // todo define color in color.xml
            textView.setTextColor(Color.BLACK)
            textView.gravity = Gravity.CENTER
            textView.text = dayString[i]

        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
             MeasureSpec.getSize(widthMeasureSpec),
            (INNER_TEXT_HEIGHT + TITLE_VIEW_MARGIN * 2).toInt())
    }
}