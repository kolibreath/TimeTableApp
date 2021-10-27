package com.kolibreath.timetableapp

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.Window
import android.view.WindowManager

/**
 * 从底部弹出的Dialog基类
 */
abstract class BaseBottomDialog(
    private val contxt: Context
): Dialog(contxt) {

    init {
        val window = (this.window as Window).apply { requestWindowFeature(Window.FEATURE_NO_TITLE) }
        onCreate(contxt)

        window.decorView.setPadding(0, 0, 0, 0)
        val params: WindowManager.LayoutParams = window.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.windowAnimations = R.style.BottomInAndOutStyle
        params.gravity = Gravity.BOTTOM

        window.attributes = params
        setBackgroundDrawableRes(android.R.color.transparent, window)
    }

    abstract fun onCreate(context: Context)
    abstract fun setBackgroundDrawableRes(resId: Int, window: Window)
}