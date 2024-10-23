package com.xtree.bet.ui.fragment

import android.content.Context
import android.widget.ImageView
import com.lxj.xpopup.core.BasePopupView
import com.xtree.bet.R

class NoDataDialog(context: Context) : BasePopupView(context) {

    override fun onCreate() {
        super.onCreate()
        initView()
    }

    override fun getInnerLayoutId(): Int {
        return R.layout.dialog_no_data
    }

    private fun initView() {
        val ivClose = findViewById<ImageView>(R.id.iv_close)
        ivClose.setOnClickListener { dismiss() }

    }


}