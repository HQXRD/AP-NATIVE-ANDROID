package com.xtree.mine.ui.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.xtree.base.adapter.CacheViewHolder
import com.xtree.base.adapter.CachedAutoRefreshAdapter
import com.xtree.mine.R
import com.xtree.mine.databinding.ItemRebateDetailsBinding
import com.xtree.mine.databinding.ItemRebateDetailsChildBinding
import com.xtree.mine.vo.X0

class RebateDetailsAdapter(val context: Context) : CachedAutoRefreshAdapter<List<X0>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rebate_details, parent, false))
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        val binding = ItemRebateDetailsBinding.bind(holder.itemView)
        binding.tvTitle.text = when (position) {
            0 -> "时时彩返点"
            1 -> "11选5返点"
            2 -> "快乐彩返点"
            else -> ""
        }
        val adapter = RDChildAdapter(context)
        adapter.addAll(get(position))
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
        val txt = name + "<font color=" + ContextCompat.getColor(context, R.color.clr_txt_rebateagrt_mark) + ">" + rebate + "</font>"
        binding.tvName.text =
            HtmlCompat.fromHtml(txt, HtmlCompat.FROM_HTML_MODE_COMPACT)

    }
}