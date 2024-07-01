package com.xtree.base.utils

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
import me.xtree.mvvmhabit.http.NetworkUtil
import me.xtree.mvvmhabit.utils.ToastUtils
import me.xtree.mvvmhabit.utils.Utils
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.CancellationException

class ChangeH5LineUtil private constructor() {

    init {
        mCurH5DomainList = ArrayList()
        mThirdDomainList = ArrayList()
    }

    companion object {
        @JvmStatic
        val instance: ChangeH5LineUtil by lazy { ChangeH5LineUtil() }

        /**
         * 当前预埋域名列表
         */
        lateinit var mCurH5DomainList: MutableList<String>
        lateinit var mThirdDomainList: MutableList<String>
        private var mIsRunning: Boolean = false
    }

    fun start() {
        if(!NetworkUtil.isNetworkAvailable(Utils.getContext())){
            runMain { ToastUtils.showShort("网络不可用，请检查您的手机网络是否开启") }
            EventBus.getDefault().post(EventVo(EventConstant.EVENT_TOP_SPEED_FAILED, ""))
            return
        }
        if (!mIsRunning) {
            CfLog.e("=====H5开始切换线路========")
            mIsRunning = true
            setThirdFasterDomain()
            setFasterH5Domain()
        }
    }

    private fun addH5DomainList(domainList: List<String>) {
        domainList.forEachIndexed { _, s ->
            run {
                mCurH5DomainList.add(s)
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

    private fun getFastestH5Domain(isThird: Boolean) {
        scopeNet {
            // 并发请求本地配置的域名 命名参数 uid = "the fastest line" 用于库自动取消任务
            val domainTasks = mCurH5DomainList.map { host ->
                Get<String>(
                    "$host/point.bmp",
                    tag = "the_fastest_line")
                {
                    addHeader("App-RNID", "87jumkljo")
                    setClient {
                        dns(DnsFactory.getDns())
                        trustSSLCertificate()
                    }
                }.transform { data ->
                    CfLog.e("域名：H5------$host")
                    DomainUtil.setH5Url(host)
                    data
                }
            }
            try {
                fastest(domainTasks/*, uid = "the_fastest_line_h5"*/)
            } catch (e: Exception) {
                CfLog.e(e.toString())
                if (e !is CancellationException) {
                    if (isThird) {
                        CfLog.e("切换线路失败，请检查手机网络连接情况")
                        //ToastUtils.showLong("切换网页线路失败，请检查手机网络连接情况")
                        mIsRunning = false
                    }else {
                        getThirdFastestDomain(isH5 = true)
                    }

                }
            }
        }
    }

    /**
     * 三方域名存储地址竞速
     */
    private fun getThirdFastestDomain(isH5: Boolean) {
        scopeNet {
            // 并发请求本地配置的域名 命名参数 uid = "the fastest line" 用于库自动取消任务
            val domainTasks = mThirdDomainList.map { host ->
                Get<String>(
                    "$host",
                    "the_fastest_line_third"
                ) {
                    addHeader("App-RNID", "87jumkljo")
                    setClient {
                        dns(DnsFactory.getDns())
                        trustSSLCertificate()
                    }
                }.transform { data ->
                    CfLog.e("$host")
                    try {
                        var domainJson = AESUtil.decryptData(
                            data,
                            "wnIem4HOB2RKzhiqpaqbZuxtp7T36afAHH88BUht/2Y="
                        )
                        val domain: Domain = Gson().fromJson(domainJson, Domain::class.java)
                        mCurH5DomainList = domain.h5
                        getFastestH5Domain(isThird = true)
                    } catch (e: Exception) {
                        mIsRunning = false
                        //ToastUtils.showLong("切换H5线路失败，获取三方域名存储地址失败，请检查手机网络连接情况")
                    }
                    data
                }
            }
            try {
                fastest(domainTasks, "the_fastest_line_third")
            } catch (e: Exception) {
                CfLog.e(e.toString())
                mIsRunning = false
                //ToastUtils.showLong("切换H5线路失败，获取三方域名存储地址失败，请检查手机网络连接情况")
            }
        }
    }

    private fun setFasterH5Domain() {
        var urls = Utils.getContext().getString(R.string.domain_url_list) // 如果为空或者不正确,转用API的
        /*if (urls.length < 10) {
            urls = getString(R.string.domain_api_list) // 如果域名列表为空,就使用API列表
        }*/
        val list = listOf(*urls.split(";".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray())

        addH5DomainList(list)
        getFastestH5Domain(isThird = false)
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
}