package com.xtree.base.mvvm

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.divider
import com.drake.brv.utils.dividerSpace
import com.drake.brv.utils.grid
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.GONE
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter
import com.xtree.base.mvvm.recyclerview.BindModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 *Created by KAKA on 2024/3/8.
 *Describe: MVVM扩展函数
 */

@BindingAdapter(
    value = ["layoutManager", "itemData", "itemViewType", "onBindListener", "dividerDrawableId", "viewPool","spanCount","gridSpace"],
    requireAll = false
)
fun RecyclerView.init(
    layoutManager: RecyclerView.LayoutManager?,
    itemData: List<BindModel>?,
    itemViewType: List<Int>?,
    onBindListener: BaseDatabindingAdapter.onBindListener?,
    dividerDrawableId: Int?,
    viewPool: RecyclerView.RecycledViewPool?,
    spanCount: Int?,
    gridSpace: Float?
) {

    if (itemData == null || itemViewType == null) {
        return
    }

    viewPool?.let { setRecycledViewPool(it) }

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
            null -> if (this.layoutManager == null) spanCount?.run { grid(spanCount) } ?: run { linear() }
            else -> this.layoutManager = layoutManager
        }

        dividerDrawableId?.let {
            divider {
                setDrawable(it)
                startVisible = false
                endVisible = true
            }
        }
        gridSpace?.let {
            dividerSpace (it.toInt(), DividerOrientation.VERTICAL)
            dividerSpace (it.toInt(), DividerOrientation.HORIZONTAL)
//            divider {
//                startVisible = true
//                endVisible = true
//            }
        }

        val mAdapter = BaseDatabindingAdapter().run {
            initData(itemData, itemViewType)
            onBind {
                onBindListener?.onBind(this, this.itemView.rootView, getItemViewType())

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
    value = ["setSelectedListener", "tabs"],
    requireAll = false
)
fun TabLayout.init(setSelectedListener: OnTabSelectedListener?, tabs: List<String>?) {
    tabs?.let {
        removeAllTabs()
        for (tab in it) {
            addTab(newTab().apply { text = tab })
        }
    } ?: run {
        visibility = GONE
    }

    setSelectedListener?.run {
        addOnTabSelectedListener(setSelectedListener)
    }
}

@BindingAdapter(
    value = ["onRefreshLoadMoreListener", "onLoadMoreListener"],
    requireAll = false
)
fun SmartRefreshLayout.init(onRefreshLoadMoreListener: OnRefreshLoadMoreListener?, onLoadMoreListener: OnLoadMoreListener?) {
    onRefreshLoadMoreListener?.let { setOnRefreshListener(it) }
    onLoadMoreListener?.let { setOnLoadMoreListener(it) }
}

@BindingAdapter(
    value = ["textChangedListener"],
    requireAll = false
)
fun EditText.init(textChangedListener: TextWatcher?) {
    textChangedListener?.let { addTextChangedListener(it) }
}

@SuppressLint("CheckResult")
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
    val glide = Glide.with(view.context)
        .asDrawable()
        .load(source)
        .placeholder(placeholder)
        .error(error)
        .fallback(fallback)
        .thumbnail(0.33f)
        .skipMemoryCache(false)
        .sizeMultiplier(0.5f)
        .format(DecodeFormat.PREFER_ARGB_8888)
        .encodeFormat(encodeFormat)
        .encodeQuality(80)
        .diskCacheStrategy(diskCacheStrategy)
        .transition(DrawableTransitionOptions.withCrossFade())
    width?.let {
        if (it > 0) {
            glide.override(widthSize, heightSize)
        }
    }
    glide.into(view)
}

@BindingAdapter(
    value = ["adapter", "fm", "fragments", "offLimit"],
    requireAll = false
)
fun ViewPager.init(
    ada: PagerAdapter?,
    fm: FragmentManager?,
    fragments: List<Fragment>?,
    offLimit: Int
) {
    ada?.let { this.adapter = it } ?: run {
        fm?.run { fragments?.run { adapter = BaseFragmentPagerAdapter(fm, fragments) } }
    }
    offscreenPageLimit = offLimit
}

@BindingAdapter(
    value = ["itemData", "itemViewType", "onBindListener", "offLimit", "attach"],
    requireAll = false
)
fun ViewPager2.init(
    itemData: List<BindModel>?,
    itemViewType: List<Int>?,
    onBindListener: BaseDatabindingAdapter.onBindListener?,
    offLimit: Int?,
    attachView: View?
) {

    if (itemData == null || itemViewType == null) {
        return
    }

    adapter?.run {

        if (itemData == bindingAdapter.models) {
            bindingAdapter.models = itemData
        } else {
            (bindingAdapter as BaseDatabindingAdapter).run {
                clearHeader(false)
                clearFooter(false)
                initData(itemData, itemViewType)
            }
        }
    } ?: run {
        BaseDatabindingAdapter().run {
            initData(itemData, itemViewType)
            onBind {
                onBindListener?.onBind(this, this.itemView.rootView, getItemViewType())

                itemView.rootView.setOnClickListener {
                    onBindListener?.onItemClick(modelPosition, layoutPosition, getItemViewType())
                }
            }
            adapter = this
        }
    }

    offLimit?.let { offscreenPageLimit = it }

    attachView?.let {
        if (it is TabLayout) {
            TabLayoutMediator(
                it, this
            ) { tab: TabLayout.Tab?, position: Int -> tab?.text = itemData[position].tag.toString()}.attach()
        }
    }
}

@BindingAdapter("isAnimationRunning")
fun ImageView.handleAnimation(isAnimationRunning: Boolean) {
    val animationDrawable = drawable as? AnimationDrawable
    if (isAnimationRunning) {
        GlobalScope.launch {
            delay(Random.Default.nextLong(1000))
            animationDrawable?.start()
        }
    } else {
        animationDrawable?.stop()
    }
}

/**
 * 如果Adapter是[BindingAdapter]则返回对象, 否则抛出异常
 * @exception NullPointerException
 */
val ViewPager2.bindingAdapter
    get() = adapter as? com.drake.brv.BindingAdapter
        ?: throw NullPointerException("RecyclerView without BindingAdapter")

fun FragmentActivity.FragmentPagerAdapter(
    fragments: List<Fragment>,
    titles: List<String>? = null
): BaseFragmentPagerAdapter {
    return BaseFragmentPagerAdapter(supportFragmentManager, fragments, titles)
}

fun Fragment.FragmentPagerAdapter(
    fragments: List<Fragment>,
    titles: List<String>? = null
): BaseFragmentPagerAdapter {
    return BaseFragmentPagerAdapter(childFragmentManager, fragments, titles)
}


class BaseFragmentPagerAdapter(
    fragmentManager: FragmentManager,
    var fragments: List<Fragment>,
    var titles: List<String>? = null
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles?.get(position)
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

}

fun FragmentActivity.FragmentAdapter(
    fragments: List<Fragment>
): FragmentStateAdapter = object : FragmentStateAdapter(this) {

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}

fun Fragment.FragmentAdapter(
    fragments: List<Fragment>
): FragmentStateAdapter = object : FragmentStateAdapter(this) {

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}



