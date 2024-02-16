package com.xtree.home.vo

data class EleVo(
    val count: String,
    val list: List<Ele>
)

data class Ele(
    val added_at: String,
    val cate_id: Int,
    val code: String,
    val id: Int,
    val is_hot: String,
    val name: String,
    val picture: String,
    val platform_id: Int
)