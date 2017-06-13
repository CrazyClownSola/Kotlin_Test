package com.sola.github.kotlin.tonight

import android.app.Application
import android.databinding.DataBindingUtil
import com.sola.github.kotlin.tonight.di.*

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
        DataBindingUtil.setDefaultComponent(DaggerDefaultUIComponent.create())
    }

}