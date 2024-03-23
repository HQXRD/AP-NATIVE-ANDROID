package com.xtree.base.mvvm

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
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
fun RecyclerView.init(
    layoutManager: RecyclerView.LayoutManager?,
    itemData: List<BindModel>?,
    itemViewType: List<Int>?,
    onBindListener: BaseDatabindingAdapter.onBindListener?,
    dividerDrawableId: Int?,
) {

    if (itemData == null || itemViewType == null) {
        return
    }

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
                onBindListener?.onBind(this,this.itemView.rootView, getItemViewType())

                itemView.rootView.setOnClickListener {
                    onBindListener?.onItemClick(modelPosition, layoutPosition, getItemViewType())
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

@BindingAdapter(
    value = ["onRefreshLoadMoreListener","onLoadMoreListener"],
    requireAll = false
)
fun SmartRefreshLayout.init(onRefreshLoadMoreListener: OnRefreshLoadMoreListener?,onLoadMoreListener: OnLoadMoreListener?) {
    onRefreshLoadMoreListener?.let { setOnRefreshListener(it) }
    onLoadMoreListener?.let { setOnLoadMoreListener(it) }
}

@BindingAdapter(
    value = ["textChangedListener"],
    requireAll = false
)
fun EditText.init(textChangedListener:TextWatcher?) {
    textChangedListener?.let { addTextChangedListener(it) }
}

@BindingAdapter(
value = ["imageUrl", "placeholder", "error", "fallback", "loadWidth", "loadHeight", "cacheEnable"],
requireAll = false
)
fun setImageUrl(
    view: ImageView,
    source: Any? = null,
    placeholder: Drawable? = null,
    error: Drawable? = null,
    fallback: Drawable? = null,
    width: Int? = -1,
    height: Int? = -1,
    cacheEnable: Boolean? = true
) {
// 计算位图尺寸，如果位图尺寸固定，加载固定大小尺寸的图片，如果位图未设置尺寸，那就加载原图，Glide加载原图时，override参数设置 -1 即可。
    val widthSize = (if ((width ?: 0) > 0) width else view.width) ?: -1
    val heightSize = (if ((height ?: 0) > 0) height else view.height) ?: -1
    // 根据定义的 cacheEnable 参数来决定是否缓存
    val diskCacheStrategy = if (cacheEnable == true) DiskCacheStrategy.AUTOMATIC else DiskCacheStrategy.NONE
    // 设置编码格式，在Android 11(R)上面使用高清无损压缩格式 WEBP_LOSSLESS ， Android 11 以下使用PNG格式，PNG格式时会忽略设置的 quality 参数。
    val encodeFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Bitmap.CompressFormat.WEBP_LOSSLESS else Bitmap.CompressFormat.PNG
    Glide.with(view.context)
        .asDrawable()
        .load(source)
        .placeholder(placeholder)
        .error(error)
        .fallback(fallback)
        .thumbnail(0.33f)
        .override(widthSize, heightSize)
        .skipMemoryCache(false)
        .sizeMultiplier(0.5f)
        .format(DecodeFormat.PREFER_ARGB_8888)
        .encodeFormat(encodeFormat)
        .encodeQuality(80)
        .diskCacheStrategy(diskCacheStrategy)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(view)
}