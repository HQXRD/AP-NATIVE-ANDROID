package com.xtree.mine.vo

import java.util.ArrayList

data class MarketingVo(
    var chessPoint: String,
    val customer_service_link: String,
    val desK: String,
    val domainList: List<String>,
    var esportsPoint: String,
    val links: List<LinkVo>,
    var livePoint: String,
    val maxPoint: String,
    val prizeGroups: HashMap<Int,List<X0>>?,
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
    var sportPoint: String,
    val topprizes_herolist_enabled: String,
    val topprizes_publicity_enabled: String,
    val topprizes_wintips_enabled: String,
    val user: User,
    val user_channel_id: String,
    val usertype: Int,
    val webtitle: String,
    val sMsg: String
)

data class LinkVo(
    val activity_proportion: String,
    val chesspoint: String,
    val contact: List<Any>,
    val createtime: String,
    val daily_wage_proportion: String,
    val domain: String,
    val esportpoint: String,
    val hourly_rate_proportion: String,
    val id: String,
    val is_activity: String,
    val is_daily_wage: String,
    val is_hourly_rate: String,
    val isrequiremoblie: String,
    val keeppoint: String,
    val livepoint: String,
    val markct: List<Any>,
    val sportpoint: String,
    val type: String,
    val updatetime: String,
    val userid: String,
    val usertype: String,
    val zhaoshang: String
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


data class X0(
    val lotteryname: String,
    val prizegroup: String
)
