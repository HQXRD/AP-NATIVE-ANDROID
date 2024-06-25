package com.xtree.base.utils

import com.alibaba.android.arouter.utils.TextUtils
import com.drake.net.Get
import com.drake.net.tag.RESPONSE
import com.drake.net.transform.transform
import com.drake.net.utils.fastest
import com.drake.net.utils.runMain
import com.drake.net.utils.scopeNet
import com.google.gson.Gson
import com.xtree.base.R
import com.xtree.base.vo.Domain
import me.xtree.mvvmhabit.http.NetworkUtil
import me.xtree.mvvmhabit.utils.ToastUtils
import me.xtree.mvvmhabit.utils.Utils
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeUnit

abstract class ChangeLine {
    /**
     * 是否是api域名竞速，true-api域名竞速，false-H5域名竞速
     */
    private var mIsApi: Boolean = false

    /**
     * 所有参与竞速的域名列表
     */
    private var mCurApiDomainList: MutableList<String> = ArrayList()

    /**
     * 被劫持域名列表
     */
    private var mHijackedDomainList: MutableList<String> = ArrayList()

    /**
     * 存放域名列表的三方域名
     */
    private var mThirdApiDomainList: MutableList<String> = ArrayList()

    /**
     * 是否正在竞速
     */
    private var mIsRunning: Boolean = false

    /**
     * 竞速使用的资源地址
     */
    private var mUrl: String = ""
    private var index = 0

    abstract fun onSuccessed()
    abstract fun onFailed()

    fun addHijeckedDomainList(domain: String) {
        if(!mHijackedDomainList.contains(domain)) {
            mHijackedDomainList.add(domain)
        }
        mCurApiDomainList.remove(domain)
    }

    /**
     * 当所有域名都被劫持时的操作
     */
    fun onAllDomainHijacked() {
        mIsRunning = false
        mHijackedDomainList.clear()
        start(mUrl, mIsApi)
    }

    open fun start(url: String, isApi: Boolean) {
        if(!NetworkUtil.isNetworkAvailable(Utils.getContext())){
            runMain { ToastUtils.showShort("网络不可用，请检查您的手机网络是否开启") }
            return
        }
        if (!mIsRunning) {
            index = 0
            mUrl = url
            mIsApi = isApi
            CfLog.e("=====开始切换线路========")
            mCurApiDomainList.clear()
            mThirdApiDomainList.clear()
            mIsRunning = true
            setThirdFasterDomain()
        }
    }

    private fun addApiDomainList(list: List<String>) {
        list.forEachIndexed { _, s ->
            run {
                var filter = mHijackedDomainList.filter {
                    s.contains(it)
                }
                if (filter.isEmpty() && !mCurApiDomainList.contains(s)) {
                    mCurApiDomainList.add(s)
                }
            }
        }
    }

    private fun addThirdDomainList(domainList: List<String>) {
        domainList.forEachIndexed { _, s ->
            run {
                mThirdApiDomainList.add(s)
            }
        }
    }

    /**
     * 设置三方存储domain域名地址
     */
    private fun setThirdFasterDomain() {
        val urls = Utils.getContext().getString(R.string.domain_url_list_third)
        val list = listOf(*urls.split(";".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray())
        addThirdDomainList(list)
        getThirdFastestDomain()
    }

    /**
     * 线路竞速
     */
    private fun setFasterApiDomain() {
        var apis = Utils.getContext().getString(R.string.domain_api_list)
        if (!mIsApi) {
            apis = Utils.getContext().getString(R.string.domain_url_list) // 不能为空,必须正确
        }
        val apiList = listOf(*apis.split(";".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray())
        addApiDomainList(apiList)
    }

    private fun getFastestApiDomain() {

        scopeNet {
            // 并发请求本地配置的域名 命名参数 uid = "the fastest line" 用于库自动取消任务
            val domainTasks = mCurApiDomainList.map { host ->
                Get<String>(
                    "$host/$mUrl",
                    absolutePath = true,
                    tag = RESPONSE,
                    uid = "the_fastest_line"
                ).transform { data ->
                    CfLog.i("$host")
                    CfLog.e("当前域名：api------$host---")
                    DomainUtil.setApiUrl(host)
                    onSuccessed()
                    mIsRunning = false
                    data
                }
            }
            try {
                fastest(domainTasks, uid = "the_fastest_line")
            } catch (e: Exception) {
                CfLog.e(e.toString())
                mIsRunning = false
                if (e !is CancellationException) {
                    CfLog.e("切换线路失败，请检查手机网络连接情况")
                    onFailed()
                }
            }
        }
    }

    /**
     * 三方域名存储地址竞速
     */
    private fun getThirdFastestDomain() {
        if (index == mThirdApiDomainList.size - 1) {
            setFasterApiDomain()
            CfLog.e("mCurApiDomainList.size==" + mCurApiDomainList.size)
            if(mCurApiDomainList.isEmpty()){
                onAllDomainHijacked()
            }else{
                getFastestApiDomain()
            }
        }
        //mCurApiDomainList.clear()
        if (index < mThirdApiDomainList.size && !TextUtils.isEmpty(mThirdApiDomainList[index])) {
            scopeNet {
                val data = Get<String>(
                    mThirdApiDomainList[index],
                    absolutePath = true,
                    tag = RESPONSE,
                    uid = "the_fastest_line_third"
                ) {
                    addHeader("App-RNID", "87jumkljo")
                    connectTimeout(5, TimeUnit.SECONDS)
                }.await()

                try {
                    var domainJson = AESUtil.decryptData(
                        data,
                        "wnIem4HOB2RKzhiqpaqbZuxtp7T36afAHH88BUht/2Y="
                    )
                    val domain: Domain = Gson().fromJson(domainJson, Domain::class.java)
                    if (mIsApi) {
                        addApiDomainList(domain.api)
                    } else {
                        addApiDomainList(domain.h5)
                    }
                    if (mCurApiDomainList.isNotEmpty()) {
                        setFasterApiDomain()
                        CfLog.e("mCurApiDomainList.size==" + mCurApiDomainList.size)
                        if(mCurApiDomainList.isEmpty()){
                            onAllDomainHijacked()
                        }else{
                            getFastestApiDomain()
                        }
                    }
                } catch (e: Exception) {
                    index++
                    getThirdFastestDomain()
                }
            }
        }
    }
}