package com.xtree.bet.bean.response.fb

import com.xtree.bet.bean.response.pm.MatchInfo

data class MatchResultBean(
    val current: Int,
    val records: List<MatchInfo>,
    val size: Int,
    val total: Int,
    val totalType: Int
)

data class Record(
    val bt: Long,
    val fid: Int,
    val fmt: Int,
    val id: Int,
    val lg: Lg,
    val ms: Int,
    val ne: Int,
    val nsg: List<Nsg>,
    val sid: Int,
    val ts: List<T>
)

data class Lg(
    val hot: Boolean,
    val id: Int,
    val lurl: String,
    val na: String,
    val or: Int,
    val rid: Int,
    val rlg: String,
    val rnm: String,
    val sid: Int,
    val slid: Int
)

data class Nsg(
    val pe: Int,
    val sc: List<Int>,
    val tyg: Int
)

data class T(
    val id: Int,
    val lurl: String,
    val na: String
)