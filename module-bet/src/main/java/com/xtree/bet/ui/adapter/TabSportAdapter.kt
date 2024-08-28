package com.xtree.bet.ui.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xtree.bet.R
import com.xtree.bet.constant.SportTypeItem

class TabSportAdapter(list: MutableList<SportTypeItem>) : BaseQuickAdapter<SportTypeItem, BaseViewHolder>(R.layout.bt_layout_bet_match_item_tab_item, list) {
    override fun convert(holder: BaseViewHolder, item: SportTypeItem) {
        holder.setText(R.id.tab_item_name, item.name)
        holder.getView<TextView>(R.id.tab_item_name).isSelected = item.isSelected
        holder.setText(R.id.tv_match_count, item.num.toString())
        holder.getView<TextView>(R.id.tv_match_count).isSelected = item.isSelected
        holder.setBackgroundResource(R.id.iv_icon, item.iconId)
        holder.getView<ImageView>(R.id.iv_icon).isSelected = item.isSelected
    }
}