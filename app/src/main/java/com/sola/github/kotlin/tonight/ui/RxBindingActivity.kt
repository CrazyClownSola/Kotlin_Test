package com.sola.github.kotlin.tonight.ui

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import com.sola.github.kotlin.tonight.R
import java.lang.ref.WeakReference

/**
 * Created by Sola
 * 2017/5/27.
 */
@Suppress("unused")
abstract class RxBindingBaseActivity : AppCompatActivity() {

    // 内部类，通常定义常量，一些供外部调用类，类似static
    companion object {
        const val TITLE_EXTRA: String = "title"
        const val MENU_EXTRA: String = "menu_id"
    }

    protected var menu_id: Int = -1
    protected lateinit var title: String
    protected lateinit var mContext: WeakReference<Context>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = WeakReference(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        when (menu_id) {
            -1 ->
                return super.onCreateOptionsMenu(menu)
            else -> {
                menuInflater.inflate(menu_id, menu)
                return true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        initView(layoutResID)
    }

    fun initView(@LayoutRes layoutResID: Int) {
        initBinding(layoutResID)
        inject_extras()
        // 这里注意一个坑，由于Kotlin是空类型安全的，然而findViewById是可能为空的
        val view = findViewById(R.id.id_tool_bar)
//        id_too

        view?.let {
            val id_tool_bar = it as Toolbar
            if (TextUtils.isEmpty(title))
                id_tool_bar.title = TITLE_EXTRA
            else
                id_tool_bar.title = title
            setSupportActionBar(id_tool_bar)
            if (supportActionBar != null)
                supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(setDisplayHomeAsUpEnable())
        }
//        val id_tool_bar: Toolbar? = findViewById(R.id.id_tool_bar) as Toolbar
//        id_tool_bar?.let {
//            if (TextUtils.isEmpty(title))
//                it.title = TITLE_EXTRA
//            else
//                it.title = title
//            setSupportActionBar(it)
//            if (supportActionBar != null)
//                supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(setDisplayHomeAsUpEnable())
//        }
        doAfterView()
    }

    open fun setDisplayHomeAsUpEnable(): Boolean {
        return true
    }

    private fun inject_extras() {
        val extras_: Bundle? = intent.extras
        if (extras_ == null)
            title = ""
        extras_?.let {
            if (it.containsKey(TITLE_EXTRA))
                title = it.getString(TITLE_EXTRA)
            if (it.containsKey(MENU_EXTRA))
                menu_id = it.getInt(MENU_EXTRA)
            initExtras(it)
        }
    }

    protected fun <T : ViewDataBinding> buildBinding(): T {
        val rootView = findViewById(android.R.id.content)
        return DataBindingUtil.bind((rootView as ViewGroup).getChildAt(0))
    }

    protected fun getContext(): Context {
        return mContext.get()!!
    }

    protected fun replaceFragment(frag: Fragment, isDestroy: Boolean, resId: Int) {
        if (supportFragmentManager != null) {
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(resId, frag)
            if (!isDestroy)
                transaction.addToBackStack(null)
            transaction.commitAllowingStateLoss()
        }
    }

    protected fun addFragment(frag: Fragment, isDestroy: Boolean, resId: Int) {
        if (supportFragmentManager != null) {
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.add(resId, frag, null)
            if (!isDestroy)
                transaction.addToBackStack(null)
            transaction.commitAllowingStateLoss()
        }
    }

    abstract fun initBinding(@LayoutRes layoutResID: Int)

    abstract fun initExtras(extras_: Bundle)

    abstract fun doAfterView()

}