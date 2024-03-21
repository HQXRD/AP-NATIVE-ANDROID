package com.xtree.mine.vo

data class AdduserVo(
    val auto_redirection: String,
    val customer_service_link: String,
    val default_url: String,
    val desK: String,
    val error: String,
    val links: List<Link>,
    val message: String,
    val msg_detail: String,
    val msg_type: Int,
    val pt_download_pc: String,
    val pub_channel_id: String,
    val pub_channel_token: String,
    val push_server_host: String,
    val push_service_module: String,
    val push_service_status: Int,
    val sSystemImagesAndCssPath: String,
    val sdata: List<Any>,
    val target: String,
    val topprizes_herolist_enabled: String,
    val topprizes_publicity_enabled: String,
    val topprizes_wintips_enabled: String,
    val user: UserAdd,
    val user_channel_id: String,
    val webtitle: String
)

data class Link(
    val target: String,
    val title: String,
    val url: String
)

data class UserAdd(
    val availablebalance: String,
    val iscreditaccount: String,
    val messages: String,
    val nickname: String,
    val parentid: String,
    val preinfo: String,
    val userrank: String,
    val usertype: String
)