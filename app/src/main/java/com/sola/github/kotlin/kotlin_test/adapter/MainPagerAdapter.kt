package com.sola.github.kotlin.kotlin_test.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.sola.github.kotlin.kotlin_test.enums.EMain
import com.sola.github.kotlin.tools.adapter.AFragmentStateViewAdapter

/**
 * Created by Sola
 * 2017/6/1.
 */
@Suppress("unused")
class MainPagerAdapter(fm: FragmentManager, tab: Array<EMain>)
    : AFragmentStateViewAdapter<EMain>(fm, tab) {

    lateinit var bundle: Bundle

    constructor(fm: FragmentManager, bundle: Bundle, tab: Array<EMain>) : this(fm, tab) {
        this.bundle = bundle
    }

    override fun initFragment(tab: EMain): Fragment {
        return Fragment()
    }

    override fun getPageTitle(position: Int): CharSequence {
        return cacheTabs[position].title
    }

}