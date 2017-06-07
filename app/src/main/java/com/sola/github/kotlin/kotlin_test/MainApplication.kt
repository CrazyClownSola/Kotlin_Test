package com.sola.github.kotlin.kotlin_test

import android.app.Application
import com.sola.github.kotlin.kotlin_test.di.AppComponent
import com.sola.github.kotlin.kotlin_test.di.AppModule
import com.sola.github.kotlin.kotlin_test.di.DaggerAppComponent

/**
 * Created by Sola
 * 2017/5/31.
 */
class MainApplication : Application() {


    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initComponent()
    }

    fun initComponent() {
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

}