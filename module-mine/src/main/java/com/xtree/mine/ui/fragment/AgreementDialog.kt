package com.xtree.mine.ui.fragment

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.util.XPopupUtils
import com.xtree.mine.R

/**
 * 游戏网站使用者条款
 */
class AgreementDialog(context: Context) : BottomPopupView(context) {
    override fun onCreate() {
        super.onCreate()
        val tvw = findViewById<TextView>(R.id.tvw_agreement)
        tvw.movementMethod = ScrollingMovementMethod.getInstance()
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_agreement
    }

    override fun getMaxHeight(): Int {
        return XPopupUtils.getScreenHeight(context) * 80 / 100
    }

}