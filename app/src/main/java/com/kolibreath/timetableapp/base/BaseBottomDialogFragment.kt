package com.kolibreath.timetableapp.base

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.kolibreath.timetableapp.R

// todo the lifecycle of DialogFragment
abstract class BaseBottomDialogFragment(
    private val resId: Int
): DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = layoutInflater.inflate(resId, container, false)
        initChild(rootView)
        return rootView
    }

    abstract fun initChild(rootView: View)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
       return Dialog(requireActivity(), resId).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(resId)
            setCanceledOnTouchOutside(true)

            // 设置Window样式 使得当前的Dialog从底部弹出
            window?.let {
                // 设置window弹出动画
                val lp = it.attributes.apply {
                    gravity = Gravity.BOTTOM
                    width = WindowManager.LayoutParams.MATCH_PARENT
                    height = WindowManager.LayoutParams.WRAP_CONTENT
                    dimAmount = 0.35f
                    // todo rename animation
                    windowAnimations = R.style.DatePickerDialogAnim
                }
                it.attributes = lp
                it.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            }
        }

    }
}