@file:Suppress("unused")

package com.sola.github.kotlin.tools.delegate

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 * Created by Sola
 * 2016/12/20.
 */
interface IRecyclerDelegate {

    /**
     * 构建Holder

     * @param context  context容器
     * *
     * @param parent   父类布局
     * *
     * @param viewType 布局类型
     * *
     * @return 返回RecyclerView需要的ViewHolder
     */
    fun getHolder(context: Context, parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    /**
     * 刷新布局

     * @param context  context容器
     * *
     * @param holder   view容器
     * *
     * @param position 序列位置
     */
    fun refreshView(context: Context, holder: RecyclerView.ViewHolder, position: Int)


    /**
     * 注意该方法的使用，主要区分在于当RecyclerView的Item项，每一项都不相同的时候，
     * 可以根据不同的ViewType进行界面的适配;
     * 但是当每个项类型都相同的时候，这个方法返回的值就不那么重要了

     * @param position 在适配器中的位置,注意这个参数在作为Header加入的时候这个值为-1
     * *
     * @return 范围该项所对应的布局类型
     */
    fun getViewType(position: Int): Int

    /**
     * 当对应位置的Holder被销毁的时候触发
     */
    fun onViewRecycled()

    /**
     * item点击事件

     * @param v 点击的view
     */
    fun itemClick(v: View, position: Int)
}

interface IListItem {

    /**
     * @param context     　控件组件
     * *
     * @param convertView 　注意这个东西的使用，用的时候有可能传入的为空，同样传入的不为空，为了更好的使用内存效应，请时候懒加载的模式
     */
    fun getView(context: Context, convertView: View, parent: ViewGroup): View

}
