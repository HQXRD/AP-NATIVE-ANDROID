package com.xtree.bet.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xtree.base.utils.TimeUtils
import com.xtree.bet.R
import com.xtree.bet.bean.response.fb.FBAnnouncementInfo

class BtAnnouncementAdapter(list: MutableList<FBAnnouncementInfo.RecordsDTO>) :
    BaseQuickAdapter<FBAnnouncementInfo.RecordsDTO, BaseViewHolder>(R.layout.item_announcement, list) {

    override fun convert(holder: BaseViewHolder, item: FBAnnouncementInfo.RecordsDTO) {
        holder.setText(R.id.tv_title, item.ti)
        val time = TimeUtils.longFormatString(java.lang.Long.valueOf(item.pt), TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS_1)
        holder.setText(R.id.tv_time, time)
        holder.setText(R.id.tv_content, item.co)
    }

}