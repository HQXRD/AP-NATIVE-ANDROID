package com.xtree.base.utils

import android.content.Intent
import com.alibaba.android.arouter.utils.TextUtils
import com.drake.net.Get
import com.drake.net.tag.RESPONSE
import com.drake.net.transform.transform
import com.drake.net.utils.fastest
import com.drake.net.utils.scopeNet
import com.google.gson.Gson
import com.xtree.base.R
import com.xtree.base.vo.Domain
import com.xtree.base.vo.EventVo
import com.xtree.base.vo.TopSpeedDomain
import me.xtree.mvvmhabit.base.AppManager
import me.xtree.mvvmhabit.utils.Utils
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeUnit

class FastestTopDomainUtil private constructor() {
    init {
        mThirdApiDomainList = ArrayList()
        mCurApiDomainList = ArrayList()
        mThirdDomainList = ArrayList()
        mTopSpeedDomainList = ArrayList()
    }

    companion object {
        @JvmStatic
        val instance: FastestTopDomainUtil by lazy { FastestTopDomainUtil() }

        /**
         * 当前预埋域名列表
         */
        private lateinit var mThirdApiDomainList: MutableList<String>
        private lateinit var mCurApiDomainList: MutableList<String>
        private lateinit var mThirdDomainList: MutableList<String>
        private lateinit var mTopSpeedDomainList: MutableList<TopSpeedDomain>
        private var mIsFinish: Boolean = false

        /**
         * 第一次进入app后获取最快4条域名是否完成
         */
        var mIsFirstFinish: Boolean = false

    }

    fun start() {
        CfLog.e("=====开始切换线路========")
        index = 0
        mCurApiDomainList.clear()
        mThirdApiDomainList.clear()
        mThirdDomainList.clear()
        mTopSpeedDomainList.clear()
        setThirdFasterDomain()
        setFasterApiDomain()
    }

    fun isFinish(): Boolean {
        return mIsFinish
    }

    fun getTopSpeedDomain() : MutableList<TopSpeedDomain>{
        return mTopSpeedDomainList
    }

    private fun addApiDomainList(list: List<String>) {
        list.forEachIndexed { _, s ->
            run {
                mCurApiDomainList.add(s)
            }
        }
    }

    private fun addThirdDomainList(domainList: List<String>) {
        domainList.forEachIndexed { _, s ->
            run {
                mThirdDomainList.add(s)
            }
        }
    }

    private fun getFastestApiDomain() {

        scopeNet {
            // 并发请求本地配置的域名 命名参数 uid = "the fastest line" 用于库自动取消任务
            var curTime: Long = System.currentTimeMillis()
            val domainTasks = mCurApiDomainList.map { host ->
                Get<String>(
                    "$host/api/bns/4/banners?limit=2",
                    absolutePath = true,
                    tag = RESPONSE,
                    uid = "the_fastest_line"
                ).transform { data ->
                    CfLog.i("$host")
                    CfLog.e("域名：api------$host")
                    if(mTopSpeedDomainList.size == 0) {
                        DomainUtil.setApiUrl(host)
                    }
                    var topSpeedDomain = TopSpeedDomain()
                    topSpeedDomain.url = host
                    topSpeedDomain.speedSec = System.currentTimeMillis() - curTime
                    mTopSpeedDomainList.add(topSpeedDomain)
                    mCurApiDomainList.remove(host)
                    if(mTopSpeedDomainList.size < 4 && mCurApiDomainList.isNotEmpty()) {
                        getFastestApiDomain()
                    }else{
                        mIsFinish = true
                        if(!mIsFirstFinish){
                            mIsFirstFinish = true
                        }
                        EventBus.getDefault().post(EventVo(EventConstant.EVENT_TOP_SPEED_FINISH, ""))
                    }
                    data
                }
            }
            try {
                fastest(domainTasks, uid = "the_fastest_line")
            } catch (e: Exception) {
                CfLog.e(e.toString())
                if (e !is CancellationException) {
                    getThirdFastestDomain()
                }
            }
        }
    }

    var index: Int = 0

    /**
     * 三方域名存储地址竞速
     */
    private fun getThirdFastestDomain() {
        mCurApiDomainList.clear()
        if (index < mThirdDomainList.size && !TextUtils.isEmpty(mThirdDomainList[index])) {
            scopeNet {
                val data = Get<String>(
                    mThirdDomainList[index],
                    absolutePath = true,
                    tag = RESPONSE,
                    uid = "the_fastest_line_third"
                ) {
                    addHeader("App-RNID", "87jumkljo")
                    connectTimeout(10, TimeUnit.SECONDS)
                }.await()

                try {
                    var domainJson = AESUtil.decryptData(
                        data,
                        "wnIem4HOB2RKzhiqpaqbZuxtp7T36afAHH88BUht/2Y="
                    )
                    val domain: Domain = Gson().fromJson(domainJson, Domain::class.java)
                    domain.api.forEachIndexed { _, domain ->
                        run {
                            if(!mCurApiDomainList.contains(domain)) {
                                mCurApiDomainList.add(domain)
                            }
                        }
                    }
                    getFastestApiDomain()
                    CfLog.e("getThirdFastestDomain success")

                } catch (e: Exception) {
                    CfLog.e("getThirdFastestDomain fail")
                    index++
                    getThirdFastestDomain()
                }
            }
        }
    }

    /**
     * 线路竞速
     */
    private fun setFasterApiDomain() {
        val apis = Utils.getContext().getString(R.string.domain_api_list) // 不能为空,必须正确
        val apiList = listOf(*apis.split(";".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray())
        addApiDomainList(apiList)
        getFastestApiDomain()
    }

    /**
     * 获取
     */
    private fun setThirdFasterDomain() {
        val urls = Utils.getContext().getString(R.string.domain_url_list_third)
        val list = listOf(*urls.split(";".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray())
        addThirdDomainList(list)
    }
}