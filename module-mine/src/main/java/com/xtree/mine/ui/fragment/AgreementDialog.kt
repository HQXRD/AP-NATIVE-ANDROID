package com.xtree.mine.ui.fragment

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.widget.CheckBox
import android.widget.TextView
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.util.XPopupUtils
import com.xtree.mine.R

/**
 * 游戏网站使用者条款
 */
class AgreementDialog(context: Context, private val checkBox: CheckBox) : BottomPopupView(context) {
    override fun onCreate() {
        super.onCreate()
        val tvw = findViewById<TextView>(R.id.tvw_agreement)
        tvw.movementMethod = ScrollingMovementMethod.getInstance()
        val tvAgree = findViewById<TextView>(R.id.tv_agree)
        val tvNoAgree = findViewById<TextView>(R.id.tv_no_agree)
        tvAgree.setOnClickListener {
            checkBox.isChecked = true
            dismiss()
        }
        tvNoAgree.setOnClickListener {
            checkBox.isChecked = false
            dismiss()
        }
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_agreement
    }

    override fun getMaxHeight(): Int {
        return XPopupUtils.getScreenHeight(context) * 90 / 100
    }

}