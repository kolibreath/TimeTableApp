package com.kolibreath.timetableapp.base.ui.dialogfragment

import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.kolibreath.timetableapp.R

// todo the lifecycle of DialogFragment
abstract class BaseBottomDialogFragment(
    private val resId: Int
): BaseDialogFragment(resId) {

    override fun initWindow(window: Window?) {
        // 设置Window样式 使得当前的Dialog从底部弹出
        window?.let {
            // 设置window弹出动画
            val lp = it.attributes.apply {
                gravity = Gravity.BOTTOM
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
                dimAmount = 0.35f
                windowAnimations = R.style.BaseBottomDialogFragmentAnim
            }
            it.attributes = lp
            it.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
    }

}