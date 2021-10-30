package com.kolibreath.timetableapp

import androidx.fragment.app.Fragment


// TODO BaseFragment中可以加上加载动画等等
// todo 如何重写从BaseFragment中继承的companion object
open  class BaseFragment: Fragment() {

    companion object {
        fun newInstance(): Fragment = BaseFragment()
    }
}