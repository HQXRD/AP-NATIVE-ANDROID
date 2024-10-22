package com.xtree.bet.ui.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xtree.bet.R
import com.xtree.bet.constant.SportTypeItem

class TabSportAdapter(
    list: MutableList<SportTypeItem>,
    private val matchGames: HashMap<Int, SportTypeItem>
) : BaseQuickAdapter<SportTypeItem, BaseViewHolder>(R.layout.bt_layout_bet_match_item_tab_item, list) {
    private var selectedPosition: Int = -1

    override fun convert(holder: BaseViewHolder, item: SportTypeItem) {
        var isSelected = false
        if (holder.layoutPosition == selectedPosition) {
            isSelected = true
        }
        val sportItem = matchGames[item.id]
        sportItem?.apply {
            holder.setText(R.id.tab_item_name, sportItem.name)
            holder.getView<TextView>(R.id.tab_item_name).isSelected = isSelected
            holder.setText(R.id.tv_match_count, item.num.toString())
            holder.getView<TextView>(R.id.tv_match_count).isSelected = isSelected
            holder.setBackgroundResource(R.id.iv_icon, sportItem.iconId)
            holder.getView<ImageView>(R.id.iv_icon).isSelected = isSelected
        }
    }

    fun setSelectedPosition(position: Int) {
        selectedPosition = position;
    }
}

class TabSportResultAdapter(
    list: ArrayList<SportTypeItem>,
    private val matchGames: HashMap<Int, SportTypeItem>
) : BaseQuickAdapter<SportTypeItem, BaseViewHolder>(R.layout.bt_layout_bet_match_item_tab_item, list) {

    private var selectedPosition: Int = -1

    override fun convert(holder: BaseViewHolder, item: SportTypeItem) {
        var isSelected = false
        if (holder.layoutPosition == selectedPosition) {
            isSelected = true
        }
        val sportItem = matchGames[item.id]
        sportItem?.apply {
            holder.setText(R.id.tab_item_name, name)
            holder.getView<TextView>(R.id.tab_item_name).isSelected = isSelected
            holder.setBackgroundResource(R.id.iv_icon, iconId)
            holder.getView<ImageView>(R.id.iv_icon).isSelected = isSelected
        }
    }

    fun setSelectedPosition(position: Int) {
        val lastPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(lastPosition)
        notifyItemChanged(selectedPosition)
    }

}