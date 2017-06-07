package com.sola.github.kotlin.tonight.view.base

import android.content.Context
import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.sola.github.kotlin.tonight.BR
import java.util.*

/**
 * Created by Sola
 * 2017/5/31.
 */
@Suppress("unused")
interface IRecyclerDelegate {

    /**
     * 构建Holder，Holder和viewType唯一绑定关系
     * 有多少个viewType，该方法被调用多少次
     */
    fun getHolder(context: Context, parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    /**
     * 布局刷新
     */
    fun refreshView(context: Context, holder: RecyclerView.ViewHolder, position: Int)

    /**
     * 获取viewType
     */
    fun getViewType(position: Int): Int

    /**
     * view被销毁的时候触发的
     */
    fun onViewRecycled()

    /**
     * 单项点击事件
     */
    fun itemClick(v: View, position: Int)
}

@Suppress("unused")
abstract class BaseView<T>(item: T) : IRecyclerDelegate {

    val viewType: Int = TypeBuilder.getInstance().generateId()

    var data: T = item

    override fun refreshView(context: Context, holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getViewType(position: Int): Int = viewType

    override fun onViewRecycled() {
    }

    override fun itemClick(v: View, position: Int) {
    }
}

@Suppress("unused") class BaseHolder : RecyclerView.ViewHolder {

    var binding: ViewDataBinding = DataBindingUtil.bind(itemView)

    constructor(itemView: View) : super(itemView) {
        binding = DataBindingUtil.bind(itemView)
    }

    constructor(itemView: View, bindingComponent: DataBindingComponent) : super(itemView) {
        binding = DataBindingUtil.bind(itemView, bindingComponent)
    }

    fun setData(obj: Any) {
        binding.setVariable(BR.data, obj)
        binding.executePendingBindings()
    }

}

@Suppress("unused")
class TypeBuilder private constructor() {
    val DEFAULT_INIT_TYPE = 0x01

    var cacheType = DEFAULT_INIT_TYPE

    val random: Random = Random()

    companion object { // 单例的一种实现方式，利用内联函数
        fun getInstance(): TypeBuilder {
            return Inner.instance
        }
    }

    private object Inner {
        var instance = TypeBuilder()
    }

    fun generateId() = ++cacheType

    fun generateRanId() = random.nextInt()

    fun generateRanId(n: Int) = random.nextInt(n)
}