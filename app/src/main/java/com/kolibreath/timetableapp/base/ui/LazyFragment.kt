package com.kolibreath.timetableapp.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class LazyFragment(
    private val resId: Int
): Fragment() {

    var firstLoaded = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(resId, container, false)
        initView(rootView)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        if(firstLoaded) {
            firstLoaded = false
            initData()
            initEvent()
        }
    }

    abstract fun initView(rootView: View)

    abstract fun initData()

    abstract fun initEvent()
}