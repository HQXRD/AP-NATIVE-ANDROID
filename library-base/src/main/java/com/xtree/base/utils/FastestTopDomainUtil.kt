package com.xtree.base.utils

import com.alibaba.android.arouter.utils.TextUtils
import com.drake.net.Get
import com.drake.net.okhttp.trustSSLCertificate
import com.drake.net.transform.transform
import com.drake.net.utils.fastest
import com.drake.net.utils.runMain
import com.drake.net.utils.scopeNet
import com.google.gson.Gson
import com.xtree.base.R
import com.xtree.base.net.DnsFactory
import com.xtree.base.vo.Domain
import com.xtree.base.vo.EventVo
import com.xtree.base.vo.TopSpeedDomain
import me.xtree.mvvmhabit.http.NetworkUtil
import me.xtree.mvvmhabit.utils.ToastUtils
import me.xtree.mvvmhabit.utils.Utils
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.CancellationException

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
        private var mIsFinish: Boolean = true
    }

    fun start() {
        if(!NetworkUtil.isNetworkAvailable(Utils.getContext())){
            runMain { ToastUtils.showShort("网络不可用，请检查您的手机网络是否开启") }
            EventBus.getDefault().post(EventVo(EventConstant.EVENT_TOP_SPEED_FAILED, ""))
            return
        }
        if(mIsFinish) {
            CfLog.e("=====开始线路测速========")
            mIsFinish = false
            index = 0
            mCurApiDomainList.clear()
            mThirdApiDomainList.clear()
            mThirdDomainList.clear()
            mTopSpeedDomainList.clear()
            setThirdFasterDomain()
            setFasterApiDomain()
        }
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
                if(!TextUtils.isEmpty(s)) {
                    mCurApiDomainList.add(s)
                }
            }
        }
    }

    private fun addThirdDomainList(domainList: List<String>) {
        domainList.forEachIndexed { _, s ->
            run {
                if(!TextUtils.isEmpty(s)) {
                    mThirdDomainList.add(s)
                }
            }
        }
    }

    private fun getFastestApiDomain(isThird: Boolean) {

        scopeNet {
            // 并发请求本地配置的域名 命名参数 uid = "the fastest line" 用于库自动取消任务
            var curTime: Long = System.currentTimeMillis()
            val domainTasks = mCurApiDomainList.map { host ->
                Get<String>(
                    "$host/api/settings/?&fields=customer_service_url")
                {
                    addHeader("App-RNID", "87jumkljo")
                    setClient {
                        dns(DnsFactory.getDns())
                        trustSSLCertificate()
                    }
                }.transform { data ->
                    CfLog.i("$host")
                    var topSpeedDomain = TopSpeedDomain()
                    topSpeedDomain.url = host
                    topSpeedDomain.speedSec = System.currentTimeMillis() - curTime
                    CfLog.e("域名：api------$host---${topSpeedDomain.speedSec}")
                    mTopSpeedDomainList.add(topSpeedDomain)
                    mCurApiDomainList.remove(host)
                    if(mTopSpeedDomainList.size < 4 && mCurApiDomainList.isNotEmpty()) {
                        getFastestApiDomain(isThird)
                    }else{
                        mIsFinish = true
                        mTopSpeedDomainList.sort()
                        DomainUtil.setApiUrl(mTopSpeedDomainList[0].url)
                        EventBus.getDefault().post(EventVo(EventConstant.EVENT_TOP_SPEED_FINISH, ""))
                    }
                    data
                }
            }
            try {
                fastest(domainTasks)
            } catch (e: Exception) {
                CfLog.e(e.toString())
                if (e !is CancellationException) {
                    if(isThird){ // 失败
                        mIsFinish = true
                        EventBus.getDefault().post(EventVo(EventConstant.EVENT_TOP_SPEED_FAILED, ""))
                    }else {
                        getThirdFastestDomain(true)
                    }
                }
            }
        }
    }

    var index: Int = 0

    /**
     * 三方域名存储地址竞速
     * @param needClear 是否删除清除本地预埋的竞速地址
     */
    private fun getThirdFastestDomain(needClear: Boolean) {
        if (needClear) {
            mCurApiDomainList.clear()
        }
        if (index < mThirdDomainList.size && !TextUtils.isEmpty(mThirdDomainList[index])) {

            scopeNet {
                val data = Get<String>(
                    mThirdDomainList[index]
                ) {
                    addHeader("App-RNID", "87jumkljo")
                    setClient {
                        dns(DnsFactory.getDns())
                        trustSSLCertificate()
                    }
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
                    getFastestApiDomain(true)
                    CfLog.e("getThirdFastestDomain success")

                } catch (e: Exception) {
                    CfLog.e("getThirdFastestDomain fail")
                    index++
                    getThirdFastestDomain(true)
                }
            }
        }else if(mCurApiDomainList.isEmpty()){
            mIsFinish = true
            EventBus.getDefault().post(EventVo(EventConstant.EVENT_TOP_SPEED_FAILED, ""))
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
        if(mCurApiDomainList.size >= 4) {
            getFastestApiDomain(false)
        }else {
            getThirdFastestDomain(false)
        }
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