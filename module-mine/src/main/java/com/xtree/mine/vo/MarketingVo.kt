package com.xtree.mine.vo

import me.xtree.mvvmhabit.http.BaseResponse2

data class MarketingVo(
    val chessPoint: String,
    val customer_service_link: String,
    val desK: String,
    val domainList: List<String>,
    val esportsPoint: String,
    val links: List<Any>,
    val livePoint: String,
    val maxPoint: String,
    val pt_download_pc: String,
    val pub_channel_id: String,
    val pub_channel_token: String,
    val push_server_host: String,
    val push_service_module: String,
    val push_service_status: Int,
    val sSystemImagesAndCssPath: String,
    val sSystemMessagesByTom: String,
    val selectedPoint: String,
    val selfPoint: String,
    val sportPoint: String,
    val topprizes_herolist_enabled: String,
    val topprizes_publicity_enabled: String,
    val topprizes_wintips_enabled: String,
    val user: User,
    val user_channel_id: String,
    val usertype: Int,
    val webtitle: String
)

data class User(
    val availablebalance: String,
    val iscreditaccount: String,
    val messages: String,
    val nickname: String,
    val parentid: String,
    val preinfo: String,
    val userrank: String,
    val usertype: String
)