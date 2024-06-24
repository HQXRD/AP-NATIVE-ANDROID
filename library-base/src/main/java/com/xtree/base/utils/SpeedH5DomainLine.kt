package com.xtree.base.utils

/**
 * 域名竞速
 */
object SpeedH5DomainLine : ChangeLine() {

    override fun onSuccessed() {

    }

    override fun onFailed() {

    }

    /**
     * 开始域名竞速
     */
    fun start(){
        super.start("point.bmp")
    }
}