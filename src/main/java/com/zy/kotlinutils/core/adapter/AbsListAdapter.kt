package com.zy.kotlinutils.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.zy.kotlinutils.AppInterface

/**
 * Created by zy on 18-1-2.
 */
abstract class AbsListAdapter<T> : BaseAdapter() {

    val data: MutableList<T> = ArrayList()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder<T>
        if (convertView == null) {
            var view = createView(getItemViewType(position))
            holder = onCreateViewHolder(view)
            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder<T>
        }
        holder.bind(getItem(position))
        return holder.view
    }

    override fun getItem(position: Int): T {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position as Long
    }

    override fun getCount(): Int {
        return data.size
    }

    abstract fun getLayoutRes(): Int

    open fun createView(type: Int): View {
        return LayoutInflater.from(AppInterface.app).inflate(getLayoutRes(), null)
    }

    abstract fun onCreateViewHolder(view: View): ViewHolder<T>

    abstract class ViewHolder<T>(val view: View) {
        abstract fun bind(model: T)
    }
}