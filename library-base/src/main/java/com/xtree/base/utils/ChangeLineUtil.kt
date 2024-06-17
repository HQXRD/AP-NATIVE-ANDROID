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
import com.xtree.base.net.RetrofitClient
import com.xtree.base.vo.Domain
import me.xtree.mvvmhabit.base.AppManager
import me.xtree.mvvmhabit.utils.ToastUtils
import me.xtree.mvvmhabit.utils.Utils
import java.util.concurrent.CancellationException

class ChangeLineUtil private constructor() {

    init {
        mCurH5DomainList = ArrayList()
        mCurApiDomainList = ArrayList()
        mThirdDomainList = ArrayList()
    }
    companion object {
        @JvmStatic
        val instance: ChangeLineUtil by lazy { ChangeLineUtil() }
        /**
         * 当前预埋域名列表
         */
        lateinit var mCurH5DomainList: MutableList<String>
        lateinit var mCurApiDomainList: MutableList<String>
        lateinit var mThirdDomainList: MutableList<String>
        private var mIsH5DomainEmpty: Boolean = false
        private var mIsRunning: Boolean = false
    }

    fun start(){
        if(!mIsRunning) {
            CfLog.e("=====开始切换线路========")
            mIsRunning = true
            setThirdFasterDomain()
            setFasterApiDomain()
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

    private fun getFastestH5Domain(isThird: Boolean) {
        scopeNet {
            // 并发请求本地配置的域名 命名参数 uid = "the fastest line" 用于库自动取消任务
            val domainTasks = mCurH5DomainList.map { host ->
                Get<String>(
                    "$host/point.bmp",
                    absolutePath = true,
                    tag = RESPONSE,
                    uid = "the_fastest_line_h5"
                ).transform { data ->
                    CfLog.e("域名：H5------$host")
                    NetConfig.host = host
                    DomainUtil.setDomainUrl(host)
                    getFastestApiDomain(isThird = false)
                    data
                }
            }
            try {
                fastest(domainTasks, uid = "the_fastest_line_h5")
            } catch (e: Exception) {
                CfLog.e(e.toString())
                if (e !is CancellationException) {
                    if (isThird) {
                        mIsH5DomainEmpty = true
                    }
                    getThirdFastestDomain(isH5 = true)

                }
            }
        }
    }

    private fun getFastestApiDomain(isThird: Boolean) {
        scopeNet {
            // 并发请求本地配置的域名 命名参数 uid = "the fastest line" 用于库自动取消任务
            val domainTasks = mCurApiDomainList.map { host ->
                Get<String>(
                    "$host/api/bns/4/banners?limit=2", /*api/bns/4/banners?limit=2*/
                    absolutePath = true,
                    tag = RESPONSE,
                    uid = "the_fastest_api"
                ).transform { data ->
                    CfLog.e("域名：api------$host---$isThird")
                    NetConfig.host = host
                    DomainUtil.setApiUrl(host)
                    if(mIsH5DomainEmpty){
                        DomainUtil.setDomainUrl(host)
                    }
                    //RetrofitClient.init() // 重置URL
                    val activity = AppManager.getAppManager().currentActivity()
                    activity.startActivity(Intent(activity, activity.javaClass))
                    CfLog.e("切换线路成功")
                    ToastUtils.showLong("切换线路成功")
                    mIsRunning = false
                    //viewModel?.reNewViewModel?.postValue(null)
                    data
                }
            }
            try {
                fastest(domainTasks, uid = "the_fastest_api")
            } catch (e: Exception) {
                CfLog.e(e.toString())
                if (e !is CancellationException) {
                    if (isThird) {
                        //viewModel?.noWebData?.postValue(null)
                        ToastUtils.showLong("切换线路失败，请检查手机网络连接情况")
                        mIsRunning = false
                    } else {
                        getThirdFastestDomain(isH5 = false)
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
                    absolutePath = true,
                    tag = RESPONSE,
                    uid = "the_fastest_line_third"
                ) { addHeader("App-RNID", "87jumkljo") }.transform { data ->
                    CfLog.i("$host")
                    var domainJson =
                        AESUtil.decryptData(data, "wnIem4HOB2RKzhiqpaqbZuxtp7T36afAHH88BUht/2Y=")
                    val domain: Domain = Gson().fromJson(domainJson, Domain::class.java)
                    mCurApiDomainList = domain.api
                    mCurH5DomainList = domain.h5
                    if (isH5) {
                        getFastestH5Domain(isThird = true)
                    } else {
                        getFastestApiDomain(isThird = true)
                    }
                    data
                }
            }
            try {
                fastest(domainTasks, uid = "the_fastest_line_third")
            } catch (e: Exception) {
                CfLog.e(e.toString())
                if (isH5) {
                    mIsH5DomainEmpty = true
                    getFastestApiDomain(isThird = false)
                } else {
                    //viewModel?.noWebData?.postValue(null)
                    mIsRunning = false
                    ToastUtils.showLong("切换线路失败，获取三方域名存储地址失败，请检查手机网络连接情况")
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