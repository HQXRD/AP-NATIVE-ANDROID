package com.xtree.base.net.fastest

import com.drake.net.Get
import com.drake.net.Net
import com.drake.net.transform.transform
import com.drake.net.utils.fastest
import com.drake.net.utils.scopeNet
import com.google.gson.Gson
import com.xtree.base.R
import com.xtree.base.utils.AESUtil
import com.xtree.base.utils.CfLog
import com.xtree.base.utils.DomainUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.xtree.mvvmhabit.utils.Utils
import okhttp3.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
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
        if (!mIsRunning) {
            CfLog.e("=====H5开始切换线路========")
            mIsRunning = true
//            setThirdFasterDomain()
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
                Get<Response>(
                    getFastestAPI(host,FASTEST_H5_API),
                    tag = "the_fastest_line", block = {
                        setGroup(FASTEST_GOURP_NAME_H5)
                        FASTEST_BLOCK(this)
                    })
            }
            try {
//                fastest(domainTasks/*, uid = "the_fastest_line_h5"*/)

                val jobs = mutableListOf<Job>()
                val mutex = Mutex()
                domainTasks.forEach {
                    val job = launch(Dispatchers.IO) {
                        try {
                            val result = it.await()
                            mutex.withLock {
                                val fullUrl = result.request.url.toString()
                                val url = fullUrl.replace(FASTEST_H5_API, "")

                                result.body?.string()?.let {
                                    val doc: Document = Jsoup.parse(it)
                                    val rootDiv = doc.select("div.root").first()
                                    if (rootDiv != null) {
                                        val dataTargetValue = rootDiv.attr("data-target")
                                        if (dataTargetValue.equals("specialFeature-1691834599183")) {
                                            CfLog.e("域名：H5------$")
                                            Net.cancelGroup(FASTEST_GOURP_NAME_H5)
//                                            DomainUtil.setH5Url(url)
                                        }
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            try {
                                it.cancel()
                            } catch (e: CancellationException) {
                                CfLog.e(e.toString())
                            }
                        }
                    }
                    jobs.add(job)
                }

                jobs.joinAll()

            } catch (e: Exception) {
                CfLog.e(e.toString())
                if (e !is CancellationException) {
                    if (isThird) {
                        CfLog.e("切换线路失败，请检查手机网络连接情况")
                        //ToastUtils.showLong("切换网页线路失败，请检查手机网络连接情况")
                        mIsRunning = false
                    }else {
//                        mCurH5DomainList.clear()
//                        getThirdFastestDomain(isH5 = true)
                    }

                }
            }
        }
    }

//    /**
//     * 三方域名存储地址竞速
//     */
//    private fun getThirdFastestDomain(isH5: Boolean) {
//        scopeNet {
//            // 并发请求本地配置的域名 命名参数 uid = "the fastest line" 用于库自动取消任务
//
//            delay(10000)
//            val domainTasks = mThirdDomainList.map { host ->
//                Get<String>(
//                    "$host",
//                    "the_fastest_line_third", block = FASTEST_BLOCK)
//                    .transform { data ->
//                        CfLog.e("$host")
//                        try {
//                            var domainJson = AESUtil.decryptData(
//                                data,
//                                "wnIem4HOB2RKzhiqpaqbZuxtp7T36afAHH88BUht/2Y="
//                            )
//                            val domain: Domain = Gson().fromJson(domainJson, Domain::class.java)
//                            mCurH5DomainList.addAll(domain.h5)
//                            getFastestH5Domain(isThird = true)
//                        } catch (e: Exception) {
//                            mIsRunning = false
//                            //ToastUtils.showLong("切换H5线路失败，获取三方域名存储地址失败，请检查手机网络连接情况")
//                        }
//                        data
//                    }
//            }
//            try {
//                fastest(domainTasks, "the_fastest_line_third")
//            } catch (e: Exception) {
//                CfLog.e(e.toString())
//                getFastestH5Domain(isThird = true)
//                //ToastUtils.showLong("切换H5线路失败，获取三方域名存储地址失败，请检查手机网络连接情况")
//            }
//        }
//    }

    private fun setFasterH5Domain() {
        var urls = Utils.getContext().getString(R.string.domain_url_list) // 如果为空或者不正确,转用API的
        /*if (urls.length < 10) {
            urls = getString(R.string.domain_api_list) // 如果域名列表为空,就使用API列表
        }*/
        val list = listOf(*urls.split(";".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray())

        addH5DomainList(list)
        getFastestH5Domain(true)
    }

//    /**
//     * 设置三方存储domain域名地址
//     */
//    private fun setThirdFasterDomain() {
//        val urls = Utils.getContext().getString(R.string.domain_url_list_third)
//        val list = listOf(*urls.split(";".toRegex()).dropLastWhile { it.isEmpty() }
//            .toTypedArray())
//        addThirdDomainList(list)
//    }
}