package com.example.spendwise

import android.app.Application
import com.example.spendwise.data.AppContainer
import com.example.spendwise.data.DefaultAppContainer

class SpendWiseApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}