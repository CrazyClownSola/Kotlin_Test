@file:Suppress("unused")

package com.sola.github.kotlin.tools.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.SparseIntArray
import android.view.View
import android.view.ViewGroup
import com.sola.github.kotlin.tools.delegate.IRecyclerDelegate
import com.sola.github.kotlin.tools.delegate.OnRecyclerItemDestroyListener
import com.sola.github.kotlin.tools.utils.SolaLogger
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by Sola
 * 2017/6/12.
 */
open class RecyclerBaseAdapter<Param : IRecyclerDelegate>
// MutableCollection，Kotlin将集合分为可变集合和不可变集合，Mutable是可变的，考虑到Adapter当中的数据存在多变性
(context: Context, data: Collection<Param>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(), SolaLogger {

    val mContext: WeakReference<Context> = WeakReference(context)

    val typeRelationship: SparseIntArray = SparseIntArray()

    var listener: OnRecyclerItemClickListener<Param>? = null

    var list: MutableList<Param> = data.toMutableList()

    constructor(
            context: Context) :
            this(context, Collections.emptyList()) {
        this.listener = listener
    }

    constructor(
            context: Context,
            list: Collection<Param>,
            listener: OnRecyclerItemClickListener<Param>) :
            this(context, list) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val delegate = getItemByViewType(viewType) ?:
                throw NullPointerException("can not find viewHolder with viewType[$viewType]")
        delegate.let {
            return delegate.getHolder(mContext.get()!!, parent!!, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (position < 0 || position >= list.size)
            return
        val item = list.elementAt(position)
        holder?.let {
            item.refreshView(mContext.get()!!, it, position)
            it.itemView.setOnClickListener {
                if (listener == null)
                    item.itemClick(it, position)
                else
                    listener!!.onClick(it, item)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        if (list.isEmpty())
            return -1
        else {
            val viewType = list.elementAt(position).getViewType(position)
            if (typeRelationship.indexOfKey(viewType) < 0)
                typeRelationship.put(viewType, position)
            return viewType
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder?) {
        holder?.let {
            val position = holder.adapterPosition
            if (position != -1)
                list.elementAt(position).onViewRecycled()
        }
    }

    open fun getItemPosition(position: Int): Param? = list.elementAtOrNull(position)

    fun getItemByViewType(viewType: Int): Param? {
        val index = typeRelationship.get(viewType)
        if (index < 0)
            return null
        else
            return list.elementAt(index)
    }

    fun refreshList(param: Collection<Param>) {
        destroyAll()
        list.addAll(param)
        notifyDataSetChanged()
    }

    fun refreshList(param: Param) {
        destroyAll()
        list.add(param)
        notifyDataSetChanged()
    }

    fun addItem(item: Param) {
        list.add(item)
        notifyItemInserted(list.indexOf(item))
    }

    fun addItem(item: Param, position: Int) {
        list.add(position, item)
        notifyItemInserted(position)
    }

    fun addItems(item: Collection<Param>) {
        val start: Int
        if (itemCount > 0)
            start = list.size - 1
        else
            start = 0
        list.addAll(item)
        notifyItemRangeChanged(start, item.size)
    }

    fun destroyAll() {
        if (list.isEmpty())
            return
        list.forEach {
            if (it is OnRecyclerItemDestroyListener)
                it.onDestroy()
        }
        list.removeAll { _ -> true }
        typeRelationship.clear()
    }

}

interface OnRecyclerItemClickListener<in Param> {
    fun onClick(v: View, dto: Param)
}

class RecyclerComplexBaseAdapter<
        Param : IRecyclerDelegate,
        Header : IRecyclerDelegate,
        Footer : IRecyclerDelegate>
(context: Context, data: Collection<Param>) :
        RecyclerBaseAdapter<Param>(context, data) {

    var headers: MutableList<Header> = Collections.emptyList()
    var footers: MutableList<Footer> = Collections.emptyList()


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (checkInHeader(position)) {
            holder?.let {
                val delegate = headers[position]
                delegate.refreshView(mContext.get()!!, it, position)
                it.itemView.setOnClickListener { v -> delegate.itemClick(v, position) }
            }
        } else if (checkInFooter(position)) {
            holder?.let {
                val delegate = footers[position - getFooterStartPosition()]
                delegate.refreshView(mContext.get()!!, it, position)
                it.itemView.setOnClickListener { v -> delegate.itemClick(v, position) }
            }
        } else
            super.onBindViewHolder(holder, position)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder?) {
        holder?.let {
            val position = it.adapterPosition
            if (position != -1) {
                if (checkInHeader(position))
                    headers[position].onViewRecycled()
                else if (checkInFooter(position))
                    footers[position - getFooterStartPosition()].onViewRecycled()
                else
                    list[getTruePosition(position)].onViewRecycled()
            }
        }
    }

    override fun getItemCount(): Int = headers.size + itemCount + footers.size

    override fun getItemViewType(position: Int): Int {
        if (checkInHeader(position)) {
            return headers[position].getViewType(position)
        } else if (checkInFooter(position)) {
            return footers[position - getFooterStartPosition()].getViewType(position)
        } else
            return super.getItemViewType(getTruePosition(position))
    }

    override fun getItemPosition(position: Int): Param? {
        return super.getItemPosition(getTruePosition(position))
    }

    fun getTruePosition(position: Int) = position - headers.size

    fun getFooterStartPosition() = headers.size + itemCount

    fun checkInHeader(position: Int) = position < headers.size

    fun checkInFooter(position: Int) = position >= getFooterStartPosition()

    fun addHeaderView(header: Header) {
        headers.add(header)
        notifyHeaderViews()
    }

    fun addHeaderView(position: Int, header: Header) {
        if (headers.size > position) {
            val oldItem = headers[position]
            headers.add(position, header)
            headers.add(position + 1, oldItem)
        } else
            headers.add(header)
        notifyHeaderViews()
    }

    fun addFooterView(footerView: Footer) {
        footers.add(footerView)
        notifyItemInserted(headers.size + itemCount)
    }

    fun removeFooterView(footerView: Footer) {
        val indexToRemove = footers.indexOf(footerView)
        if (indexToRemove >= 0) {
            footers.removeAt(indexToRemove)
            notifyItemRemoved(getFooterStartPosition() + indexToRemove)
        }
    }

    fun notifyHeaderViews() {
        headers.forEachIndexed { index, _ -> notifyItemChanged(index) }
    }
}