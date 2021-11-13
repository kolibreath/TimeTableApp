package com.kolibreath.timetableapp.base.ui

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kolibreath.timetableapp.R

// 需要使用返回箭头 + title的公用父类
abstract class ToolbarActivity(
    private val resId: Int,
    private val titleTextResId: Int
): AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(resId)
        val titleTextView = findViewById<TextView>(R.id.tv_general_title)
        titleTextView.text = resources.getString(titleTextResId)

        val button = findViewById<ImageButton>(R.id.ib_back)
        button.setOnClickListener { finish() }
    }
}