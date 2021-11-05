package com.kolibreath.timetableapp

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager

// 和View相关的工具类 （尺寸处理，屏幕长宽获取信息等等）

fun Context.dp2px(dpValue: Int): Float = resources.displayMetrics.density * dpValue

fun Context.px2dp(pxValue: Int): Float = pxValue / resources.displayMetrics.density

fun Context.dp2sp(dpValue: Int): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue.toFloat(), this.resources.displayMetrics)
}

fun Context.getScreenWidth(): Int {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val displayMetrics = DisplayMetrics()
    if (Build.VERSION.SDK_INT < 30) {
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)
    }else{
        this.display!!.getRealMetrics(displayMetrics)
    }
    return displayMetrics.widthPixels
}

fun Context.getScreenHeight(): Int {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val displayMetrics = DisplayMetrics()
    if (Build.VERSION.SDK_INT < 30) {
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)
    }else{
        this.display!!.getRealMetrics(displayMetrics)
    }
    return displayMetrics.heightPixels
}


// 获取状态栏高度
// 主要思想是通过DecorView获取
fun Context.getStatusBar(): Int {
    val rect = Rect()
    // TODO 先暂时假设这个方法使用在Activity中
    val activity = this as Activity
    val window = activity.window
    window.decorView.getWindowVisibleDisplayFrame(rect)
    return rect.top
}
