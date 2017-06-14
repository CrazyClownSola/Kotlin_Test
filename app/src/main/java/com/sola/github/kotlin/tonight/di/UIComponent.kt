package com.sola.github.kotlin.tonight.di

import android.databinding.BindingAdapter
import android.databinding.DataBindingComponent
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
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

    @BindingAdapter(value = *arrayOf("adapter"), requireAll = false)
    fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>)

    @BindingAdapter(value = *arrayOf("pic", "resId", "empty", "error"), requireAll = false)
    fun setImageUrl(view: ImageView, url: String?, @DrawableRes resId: Int, empty: Drawable?, error: Drawable?)

}

class DefaultBindingAdapter @Inject constructor() : IBindAdapter {
    override fun setImageUrl(view: ImageView, url: String?, resId: Int, empty: Drawable?, error: Drawable?) {
        if (!url.isNullOrEmpty()) {
            val creator: RequestCreator = Picasso.with(view.context).load(url)
            if (error != null)
                creator.error(error)
            creator.into(view)
        } else if (resId > 0) {
            val creator: RequestCreator = Picasso.with(view.context).load(resId)
            if (error != null)
                creator.error(error)
            creator.into(view)
        } else if (empty != null)
            view.setImageDrawable(empty)

    }

    override fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        view.adapter = adapter
    }

    override fun setPagerAdapter(viewPager: ViewPager, adapter: PagerAdapter) {
        viewPager.adapter = adapter
    }

}