package com.kolibreath.timetableapp

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.kolibreath.timetableapp.schedule.weeklyschedule.TimeTableFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)

        Preference(this).put(CUR_WEEK, getCurrWeekNum())
        val fragment = TimeTableFragment.newInstance()
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.container, fragment, TAG_TIME_TIME_FRAGMENT)
        transaction.commit()

    }

    override fun onResume() {
        super.onResume()
    }

}
