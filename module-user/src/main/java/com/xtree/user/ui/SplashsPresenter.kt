package com.xtree.main.ui

import android.content.Context
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.drake.net.Get
import com.drake.net.NetConfig
import com.drake.net.tag.RESPONSE
import com.drake.net.transform.transform
import com.drake.net.utils.fastest
import com.drake.net.utils.scopeNet
import com.google.gson.Gson
import com.umeng.analytics.MobclickAgent
import noc.imgs.one.base.App
import noc.mpreivgckw.wikbefknpz.iaqlhprmup.BuildConfig
import noc.mpreivgckw.wikbefknpz.iaqlhprmup.R
import noc.mpreivgckw.wikbefknpz.iaqlhprmup.base.BaseRxPresenter
import noc.mpreivgckw.wikbefknpz.iaqlhprmup.base.HttpClient
import noc.mpreivgckw.wikbefknpz.iaqlhprmup.base.constant.BaseConstant
import noc.mpreivgckw.wikbefknpz.iaqlhprmup.base.helper.*
import noc.mpreivgckw.wikbefknpz.iaqlhprmup.base.net.ApiException
import noc.mpreivgckw.wikbefknpz.iaqlhprmup.base.net.BaseModel
import noc.mpreivgckw.wikbefknpz.iaqlhprmup.base.net.HttpSubscriber
import noc.mpreivgckw.wikbefknpz.iaqlhprmup.bean.*
import noc.mpreivgckw.wikbefknpz.iaqlhprmup.bean.ad.AdvertisementBean
import noc.mpreivgckw.wikbefknpz.iaqlhprmup.bean.common.GitAnalyze
import noc.mpreivgckw.wikbefknpz.iaqlhprmup.constant.AppChanel
import noc.mpreivgckw.wikbefknpz.iaqlhprmup.constant.AppDomainTypeEnum
import noc.mpreivgckw.wikbefknpz.iaqlhprmup.constant.SPConstant
import noc.mpreivgckw.wikbefknpz.iaqlhprmup.dagger.Apis
import noc.mpreivgckw.wikbefknpz.iaqlhprmup.mvp.view.SplashView
import noc.mpreivgckw.wikbefknpz.iaqlhprmup.util.*
import noc.mpreivgckw.wikbefknpz.iaqlhprmup.util.domain.FastDomainUtils.Companion.getFastLocalDomain
import noc.mpreivgckw.wikbefknpz.iaqlhprmup.util.encrypt.Des3Utils
import noc.mpreivgckw.wikbefknpz.iaqlhprmup.util.executor.ExecutorFactory
import noc.mpreivgckw.wikbefknpz.iaqlhprmup.util.sensors.SensorsNewUtils
import okhttp3.*
import org.apache.http.conn.ConnectTimeoutException
import retrofit2.HttpException
import java.io.IOException
import java.net.Proxy
import java.net.ProxySelector
import java.net.SocketAddress
import java.net.URI
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SplashsPresenter @Inject constructor(val mContext: Context, val mApis: Apis) :
    BaseRxPresenter<SplashView>() {
    private var mHandler: Handler? = null
    private var mAdBean: AdvertisementBean? = null
    private var mGithubList: List<String>? = null
    private var mInstallCode: String? = ""
    private var mCurrentPosition = 0
    private var mCurrentGithubPosition = 0

    /**
     * 开始请求域名的时间
     */
    private var startTime: Long? = 0

    /**
     * 最后一次请求的域名
     */
    private var mApiDomain: String? = null

    /**
     * 是否是第一次倒计时 第一次显广告
     */
    private var mIsFirstLoaded = true

    /**
     * 第一次请求
     */
    private var mIsFirstRequest = true

    /**
     * 设置是否超时
     */
    private var isTimeOut = false

    //    private var isRepeat = false
    private var repeatNum = 0
    private var isStart = false

    /**
     * 设置是否已请求git域名
     */
    private var mIsFirstGit = true

    /**
     * 请求域名类型
     */
    private var mDomainType = AppDomainTypeEnum.DOMAIN_LOCAL

    fun getCurrentPosition(): Int {
        return mCurrentPosition
    }

    fun getAdBean(): AdvertisementBean? {
        return mAdBean
    }

    companion object {

        const val GO_MAIN = -5

        /**
         * 当前预埋域名列表：可能是本地或者第三方域名
         */
        lateinit var mCurDomainList: HashSet<String>
    }

    init {
        mHandler = object : Handler(mContext.mainLooper) {
            override fun handleMessage(msg: Message) {
                var message: Message? = msg
                if (message?.obj == null) {
                    mLogger.e("msg == null || msg.obj == null")
                    return
                }
                var countdownTime = message.obj as Int
                if (mView != null) {
                    if (countdownTime >= 0) {
                        mView.setCountdownTime(countdownTime)
                        countdownTime--
                        message = Message()
                        message.obj = countdownTime
                        mHandler?.sendMessageDelayed(message, 1000)
                    } else {
                        mIsFirstLoaded = false
                        if (countdownTime == GO_MAIN) {
                            mView.goMain2()
                        } else {
                            mView.goMain()
                        }
                    }
                }
            }
        }
        mCurrentPosition = 0
        mCurDomainList = HashSet()
    }

    fun setInstallCode(codes: String?) {
        if (!StringUtils.isEmpty(codes)) {
            mInstallCode = codes
            SPHelper.saveString(mContext, SPConstant.SP_SHARER_CODE, mInstallCode)
        }
        SPHelper.saveBoolean(
            mContext, SPConstant.APP_IS_ISFIRST_INSTALL, false
        )
    }

    /**
     * 获取预埋域名列表
     */
    fun setFirstRequestConfigHosts() {
        val bootStrap = BootStrapUtil().bootStrap()
        // 后台配置域名列表优先级比本地预埋的高，优先使用后台配置域名
        if (bootStrap != null && bootStrap.domains.size > 0) {
            setServerConfigHosts(bootStrap)
        } else {
            setLocalConfigHosts()
        }
    }

    fun getFastestDomain() {
        startTime = System.currentTimeMillis()
//        if (AppChanel.FU_LI_SHE.value == BuildConfig.app_chanel && BuildConfig.DEBUG) {
//            HttpClient.setFastestDomain("http://qqcnewapi.qzappapia456.com/api/")
//            setSensorsLine("http://qqcnewapi.qzappapia456.com/api/")
//            NetConfig.host = "http://qqcnewapi.qzappapia456.com/api/"
//            mApiDomain = "http://qqcnewapi.qzappapia456.com/api/"
//            // 设置最快访问的域名为当次使用接口域名
//            setCurDomain()
//            initBootStrap()
//            //initUuid()
//            return
//        }
        // cdn接口域名并发择优事件埋点:开始
        scopeNet {
            // 并发请求本地配置的域名 命名参数 uid = "the fastest line" 用于库自动取消任务
            val domainTasks = mCurDomainList.map { host ->
                Get<String>(
                    "$host/index.html",
                    absolutePath = true,
                    tag = RESPONSE,
                    uid = "the_fastest_line"
                ).transform { data ->
                    if (data.contains("hello world")) {
                        
                        mApiDomain = host
                        // 设置最快访问的域名为当次使用接口域名
                        setCurDomain()
//                        if (isTimeOut) {
//                        val code = WalleChannelReader.getChannel(mContext)
//                        val code = "Rt1CVeFZ"
//                        if (!StringUtils.isEmpty(code) && (code == "Rt1CVeFZ" || code == "ktgufze8" || code == "eHSWIrLO")) {
//                            initUuid()
//                        } else {
//                            initBootStrap()
//                        }
                            initBootStrap()
//                            initUuid()
//                        isRepeat = true
//                        }
                    } else {
                        val bean = ApiUrlCheckBean()
                        val gson = Gson()
                        if (data.startsWith("<!DOCTYPE html>")) {
                            bean.response_body =
                                StringUtils.getMiddleStr(data, "<title>", "</title>")
                        } else {
                            bean.response_body = data
                        }
                        bean.url = host
                        SensorsNewUtils.trackEventCheckDomain(
                            "200", gson.toJson(bean)
                        )
                        mCurDomainList.remove(host)
                        if (mCurDomainList.size > 0) {
                            getFastestDomain()
                        } else {
                            changeDomain()
                        }

                    }
                    data
                }
            }
            try {
                fastest(domainTasks, uid = "the_fastest_line")
            } catch (e: Exception) {
                e.printStackTrace()
                changeDomain();
            }
        }
    }

    private fun changeDomain() {
        for (url in mCurDomainList) {
            checkUrlHijack(url)
        }
        mIsFirstRequest = false
        when (mDomainType.value) {
            AppDomainTypeEnum.DOMAIN_LOCAL.value -> {
                // 第一次预埋域名请求失败，请求第三方预埋域名
                initDomains()
            }

            AppDomainTypeEnum.DOMAIN_SERVER.value -> {
                // 用后台配置域名并发请求失败，则退回使用本地域名并发请求获取最快域名
                setLocalConfigHosts()
                getFastestDomain()
            }

            AppDomainTypeEnum.DOMAIN_THIRD.value -> {
                // 切换下一个第三方链接地址
                doNextThirdDomainFile()
                mLogger.e("当前域名成功:切换下一个第三方链接地址")
            }
        }
    }

    /**
     * 域名是否被劫持检测
     */
    private fun checkUrlHijack(host: String) {
        val okHttpClient = OkHttpClient.Builder()
//            .retryOnConnectionFailure(false)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            //防止抓包
            .proxy(Proxy.NO_PROXY)
            .proxySelector(object : ProxySelector() {
                override fun select(p0: URI?): MutableList<Proxy> =
                    Collections.singletonList(Proxy.NO_PROXY)

                override fun connectFailed(p0: URI?, p1: SocketAddress?, p2: IOException?) = Unit
            })
            .build()

        val request =
            Request.Builder().url("$host/index.html").get().addHeader("Accept", "application/json")
                .addHeader("Connection", "close").build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(arg0: Call, response: Response) {
                var result: String?
                if (response.body != null) {
                    result = response.body?.string()
                    if (result != null) {
                        if (result.contains("hello world")) {
                            
                            mApiDomain = host
                            // 设置最快访问的域名为当次使用接口域名
                            setCurDomain()
//                            if (isTimeOut) {
//                            val code = WalleChannelReader.getChannel(mContext)
//                            val code = "Rt1CVeFZ"

//                            if (!StringUtils.isEmpty(code) && (code == "Rt1CVeFZ" || code == "ktgufze8" || code == "eHSWIrLO")) {
//                                initUuid()
//                            } else {
//                                initBootStrap()
//                            }
//                                initUuid()
                                initBootStrap()
//                                isRepeat = true
//                            }
                        } else {
//                                initDomains()
                            val bean = ApiUrlCheckBean()
                            val gson = Gson()
                            try {
                                if (result.startsWith("<!DOCTYPE html>")) {
                                    result = StringUtils.getMiddleStr(result, "<title>", "</title>")
                                }
                                bean.response_body = result
                                bean.url = host
                                SensorsNewUtils.trackEventCheckDomain(
                                    response.code.toString(), gson.toJson(bean)
                                )
                            } catch (e: IllegalStateException) {
                                e.printStackTrace()
                            }

                        }
                    } else {
//                            initDomains()
                        val bean = ApiUrlCheckBean()
                        val gson = Gson()
                        try {
                            bean.response_body = response.body?.string()
                            bean.url = host
                            SensorsNewUtils.trackEventCheckDomain(
                                response.code.toString(), gson.toJson(bean)
                            )
                        } catch (e: IllegalStateException) {
                            e.printStackTrace()
                        }

                    }
                } else {
//                        initDomains()
                    val bean = ApiUrlCheckBean()
                    bean.response_body = null
                    bean.url = host
                    val gson = Gson()
                    SensorsNewUtils.trackEventCheckDomain(
                        response.code.toString(), gson.toJson(bean)
                    )
                }
            }


            override fun onFailure(arg0: Call, arg1: IOException) {
//                    initDomains()
                var code: String = "404"
                if (arg1 is HttpException) {
                    code = arg1.code().toString()
                }
                if (arg1 is ConnectTimeoutException) {
                    code = "500"
                }
                val bean = ApiUrlCheckBean()
                bean.response_body = arg1.message
                bean.url = host
                val gson = Gson()
                SensorsNewUtils.trackEventCheckDomain(
                    code, gson.toJson(bean)
                )
            }
        })
    }


    fun initBootStrap() {
        if (isStart) {
            return
        }
//        if (isRepeat) {
//            loadVideoAds2(true)
//            ToastHelper.showShort(mContext, "无法获取用户信息")
//            return
//        }
        isStart = true
        try {
            val map: TreeMap<String, String>? = BootStrapUtil().getBootStrapArgument(mContext)
            if (mInstallCode?.isNotEmpty() == true) {
                map?.set("code", mInstallCode!!)
            }
            mLogger?.i(GsonTools.createGsonString(map))
            if (BuildConfig.BUILD_TYPE == "debug") {
                FileHelper.saveToSDCard2(
                    "QQBootStrapUtil2.txt", GsonTools.createGsonString(map), true
                )
            }
            mView?.setTipsVisibility(View.VISIBLE, null)
            addSubscribe(
                mApis.bootStrap(map).compose(HttpClient.rxSchedulerHelper())
                    .compose(HttpClient.handleResult())
                    .subscribeWith(object : HttpSubscriber<BootStrap>() {
                        override fun onNext(bootStrap: BootStrap) {
                            isTimeOut = false
                            isStart = false
                            if (bootStrap != null && !TextUtils.isEmpty(bootStrap.woshow_live_domain)) {
                                val urlList: MutableList<String> = ArrayList()
                                urlList.add(bootStrap.woshow_live_domain)
                            }
                            initResult(bootStrap)
                            getHomeBottom()
                            getHotKeywords()
                            loadVideoAds2(false)
                            getShareConfig()
                            getPaymentDomain()
                        }

                        override fun onError(exception: ApiException) {
                            isStart = false
                            repeatNum++
                            if (!isTimeOut) {
                                if (repeatNum < 3) {
                                    val handler = Handler()
                                    handler.postDelayed({
                                        isTimeOut = true
                                        doRequestFailProcess()
                                    }, (3 * 1000).toLong())
                                } else {
                                    isTimeOut = false
                                    ToastHelper.showShort(
                                        mContext,
                                        WordUtil.getString(R.string.string_check_network_retry)
                                    )
                                }
                            } else {
                                ToastHelper.showShort(mContext, exception.message)
                                mView?.setTipsVisibility(View.VISIBLE, exception.message)
                                if (exception.code == 403) {
                                    ToastHelper.showShort(mContext, exception.message)
                                    return
                                }
                                mView?.goMain2()
                            }
                        }
                    })
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * bootstrap接口请求失败处理方式：删除当前域名再次并发获取访问最快的域名
     */
    private fun doRequestFailProcess() {
        // 从列表里删除当前请求域名，从新进行并发请求获取当最快的数据,当前域名列表为空时从第三方获取域名列表
        if (mCurDomainList.size > 0) {
            mCurDomainList.remove(mApiDomain)
            getFastestDomain()
        } else {
            if (mDomainType.value == AppDomainTypeEnum.DOMAIN_SERVER.value) {
                // 之前用的是后台配置域名则退回使用本地域名并发请求获取最快域名
                setLocalConfigHosts()
                getFastestDomain()
            } else {
                // 本地域名失败则获取第三方域名
                doNextThirdDomainFile()
            }
        }
    }

    /**
     * 设置后台配置域名到请求域名集合
     */
    private fun setServerConfigHosts(bootStrap: BootStrap) {
        mCurDomainList.clear()
        bootStrap.domains.indices.forEach {
            mCurDomainList.add(bootStrap.domains[it].api_domain)
        }
        mDomainType = AppDomainTypeEnum.DOMAIN_SERVER
    }

    fun getHomeBottom() {
        addSubscribe(
            mApis.homeBottom.compose(HttpClient.rxSchedulerHelper())
                .compose(HttpClient.handleResult())
                .subscribeWith(object : HttpSubscriber<HomeBottomBean>() {
                    override fun onNext(bottomBean: HomeBottomBean) {
                        if (bottomBean.list != null && bottomBean.list.size > 0) {
                            bottomBean.time = System.currentTimeMillis()
                            SPHelper.saveObject(mContext, SPConstant.HOME_BOTTOM_OBJECT, bottomBean)
                            mLogger.i(GsonTools.createGsonString(bottomBean))
                        }
                    }

                    override fun onError(exception: ApiException) {
                        MyLogUtils.d("exception")
                    }
                })
        )
    }

    /**
     * 设置本地预埋域名到请求域名集合
     */
    private fun setLocalConfigHosts() {
        mCurDomainList.clear()
        mCurDomainList.addAll(HttpClient.getDomains())
        mDomainType = AppDomainTypeEnum.DOMAIN_LOCAL
    }

    /**
     * 获取第三方域名到请求域名集合
     */
    private fun setThirdConfigHosts(bootStrap: BootStrap) {
        mCurDomainList.clear()
        bootStrap.domains.indices.forEach {
            val domain = bootStrap.domains[it]
            mCurDomainList.add(domain.api_domain)
        }
        // 设置域名请求类型为第三方预埋域名
        mDomainType = AppDomainTypeEnum.DOMAIN_THIRD
    }

    /**
     * 设置当前使用域名
     *
     */
    private fun setCurDomain() {
        if (AppChanel.FU_LI_SHE.value == BuildConfig.app_chanel && BuildConfig.DEBUG) {
            return
        }
        if (!TextUtils.isEmpty(mApiDomain)) {
            HttpClient.setFastestDomain(mApiDomain)
            mLogger.i("当前域名成功:$mApiDomain")
            setSensorsLine("" + mApiDomain)
        }
    }

    /**
     * 初始化域名
     *
     */

    private fun initDomains() {
        if (!mIsFirstGit) return
        mIsFirstGit = false
        App.getMainThreadHandler()?.post {
            mView?.setTipsVisibility(
                View.VISIBLE, WordUtil.getString(R.string.splash_init_third_domain)
            )
        }
        mGithubList = getGithub() as List<String>?
        if (mGithubList == null || mGithubList?.size == 0) {
            App.mMainThreadHandler.post {
                ToastHelper.showShort(mContext, WordUtil.getString(R.string.splash_init_fail))
                mView?.setCountdownTime(GO_MAIN)
            }
        } else {
            ExecutorFactory.getInstance().execute { initThirdDomains() }
        }
    }

    private fun initResult(bootStrap: BootStrap) {
        mLogger.e("当前域名成功----" + GsonTools.createGsonString(bootStrap.domains))
        BootStrapUtil().saveBootStrap(bootStrap)
        if (mView == null) {
            return
        }
        useOriginAd(bootStrap)
    }

    /**
     * 读取第三方域名
     *
     * @return
     */
    private fun getGithub(): List<String?>? {
        try {
            // 其它平台正式环境注意修改 R.raw.start 内容,测试环境共用R.raw.uat文件
            if (mGithubList == null) {
                //只有在正式环境才请求
                if (BuildConfig.BUILD_TYPE == "release") {
                    val rawId = R.raw.start
                    val `is` = mContext.resources.openRawResource(rawId)
                    mGithubList = GsonTools.changeGsonToList(`is`, String::class.java)
                }
            }
        } catch (e: Exception) {
            ToastHelper.showShort(mContext, e.message)
            e.printStackTrace()
        }
        return mGithubList
    }

    /**
     * 第三方域名
     */
    private fun initThirdDomains() {
        mGithubList?.size?.let {
            if (mCurrentGithubPosition >= it) {
                App.mMainThreadHandler.post {
                    ToastHelper.showShort(mContext, WordUtil.getString(R.string.splash_init_fail))
                }
                // 线路选择失败事件埋点
//                setSensorsLineFail()
                mDomainType = AppDomainTypeEnum.DOMAIN_FAILURE
                setSensorsLine("")
                sendHandleGoMainMsg()
                return
            }
        }
        mView?.setTipsVisibility(
            View.VISIBLE, StringBuffer(WordUtil.getString(R.string.activating_backup_line)).append(
                mCurrentGithubPosition + 1
            ).append(" ……").toString()
        )
        val okHttpClient = OkHttpClient.Builder()
//            .retryOnConnectionFailure(false)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            //防止抓包
            .proxy(Proxy.NO_PROXY)
            .proxySelector(object : ProxySelector() {
                override fun select(uri: URI): List<Proxy> = listOf(Proxy.NO_PROXY)
                override fun connectFailed(
                    uri: URI,
                    socketAddress: SocketAddress,
                    e: IOException
                ) = Unit
            })
            .build()
//        StringUtils.emptyIfNull(it[mCurrentGithubPosition])
        mGithubList?.let {
            val request = Request.Builder().url(
                if (mGithubList!!.size > mCurrentGithubPosition) StringUtils.emptyIfNull(it[mCurrentGithubPosition])
                else StringUtils.emptyIfNull(it[0])
            ).get().addHeader("Accept", "application/json").addHeader("Connection", "close").build()
            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onResponse(arg0: Call, response: Response) {
                    onThirdResponse(response)
                    MobclickAgent.onEvent(
                        mContext, "ThirdDomins", WordUtil.getString(R.string.success)
                    )
                }

                override fun onFailure(arg0: Call, arg1: IOException) {
                    MobclickAgent.onEvent(
                        mContext, "ThirdDomins", WordUtil.getString(R.string.failed)
                    )
//                        mView?.setTipsVisibility(
//                            View.VISIBLE,
//                            WordUtil.getString(R.string.third_party_domain_access_failed)
//                        )
                    mView?.goMain2()
//                        doNextThirdDomainFile()
                }
            })
        }

    }

    /**
     * 处理三方接口
     *
     * @param response
     */
    private fun onThirdResponse(response: Response) {
        try {
            var result: String? = null
            if (response.body != null) {
                result = response.body?.string()
            }
            if (result == null) {
                return
            }
            val finalGameResponse = GsonTools.changeGsonToBean(result, GitAnalyze::class.java)
            if (finalGameResponse != null && finalGameResponse.body != null) {
                val decryptThirdDomainString = Des3Utils.decryptFromString(
                    finalGameResponse.body.trim(), EncryptUtils.getGameKey()
                )
                mLogger.e(decryptThirdDomainString)
                if (decryptThirdDomainString != null) {
                    val bootStrap =
                        GsonTools.changeGsonToBean(decryptThirdDomainString, BootStrap::class.java)
                    if (bootStrap != null && bootStrap.domains.size > 0) {
                        mCurrentPosition = 0
                        // 重新并发请求一下，获取最快的域名
                        setThirdConfigHosts(bootStrap)
                        getFastestDomain()
                        mLogger.i(GsonTools.createGsonString(bootStrap))
                    } else {
                        doNextThirdDomainFile()
                    }
                } else {
                    doNextThirdDomainFile()
                }
            } else {
                mLogger.i(GsonTools.createGsonString(finalGameResponse))
                doNextThirdDomainFile()
            }
        } catch (e: Exception) {
            doNextThirdDomainFile()
            e.printStackTrace()
        }
    }

    /**
     * 获取下一个第三方域名文件地址
     */
    private fun doNextThirdDomainFile() {
        mGithubList?.size?.let {
            if (it > 0) {
                mCurrentGithubPosition++
                ExecutorFactory.getInstance().execute { initThirdDomains() }
            }
        }
    }

    private fun sendHandleGoMainMsg() {
        val msg = Message()
        msg.obj = GO_MAIN
        mHandler?.sendMessage(msg)
    }

    private fun useOriginAd(bootStrap: BootStrap) {
        if (bootStrap.ads.size > 0 && mView != null) {
            mAdBean = bootStrap.ads[0] as AdvertisementBean
            sendHandler(true, 0)
        } else {
            sendHandleGoMainMsg()
        }
    }

    private fun sendHandler(isOriginUseAd: Boolean, timer: Int) {
        mView?.initBg(mAdBean, object : RequestListener<Any?> {

            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Any?>?,
                isFirstResource: Boolean,
            ): Boolean {
                val msg = Message()
                msg.obj = GO_MAIN
                if (mIsFirstLoaded) {
                    mHandler?.sendMessage(msg)
                    mIsFirstLoaded = false
                }
                return false
            }

            override fun onResourceReady(
                resource: Any?,
                model: Any?,
                target: Target<Any?>?,
                dataSource: DataSource?,
                isFirstResource: Boolean,
            ): Boolean {
                val msg = Message()
                if (isOriginUseAd) {
                    msg.obj = 5
                } else {
                    msg.obj = timer
                }
                if (mIsFirstLoaded) {
                    mHandler?.sendMessage(msg)
                    mIsFirstLoaded = false
                }
                return false
            }
        })
    }

    /**
     * 获取热门搜索
     */
    fun getHotKeywords() {
        addSubscribe(
            mApis.shortVideoHotKeywords().compose(HttpClient.rxSchedulerHelper())
                .compose(HttpClient.handleResult())
                .subscribeWith(object : HttpSubscriber<HotKeywords>() {
                    override fun onNext(resultBean: HotKeywords) {
                        if (resultBean == null) {
//                            ToastHelper.showLong(App.mApplication, WordUtil.getString(R.string.no_date_return))
                            return
                        }
                        var keys = ""
                        if (resultBean.keywords != null && resultBean.keywords.size > 0) {
                            if (resultBean.keywords.size > 3) {
                                for (i in 0..2) keys += resultBean.keywords[i].keyword.toString() + "、"
                            } else {
                                for (bean in resultBean.keywords) {
                                    keys += bean.keyword + "、"
                                }
                            }
                            keys = keys.substring(0, keys.length - 1)
                        }
                        mView?.setSelectKeys(keys)
                    }

                    override fun onError(exception: ApiException) {
//                        ToastHelper.showShort(App.mApplication, exception.message)
                    }
                })
        )
        addSubscribe(
            mApis.notificationContent().compose(HttpClient.rxSchedulerHelper())
                .compose(HttpClient.handleResult())
                .subscribeWith(object : HttpSubscriber<NotificationLocalBeans?>() {
                    override fun onNext(beans: NotificationLocalBeans) {
                        if (beans == null) {
                            return
                        }
                        if (beans.messages != null) {
                            mView?.setNotificationLocal(beans)
                        }
                    }

                    override fun onError(exception: ApiException) {
                        Log.d("log", "del");
                    }
                })
        )
    }


    /**
     * 加载广告
     */
    fun loadVideoAds2(initfalture: Boolean) {
        if (initfalture) mView?.goMain2()
//        addSubscribe(
//            mApis.investment().compose(HttpClient.rxSchedulerHelper())
//                .compose(HttpClient.handleResult())
//                .subscribeWith(object : HttpSubscriber<AdsBeans>() {
//                    override fun onNext(beans: AdsBeans) {
//                        if (beans != null) {
//                            SPHelper.saveObject(App.mApplication, SPConstant.SP_SAVE_ALL_AD, beans)
//                            App.mAdsBeans = beans
//                            saveCached()
//                            if (initfalture) mView?.goMain2()
//                        }
//                    }
//
//                    override fun onError(exception: ApiException) {
//                        try {
//                            val beans = SPHelper.getObject(
//                                App.mApplication, SPConstant.SP_SAVE_ALL_AD
//                            ) as AdsBeans?
//                            if (beans != null) {
//                                App.mAdsBeans = beans
//                                saveCached()
//                            }
//                        } catch (e: Exception) {
//                            mLogger.i(e.message)
//                        }
//                        if (initfalture) mView?.goMain2()
//                    }
//                })
//        )
    }

    /**
     * 缓存到内存缓存
     */
//    private fun saveCached() {
//        App?.mAdsBeans?.ads?.apply {
//            if (this.size > 0) {
//                val advertisementBeanList =
//                    this.get(AdEnum.POSITION_ID_VIDEOPREPIC.value.toString())
//                if (advertisementBeanList != null && advertisementBeanList.size > 0) {
//                    AdvertisementSingleInstance.getInstance().videoBefore = advertisementBeanList[0]
//                }
//                val advertisementBeanList2 = this.get(AdEnum.POSITION_ID_VIDEO.value.toString())
//                if (advertisementBeanList2 != null && advertisementBeanList2.size > 0) {
//                    AdvertisementSingleInstance.getInstance().videoPause = advertisementBeanList2[0]
//                }
//            }
//        }
//    }


    fun adClick() {
        if (mAdBean != null) {
            mAdBean?.isBuryIfClose = true
            mAdBean?.isOpenAd = true
            mView?.advertisement(mContext, mAdBean)
        }
    }


    /**
     * 神策线路成功
     */
    fun setSensorsLine(lineUrl: String) {
        startTime?.let { SensorsNewUtils.trackEventChooseLine(lineUrl, "" + mDomainType.value, it) }
    }

    /**
     * 获取分享地址
     */
    fun getShareConfig() {
        addSubscribe(
            mApis.getShareConfigInfo("system", "common", "contact")
                .compose(HttpClient.rxSchedulerHelper()).compose(HttpClient.handleResult())
                .subscribeWith(object : HttpSubscriber<ConfigBean>() {
                    override fun onNext(configBean: ConfigBean) {
                        SPHelper.saveString(
                            mContext, SPConstant.SP_SAVE_SHARE_URL, configBean.value.site
                        )
                    }

                    override fun onError(exception: ApiException) {}
                })
        )

        addSubscribe(
            mApis.getShareConfigInfo("system", "common", "basic")
                .compose(HttpClient.rxSchedulerHelper()).compose(HttpClient.handleResult())
                .subscribeWith(object : HttpSubscriber<ConfigBean>() {
                    override fun onNext(configBean: ConfigBean) {
                        SPHelper.saveString(
                            mContext,
                            SPConstant.SP_SAVE_TASK_ADS_STATUS,
                            configBean.value.task_ads_status
                        )
                    }

                    override fun onError(exception: ApiException) {}
                })
        )
    }

    /**
     * 实量包
     */
    fun initUuid() {
        val map = HashMap<String, String>()
        val deviceId = DeviceHelper.getUuid(App.mApplication)
        if (TextUtils.isEmpty(deviceId)) {
            addSubscribe(
                mApis.getDeviceId(map).compose(HttpClient.rxSchedulerHelper())
                    .compose(HttpClient.handleResult())
                    .subscribeWith(object : HttpSubscriber<UuidData>() {
                        override fun onNext(uuidData: UuidData) {
                            DeviceInfoWriteAndReadFileUtil.writeDeviceInfoToTxt(uuidData.device_uuid)
                            SPHelper.saveString2(
                                App.mApplication, BaseConstant.SP_SAVE_UUID, uuidData.device_uuid
                            )
                            initBootStrap()
                        }

                        override fun onError(exception: ApiException) {
                            isStart = false
                            repeatNum++
                            if (!isTimeOut) {
                                if (repeatNum < 3) {
                                    val handler = Handler()
                                    handler.postDelayed({
                                        isTimeOut = true
                                        doRequestFailProcess()
                                    }, (3 * 1000).toLong())
                                } else {
                                    isTimeOut = false
                                    ToastHelper.showShort(
                                        mContext,
                                        WordUtil.getString(R.string.string_check_network_retry)
                                    )
                                }
                            } else {
                                ToastHelper.showShort(mContext, exception.message)
                                mView?.setTipsVisibility(View.VISIBLE, exception.message)
                                if (exception.code == 403) {
                                    ToastHelper.showShort(mContext, exception.message)
                                    return
                                }
                                mView?.goMain2()
                            }
                        }
                    })
            )
        } else {
            initBootStrap()
        }
    }

    fun getPaymentDomain() {
        addSubscribe(
            mApis.payDomain.compose(HttpClient.rxSchedulerHelper<BaseModel<PayBean>>())
                .compose(HttpClient.handleResult())
                .subscribeWith(object : HttpSubscriber<PayBean>() {
                    override fun onNext(payBean: PayBean) {
                        if (payBean.list != null && payBean.list.size > 0) {
                            val mList: MutableList<String> = ArrayList()
                            for (i in payBean.list.indices) {
                                if (!StringUtils.isEmpty(payBean.list[i])) {
                                    mList.add(payBean.list[i])
                                }
                            }
                            SPHelper.saveObject(mContext, SPConstant.PAYMENT_DOMAIN_LIST, mList)
                        }
                        val fastDomain = SPHelper.getString(mContext, SPConstant.FAST_USE_DOMAIN)
                        if (TextUtils.isEmpty(fastDomain)) {
                            getFastLocalDomain(mContext)
                        }
                    }

                    override fun onError(exception: ApiException) {
                        mLogger.e(WordUtil.getString(R.string.onError_info) + exception.message)
                    }
                })
        )
    }
}