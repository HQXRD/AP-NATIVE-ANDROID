package com.xtree.base.mvvm.recyclerview

import com.drake.brv.BindingAdapter
import kotlin.reflect.full.createType

/**
 *Created by KAKA on 2024/3/8.
 *Describe: RecyclerView适配器的封装层，这里会提供BRV的功能转化和封装操作
 */
class BaseDatabindingAdapter : BindingAdapter() {

    /**
     * 数据回调
     */
    interface onBindListener{
        /**
         * 可操作视图和adapter,最好不用
         */
        fun onBind(bindingViewHolder: BindingViewHolder)

        /**
         * item点击回调
         */
        fun onItemClick(modelPosition: Int, layoutPosition: Int)
    }

    /**
     * 数据和视图绑定
     */
    fun initData(
        datas:List<BindModel>,
        layouts: List<Int>
    ) {

        datas.distinctBy { it::class }.forEach { model ->
            val kType = model.javaClass.kotlin.createType()
            typePool[kType] = { layouts[model.itemType] }
        }

        datas.filterNot {

            if (it is BindHead || it is BindFooter) {
                if (it is BindHead) addHeader(it)
                if (it is BindFooter) addFooter(it)
                true
            } else {
                false
            }
        }.let { models = it }

    }

}