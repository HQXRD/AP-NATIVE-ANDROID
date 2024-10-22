package com.xtree.bet.bean.response.pm

data class PMResultBean(
    val menuId: String,
    val menuType: Int,
    val name: String,
    val sportId: String,

)

data class Sub(
    val count: Int,
    val field1: String,
    val field2: String,
    val field3: String,
    val menuId: String,
    val menuType: String,
    val name: String,
    val sportId: String,
    val subList: Any
)