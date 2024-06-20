package com.xtree.base.utils

/**
 * 接口域名竞速
 */
object SpeedApiLine : ChangeLine() {

    override fun onSuccessed() {

    }

    override fun onFailed() {

    }

    /**
     * 开始域名竞速
     */
    fun start(){
        super.start("api/bns/4/banners?limit=2")
    }
}