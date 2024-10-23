package com.xtree.home.ui.custom.view

import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.widget.ImageView
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.util.XPopupUtils
import com.xtree.base.widget.BrowserActivity
import com.xtree.home.R
import me.xtree.mvvmhabit.utils.ConvertUtils
import me.xtree.mvvmhabit.utils.KLog

class ECAnimDialog : BasePopupView {
    constructor(context: Context) : super(context)
    constructor(context: Context, url: String) : super(context) {
        this.url = url;
    }

    private lateinit var url: String

    override fun onCreate() {
        super.onCreate()
        initView()
    }

    override fun getInnerLayoutId(): Int {
        return R.layout.dialog_ec_anim
    }

    private fun initView() {
        val ivClose = findViewById<ImageView>(R.id.iv_close)
        val layoutTop = findViewById<View>(R.id.layout_top)
        val ivBottom = findViewById<ImageView>(R.id.iv_bottom)
        ivClose.setOnClickListener { dismiss() }
        ivBottom.setOnClickListener {
            BrowserActivity.start(context, url)
            dismiss()
        }

        val height1 = XPopupUtils.getScreenHeight(context) / 2f - ConvertUtils.dp2px((258 + 20 + 20 - 40).toFloat())

        val height2 =
            ConvertUtils.dp2px((103 + 20).toFloat()) - XPopupUtils.getScreenHeight(context) / 2f

        KLog.i(
            "height", "$height1     $height2" + "      " + XPopupUtils.getScreenHeight(context) + "     " + XPopupUtils.getNavBarHeight(hostWindow) +
                    "   " + getNavigationBarHeight(context)
        )
        val objectAnimatorX = ObjectAnimator.ofFloat(layoutTop, "translationY", height1)
        objectAnimatorX.setDuration(500)
        objectAnimatorX.start()
        val objectAnimatorBottom = ObjectAnimator.ofFloat(ivBottom, "translationY", height2)
        objectAnimatorBottom.setDuration(500)
        objectAnimatorBottom.start()
    }

    private fun getNavigationBarHeight(context: Context): Int {
        val rid: Int =
            context.resources.getIdentifier("config_showNavigationBar", "bool", "android")
        return if (rid != 0) {
            val resourceId: Int =
                context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

}