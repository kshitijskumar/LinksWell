package com.example.linkswell

import android.app.Application
import com.example.linkswell.di.AppComponent
import com.example.linkswell.di.DaggerAppComponent

class LinksWellApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .applicationContext(applicationContext)
            .build()
    }
}