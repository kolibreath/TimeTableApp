package com.kolibreath.timetableapp.schedule

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.appcompat.widget.AppCompatTextView
import com.kolibreath.timetableapp.R


// 显示课程的View
// 如果出现重复的课程使用红点表示 TODO 之后按照设计稿的思路改成对应的drawable
class CourseView(val contxt: Context): AppCompatTextView(contxt) {

    // 是否有重复的课程
    var tipOn: Boolean = false
    // 点击事件获取courseId
    var courseId = -1

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if(tipOn){
            setTip(canvas)
        }
    }

    private fun setTip(canvas: Canvas){
        // 初始化提示点：
        val hintColor = contxt.resources.getColor(R.color.hint_overlap_course)
        val density = contxt.resources.displayMetrics.density
        val radiusPx = density * 5
        val marginTopPx = density * 3
        val marginRightPx = density * 3

        val x = width - marginRightPx - radiusPx
        val y = marginTopPx + radiusPx

        // 避免重复创建paint对象，这里获取的paint是TextView中的paint
        val p = paint
        val tempColor = p.color

        p.color = hintColor
        p.style = Paint.Style.FILL
        canvas.drawCircle(x, y, radiusPx, p)

        p.color = tempColor

    }
}