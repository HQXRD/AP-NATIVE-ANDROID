package com.xtree.mine.ui.fragment

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.xtree.base.adapter.CacheViewHolder
import com.xtree.base.adapter.CachedAutoRefreshAdapter
import com.xtree.mine.R
import com.xtree.mine.databinding.ItemRebateDetailsBinding
import com.xtree.mine.databinding.ItemRebateDetailsChildBinding
import com.xtree.mine.vo.X0

class RebateDetailsAdapter(val context: Context) : CachedAutoRefreshAdapter<KeyValue>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rebate_details, parent, false))
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        val binding = ItemRebateDetailsBinding.bind(holder.itemView)
        val keyValue: KeyValue = get(position)
        binding.tvTitle.text = when (keyValue.key) {
            0 -> "时时彩返点"
            2 -> "11选5返点"
            3 -> "快乐彩返点"
            else -> ""
        }
        val adapter = RDChildAdapter(context)
        adapter.addAll(get(position).value)
        binding.rvRb.adapter = adapter
        binding.rvRb.layoutManager = GridLayoutManager(context, 2)
    }
}

class RDChildAdapter(val context: Context) : CachedAutoRefreshAdapter<X0>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rebate_details_child, parent, false))
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        val binding = ItemRebateDetailsChildBinding.bind(holder.itemView)

        val name = get(position).lotteryname.trim().plus("：")
        val rebate = get(position).prizegroup.plus("%")
        val txt = name + "<font color=" + ContextCompat.getColor(context,R.color.color_FD5255) + ">" + rebate + "</font>"
        binding.tvName.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(txt, Html.FROM_HTML_MODE_COMPACT)
        }else{
            Html.fromHtml(txt)
        }
    }
}


class KeyValue(
    var key: Int,
    var value: List<X0>
)