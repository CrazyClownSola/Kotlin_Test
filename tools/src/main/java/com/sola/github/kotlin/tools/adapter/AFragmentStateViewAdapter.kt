package com.sola.github.kotlin.tools.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

/**
 * Created by Sola
 * 2017/6/1.
 */
@Suppress("unused")
abstract class AFragmentStateViewAdapter<T>(fm: FragmentManager, tab: Array<T>) : FragmentStatePagerAdapter(fm) {

    var cacheTabs: Array<T> = tab

    var fragments: Array<Fragment?> = arrayOfNulls<Fragment>(tab.size)

    override fun getCount(): Int = fragments.size

    override fun getItem(position: Int): Fragment {
        if (fragments[position] == null)
            fragments[position] = initFragment(cacheTabs[position])
        return fragments[position]!!
    }

    fun setFragment(relation: T, field: Fragment) {
        for (i in cacheTabs.indices) {
            if (cacheTabs[i] == relation)
                fragments[i] = field
        }
    }

    abstract fun initFragment(tab: T): Fragment

}
