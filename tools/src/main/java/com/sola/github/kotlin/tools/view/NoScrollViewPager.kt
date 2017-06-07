package com.sola.github.kotlin.tools.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by Sola
 * 2017/6/1.
 */
class NoScrollViewPager : ViewPager {

    var isScrollEnable: Boolean = false

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attributes: AttributeSet) : super(context, attributes)

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return isScrollEnable && super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return isScrollEnable && super.onInterceptTouchEvent(ev)
    }

}