package com.xtree.base.net.fastest

import com.drake.net.utils.runMain
import com.xtree.base.utils.TagUtils
import me.xtree.mvvmhabit.utils.ToastUtils
import me.xtree.mvvmhabit.utils.Utils

/**
 * 接口域名竞速
 */
object SpeedApiLine : ChangeLine() {

    override fun onSuccessed() {
        TagUtils.tagEvent(
            Utils.getContext(),
            "event_change_api_line_successed",
            "切换API线路成功"
        )
        runMain { ToastUtils.showShort("切换线路成功") }
    }

    override fun onFailed() {
        TagUtils.tagEvent(
            Utils.getContext(),
            "event_change_api_line_failed",
            "切换API线路失败"
        )
        runMain { ToastUtils.showShort("切换线路失败，请检查手机网络连接情况") }
    }

    /**
     * 开始域名竞速
     */
    fun start(){
        super.start(FASTEST_API, true)
    }
}