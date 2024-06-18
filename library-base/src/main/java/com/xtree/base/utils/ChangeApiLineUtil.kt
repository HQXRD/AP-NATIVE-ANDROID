package com.xtree.base.utils

import android.content.Intent
import com.drake.net.Get
import com.drake.net.NetConfig
import com.drake.net.tag.RESPONSE
import com.drake.net.transform.transform
import com.drake.net.utils.fastest
import com.drake.net.utils.scopeNet
import com.google.gson.Gson
import com.xtree.base.R
import com.xtree.base.vo.Domain
import me.xtree.mvvmhabit.base.AppManager
import me.xtree.mvvmhabit.utils.ToastUtils
import me.xtree.mvvmhabit.utils.Utils
import java.util.concurrent.CancellationException

class ChangeApiLineUtil private constructor() {

    init {
        mCurApiDomainList = ArrayList()
        mThirdDomainList = ArrayList()
    }
    companion object {
        @JvmStatic
        val instance: ChangeApiLineUtil by lazy { ChangeApiLineUtil() }
        /**
         * 当前预埋域名列表
         */
        lateinit var mCurApiDomainList: MutableList<String>
        lateinit var mThirdDomainList: MutableList<String>
        private var mIsRunning: Boolean = false
    }

    fun start(){
        if(!mIsRunning) {
            CfLog.e("=====开始切换线路========")
            mIsRunning = true
            setThirdFasterDomain()
            setFasterApiDomain()
        }
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

    private fun getFastestApiDomain(isThird: Boolean) {
        scopeNet {
            // 并发请求本地配置的域名 命名参数 uid = "the fastest line" 用于库自动取消任务
            val domainTasks = mCurApiDomainList.map { host ->
                Get<String>(
                    "$host/point.bmp", /*api/bns/4/banners?limit=2*/
                    absolutePath = true,
                    tag = RESPONSE,
                    uid = "the_fastest_api"
                ).transform { data ->
                    CfLog.e("域名：api------$host---$isThird")
                    NetConfig.host = host
                    DomainUtil.setApiUrl(host)
                    val activity = AppManager.getAppManager().currentActivity()
                    activity.startActivity(Intent(activity, activity.javaClass))
                    CfLog.e("切换线路成功")
                    ToastUtils.showLong("切换线路成功")
                    mIsRunning = false
                    data
                }
            }
            try {
                fastest(domainTasks, uid = "the_fastest_api")
            } catch (e: Exception) {
                CfLog.e(e.toString())
                if (e !is CancellationException) {
                    if (isThird) {
                        CfLog.e("切换线路失败，请检查手机网络连接情况")
                        ToastUtils.showLong("切换线路失败，请检查手机网络连接情况")
                        mIsRunning = false
                    } else {
                        getThirdFastestDomain()
                    }
                }
            }
        }
    }

    /**
     * 三方域名存储地址竞速
     */
    private fun getThirdFastestDomain() {
        scopeNet {
            // 并发请求本地配置的域名 命名参数 uid = "the fastest line" 用于库自动取消任务
            val domainTasks = mThirdDomainList.map { host ->
                Get<String>(
                    "$host",
                    absolutePath = true,
                    tag = RESPONSE,
                    uid = "the_fastest_line_third"
                ) { addHeader("App-RNID", "87jumkljo") }.transform { data ->
                    CfLog.i("$host")
                    var domainJson =
                        AESUtil.decryptData(data, "wnIem4HOB2RKzhiqpaqbZuxtp7T36afAHH88BUht/2Y=")
                    val domain: Domain = Gson().fromJson(domainJson, Domain::class.java)
                    mCurApiDomainList = domain.api
                    getFastestApiDomain(isThird = true)
                    data
                }
            }
            try {
                fastest(domainTasks, uid = "the_fastest_line_third")
            } catch (e: Exception) {
                CfLog.e(e.toString())
                mIsRunning = false
                ToastUtils.showLong("切换线路失败，获取三方域名存储地址失败，请检查手机网络连接情况")
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
        getFastestApiDomain(isThird = false)
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