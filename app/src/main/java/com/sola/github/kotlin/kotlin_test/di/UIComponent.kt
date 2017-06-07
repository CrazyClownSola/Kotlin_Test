package com.sola.github.kotlin.kotlin_test.di

import android.databinding.BindingAdapter
import android.databinding.DataBindingComponent
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Inject

/**
 * Created by Sola
 * 2017/6/1.
 */
@BindingType
@Component(modules = arrayOf(
        DefaultUIComponent.DefaultUIModule::class
))
interface DefaultUIComponent : DataBindingComponent {

    // 这里并不是很理解，如果不继承该方法，dagger2会报错无法根据对应的Module生成对应的BindingAdapter实例
    override fun getIBindAdapter(): IBindAdapter

    @Module
    class DefaultUIModule {

        @Provides
        @BindingType
        fun getIBindAdapter(): IBindAdapter {
            return DefaultBindingAdapter()
        }
    }
}

interface IBindAdapter {

    @BindingAdapter(value = *arrayOf("pagerAdapter"), requireAll = false)
    fun setPagerAdapter(viewPager: ViewPager, adapter: PagerAdapter)

}

class DefaultBindingAdapter @Inject constructor() : IBindAdapter {

    override fun setPagerAdapter(viewPager: ViewPager, adapter: PagerAdapter) {
        viewPager.adapter = adapter
    }

}