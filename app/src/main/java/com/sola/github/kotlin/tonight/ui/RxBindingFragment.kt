package com.sola.github.kotlin.tonight.ui

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sola.github.kotlin.tools.utils.SolaLogger

/**
 * Created by Sola
 * 2017/6/7.
 */
abstract class RxBindingFragment : Fragment(), SolaLogger {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(setContentView(), container, false)
        inject_binding(view)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doAfterView()
    }

    fun <T : ViewDataBinding> buildBinding(view: View): T = DataBindingUtil.bind(view)

    @LayoutRes
    abstract fun setContentView(): Int

    abstract fun inject_binding(view: View?)

    abstract fun doAfterView()
}