package com.xtree.mine.ui.fragment

import android.widget.TextView

interface RegInterface {
    public val mList: ArrayList<String>
        get() = arrayListOf("大招商", "招商", "会员")

    //未知
    val unknown: Int
        get() = 0

    //管理员
    val rootmanager: Int
        get() = 1

    //大招商
    val dazhaoshang: Int
        get() = 2

    //招商
    val zhaoshang: Int
        get() = 3

    //会员
    val member: Int
        get() = 4
    /**
     * 去除百分比符号
     */
    fun TextView.removePercentage(): String {
        return text.toString().replace("%", "")
    }

}

