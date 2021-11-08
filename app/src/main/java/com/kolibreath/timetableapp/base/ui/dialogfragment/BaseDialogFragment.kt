package com.kolibreath.timetableapp.base.ui.dialogfragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment

abstract class BaseDialogFragment(
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

    abstract fun initWindow(window: Window?)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireActivity(), resId).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(resId)
            setCanceledOnTouchOutside(true)

            initWindow(window)
        }
    }
}