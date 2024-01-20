package com.achmea.demo

import android.app.Application
import com.achmea.demo.di.AppModule
import com.achmea.demo.di.AppModuleImpl

class MyApplication : Application() {

    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
    }
}