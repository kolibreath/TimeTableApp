package com.kolibreath.timetableapp

import androidx.fragment.app.Fragment


// TODO BaseFragment中可以加上加载动画等等
open  class BaseFragment: Fragment() {

    companion object {
        fun newInstance(): Fragment = BaseFragment()
    }
}