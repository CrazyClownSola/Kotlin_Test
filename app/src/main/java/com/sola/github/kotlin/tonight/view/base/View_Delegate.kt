@file: Suppress("unused")

package com.sola.github.kotlin.tonight.view.base

import android.content.Context
import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.View
import com.sola.github.kotlin.tonight.BR
import com.sola.github.kotlin.tools.delegate.IRecyclerDelegate
import java.util.*

/**
 * Created by Sola
 * 2017/5/31.
 * 界面呈现基类，基本所有界面呈现可以通用这个类
 */
abstract class BaseView<T : Any>(item: T) : IRecyclerDelegate {

    val viewType: Int = TypeBuilder.getInstance().generateId()

    var data: T = item

    override fun refreshView(context: Context, holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BaseHolder)
            holder.setData(data)
    }

    override fun getViewType(position: Int): Int = viewType

    override fun onViewRecycled() {
    }

    override fun itemClick(v: View, position: Int) {
    }
}

/**
 * 适配BaseView的所对应的Holder 类，内部采用DataBinding的方式进行代码构建
 */
class BaseHolder : RecyclerView.ViewHolder {

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