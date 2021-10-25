package com.kolibreath.timetableapp

import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)

//        val weekSelectView = findViewById<WeekSelectView>(R.id.view_week_select)
//        val onWeekSelectedListener =object: WeekSelectView.OnWeekSelectedListener{
//            override fun onWeekSelected(week: Int) {
//                Log.d("MainActivity", week.toString())
//            }
//        }
//        weekSelectView.setOnWeekSelectedListener(onWeekSelectedListener)
//
//        val tvWeekSelect = findViewById<TextView>(R.id.tv_week_select)
//        val ivWeekSelect = findViewById<ImageView>(R.id.ib_week_select)
//
//        val weekSelectListener = View.OnClickListener {
//            if (weekSelectView.slideStatus == WeekSelectView.WeekSelectViewStatus.OPEN){
//                // 当前为出现
//                weekSelectView.slideUp()
//                weekSelectView.visibility =View.INVISIBLE
//            }else {
//                weekSelectView.slideDown()
//                weekSelectView.visibility =View.VISIBLE
//            }
//        }
//
//        tvWeekSelect.setOnClickListener(weekSelectListener)
//        ivWeekSelect.setOnClickListener(weekSelectListener)

//        Preference(this).put(CUR_WEEK, getCurrWeekNum())
//        val fragment = TimeTableFragment.newInstance()
//        val fragmentManager = supportFragmentManager
//        val transaction = fragmentManager.beginTransaction()
//        transaction.add(R.id.container, fragment, "fff")
//        transaction.commit()

    }

    override fun onResume() {
        super.onResume()
        val weekTitleView = findViewById<WeekTitleView>(R.id.week_title_view)
        weekTitleView.post {
            Log.d("fuck this", "${weekTitleView.width} ${weekTitleView.height}")
        }
    }

}
