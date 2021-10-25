package com.kolibreath.timetableapp

import android.content.Context

// 定义SharedPreference的Key

// 当前周数 返回 Int 从1开始
val CUR_WEEK = "CUR_WEEK"

class Preference(private val context: Context) {

    private val prefs by lazy {context.getSharedPreferences("ccnubox-android", Context.MODE_PRIVATE)}

        fun <A> get(name: String, default: A) : A = with(prefs) {
        val res: Any? = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> null // 不支持的类型
        }
        res as A
    }


     fun <A> put(name :String,value: A) = with(prefs.edit()){
        when(value){
            is Long -> putLong(name,value)
            is String -> putString(name,value)
            is Int -> putInt(name,value)
            is Boolean -> putBoolean(name,value)
            is Float -> putFloat(name,value)
            else -> null // 不支持的类型
        }?.apply()

    }

}
