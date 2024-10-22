package com.xtree.bet.bean.response.fb

data class ResultBean(
    val sss: IntArray,//体育
    val oss: IntArray//冠军
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ResultBean

        if (!sss.contentEquals(other.sss)) return false
        return oss.contentEquals(other.oss)
    }

    override fun hashCode(): Int {
        var result = sss.contentHashCode()
        result = 31 * result + oss.contentHashCode()
        return result
    }
}