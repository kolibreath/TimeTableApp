package com.kolibreath.timetableapp.base.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout

// 实现水波纹效果 禁止消息向下传递
class RippleLayout(
    private val contxt: Context,
    private val attributeSet: AttributeSet
): ConstraintLayout(contxt, attributeSet) {

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }
}