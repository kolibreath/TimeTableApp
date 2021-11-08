package com.kolibreath.timetableapp

import android.app.Application
import android.content.Context

// todo prevent memory leaks
class MainApplication: Application() {
    companion object {
        lateinit var contxt: Context

    }

    override fun onCreate() {
        super.onCreate()
        contxt = this
    }
}