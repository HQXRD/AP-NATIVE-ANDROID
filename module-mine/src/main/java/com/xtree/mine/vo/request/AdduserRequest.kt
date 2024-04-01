package com.xtree.mine.vo.request

data class AdduserRequest(
    val nonce: String,
    val flag: String,
    val usertype: String,
    val username: String,
    val userpass: String,
    val nikename: String,


    val point: String,
    val livepoint: String,
    val sportpoint: String,
    val pokerpoint: String,
    val esportspoint: String
)