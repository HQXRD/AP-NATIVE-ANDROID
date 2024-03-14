package com.xtree.base.mvvm

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter
import com.xtree.base.mvvm.recyclerview.BindModel
import com.xtree.base.widget.FilterView
import com.xtree.base.widget.impl.FilterViewOnClickListerner

/**
 *Created by KAKA on 2024/3/8.
 *Describe: MVVM扩展函数
 */

@BindingAdapter(
    value = ["layoutManager", "itemData", "itemViewType", "onBindListener", "dividerDrawableId"],
    requireAll = false
)
fun RecyclerView.initData(
    layoutManager: RecyclerView.LayoutManager?,
    itemData: List<BindModel>,
    itemViewType: List<Int>,
    onBindListener: BaseDatabindingAdapter.onBindListener?,
    dividerDrawableId: Int?,
) {

    adapter?.run {

        if (itemData == models) {
            models = itemData
        } else {
            (bindingAdapter as BaseDatabindingAdapter).run {
                clearHeader(false)
                clearFooter(false)
                initData(itemData, itemViewType)
            }
        }
    } ?: run {
        when (layoutManager) {
            null -> linear()
            else -> this.layoutManager = layoutManager
        }

        dividerDrawableId?.let { divider {
            setDrawable(it)
            startVisible = false
            endVisible = true
        } }

        val mAdapter = BaseDatabindingAdapter().run {
            initData(itemData, itemViewType)
            onBind {
                onBindListener?.onBind(this)

                itemView.rootView.setOnClickListener {
                    onBindListener?.onItemClick(modelPosition, layoutPosition)
                }

//                 itemDifferCallback = object : ItemDifferCallback {
//
//                     override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
//                         return oldItem == newItem
//                     }
//
//                     override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
//                         return (oldItem as BindModel).getItemId() == (newItem as BindModel).getItemId()
//                     }
//
//                     override fun getChangePayload(oldItem: Any, newItem: Any): Any? {
//                         return true
//                     }
//                 }
            }
            adapter = this
        }
    }
}

@BindingAdapter(
    value = ["typeData", "statuData", "queryListener"],
    requireAll = false
)
fun FilterView.initData(
    typeData: List<FilterView.IBaseVo>?,
    statuData: List<FilterView.IBaseVo>,
    queryListener: FilterViewOnClickListerner?
) {
    setData(typeData, statuData)
    queryListener?.let { setQueryListener(it) }
}

@BindingAdapter(
    value = ["setSelectedListener", "tabs"],
    requireAll = false
)
fun TabLayout.init(setSelectedListener: OnTabSelectedListener, tabs: List<String>) {
    for (tab in tabs) {
        addTab(newTab().apply { text = tab })
    }
    addOnTabSelectedListener(setSelectedListener)
}