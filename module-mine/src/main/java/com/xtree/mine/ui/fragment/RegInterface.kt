package com.xtree.mine.ui.fragment

import android.widget.TextView
import java.math.BigDecimal

interface RegInterface {
    public val mList: ArrayList<String>
        get() = arrayListOf("代理", "会员")


    //代理
    val daili: Int
        get() = 1

    //会员
    val member: Int
        get() = 0

    /**
     * 去除百分比符号
     */
    fun TextView.removePercentage(): String {
        return text.toString().replace("%", "")
    }

    /**
     * 获得返点list
     */
    fun getRebateList(max: Double): ArrayList<Double> {
        val start = BigDecimal.valueOf(max)
        val end = BigDecimal.ZERO
        val step = BigDecimal.valueOf(0.1)
        val arraySize = ((start - end) / step).toInt() + 1
        val arrayList = ArrayList<Double>()
        for (i in 0 until arraySize) {
            val value = start - i.toBigDecimal() * step
            arrayList.add(value.toDouble())
        }
        return arrayList
    }
}

