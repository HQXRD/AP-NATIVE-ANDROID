package com.xtree.base.net.fastest

import com.alibaba.android.arouter.utils.TextUtils
import com.drake.net.Get
import com.drake.net.okhttp.trustSSLCertificate
import com.drake.net.transform.transform
import com.drake.net.utils.fastest
import com.drake.net.utils.runMain
import com.drake.net.utils.scopeNet
import com.google.gson.Gson
import com.xtree.base.R
import com.xtree.base.net.fastest.ChangeH5LineUtil.Companion.mCurH5DomainList
import com.xtree.base.utils.AESUtil
import com.xtree.base.utils.CfLog
import com.xtree.base.utils.DomainUtil
import com.xtree.base.vo.Domain
import me.xtree.mvvmhabit.http.NetworkUtil
import me.xtree.mvvmhabit.utils.ToastUtils
import me.xtree.mvvmhabit.utils.Utils
import java.util.concurrent.CancellationException

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
        if (!mHijackedDomainList.contains(domain)) {
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

    fun isRunning() : Boolean{
        return mIsRunning
    }

    open fun start(url: String, isApi: Boolean) {
        if (!NetworkUtil.isNetworkAvailable(Utils.getContext())) {
            runMain { ToastUtils.showShort("网络不可用，请检查您的手机网络是否开启") }
            return
        }
        if (!mIsRunning) {
            index = 0
            mUrl = url
            mIsApi = isApi
            CfLog.e("=====API开始切换线路========")
            mCurApiDomainList.clear()
            mThirdApiDomainList.clear()
            mIsRunning = true
            setThirdFasterDomain()
            getThirdFastestDomain()
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
                    getFastestAPI(host, mUrl),
                    tag = "the_fastest_line", block = FASTEST_BLOCK
                )
                    .transform { data ->
                        CfLog.i("$host")
                        CfLog.e("当前域名：api------$host---")
                        if (mIsApi) {
                            DomainUtil.setApiUrl(host)
                        } else {
                            DomainUtil.setH5Url(host)
                        }
                        onSuccessed()
                        mIsRunning = false
                        data
                    }

            }
            try {
                fastest(domainTasks, "the_fastest_line")
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
            if (mCurApiDomainList.isEmpty()) {
                onAllDomainHijacked()
            } else {
                getFastestApiDomain()
            }
        }
        //mCurApiDomainList.clear()
        if (index < mThirdApiDomainList.size && !TextUtils.isEmpty(mThirdApiDomainList[index])) {
            scopeNet {
                try {
                    val data =
                        Get<String>(mThirdApiDomainList[index], block = FASTEST_BLOCK).await()

                    var domainJson = AESUtil.decryptData(
                        data,
                        "wnIem4HOB2RKzhiqpaqbZuxtp7T36afAHH88BUht/2Y="
                    )
                    val domain: Domain = Gson().fromJson(domainJson, Domain::class.java)
                    if (mIsApi) {
                        if (!domain.api.isNullOrEmpty()) {
                            mCurApiDomainList.clear()
                        }
                        addApiDomainList(domain.api)
                    } else {
                        if (!domain.h5.isNullOrEmpty()) {
                            mCurH5DomainList.clear()
                        }
                        addApiDomainList(domain.h5)
                    }
                    if (mCurApiDomainList.isNotEmpty()) {
                        setFasterApiDomain()
                        CfLog.e("mCurApiDomainList.size==" + mCurApiDomainList.size)
                        if (mCurApiDomainList.isEmpty()) {
                            onAllDomainHijacked()
                        } else {
                            getFastestApiDomain()
                        }
                    } else {
                        index++
                        getThirdFastestDomain()
                    }
                } catch (e: Exception) {
                    CfLog.e("Exception==$e")
                    index++
                    getThirdFastestDomain()
                }
            }
        }
    }
}