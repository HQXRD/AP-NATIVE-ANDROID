package com.xtree.base.net.fastest

import com.drake.net.utils.runMain
import com.xtree.base.utils.TagUtils
import me.xtree.mvvmhabit.utils.ToastUtils
import me.xtree.mvvmhabit.utils.Utils

/**
 * 域名竞速
 */
object SpeedH5DomainLine : ChangeLine() {

    override fun onSuccessed() {
        TagUtils.tagEvent(
            Utils.getContext(),
            "event_change_h5_line_successed",
            "切换H5线路成功"
        )
        runMain { ToastUtils.showShort("切换H5线路成功") }
    }

    override fun onFailed() {
        TagUtils.tagEvent(
            Utils.getContext(),
            "event_change_h5_line_failed",
            "切换H5线路失败"
        )
        runMain { ToastUtils.showShort("切换H5线路失败") }
    }

    /**
     * 开始域名竞速
     */
    fun start(){
        super.start(FASTEST_API, false)
    }
}