package com.example.circularrecyclerview

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.simple_text_recycler_item.view.*

/**
 * Created by kranthi.polimetla on 7/25/19.
 */

class CircularRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    private var onValueChangedListener: (value: Any) -> Unit = {}


    init {
        PagerSnapHelper().attachToRecyclerView(this)
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_IDLE) {
                    val position = getCurrentItem()
                    onValueChangedListener(position)
                }
            }
        })
    }

    fun setPickerAdapter(adapter: CircularRecyclerAdapter, items: List<Any>, initialPosition: Int) {
        adapter.setItems(items)
        this.adapter = adapter
        val layoutManager = LinearLayoutManager(context).apply {
            orientation = VERTICAL
            if (initialPosition != 0) {
                scrollToPosition((Int.MAX_VALUE / 2) - ((Int.MAX_VALUE / 2) % items.size) + (initialPosition - 1))
            } else {
                scrollToPosition((Int.MAX_VALUE / 2) - ((Int.MAX_VALUE / 2) % items.size))
            }
        }
        this.layoutManager = layoutManager
    }


    fun setCustomPickerValue(actualPosition: Int, originalItemCount: Int) {
        val recyclerPosition = (Int.MAX_VALUE / 2) - ((Int.MAX_VALUE / 2) % originalItemCount) + actualPosition - 1
        scrollToPosition(recyclerPosition)
    }

    private fun getCurrentItem(): Int {
        return (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
    }

    fun setOnValueChangedListener(listener: (value: Any) -> Unit) {
        this.onValueChangedListener = listener
    }
}

open class CircularRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemPosition = position % pickerItems.size
        val item = pickerItems[itemPosition]
        if (holder is CircularRecyclerHolder) holder.bind(item)
    }

    open var pickerItems: List<Any> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircularRecyclerHolder {
        return CircularRecyclerHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.simple_text_recycler_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    fun setItems(items: List<Any>) {
        pickerItems = items
    }
}

open class CircularRecyclerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    open fun bind(item: Any) {
        itemView.simpleTextView.text = item.toString()
    }
}