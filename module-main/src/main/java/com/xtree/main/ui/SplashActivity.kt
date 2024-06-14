package com.xtree.main.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.drake.net.Get
import com.drake.net.NetConfig
import com.drake.net.tag.RESPONSE
import com.drake.net.transform.transform
import com.drake.net.utils.fastest
import com.drake.net.utils.scopeNet
import com.google.gson.Gson
import com.xtree.base.global.SPKeyGlobal
import com.xtree.base.net.RetrofitClient
import com.xtree.base.router.RouterActivityPath
import com.xtree.base.utils.AESUtil
import com.xtree.base.utils.CfLog
import com.xtree.base.utils.DomainUtil
import com.xtree.base.utils.TagUtils
import com.xtree.main.BR
import com.xtree.main.BuildConfig
import com.xtree.main.R
import com.xtree.main.bean.Domain
import com.xtree.main.databinding.ActivitySplashBinding
import com.xtree.main.ui.viewmodel.SplashViewModel
import com.xtree.main.ui.viewmodel.factory.AppViewModelFactory
import me.xtree.mvvmhabit.base.BaseActivity
import me.xtree.mvvmhabit.bus.Messenger
import me.xtree.mvvmhabit.utils.SPUtils
import me.xtree.mvvmhabit.utils.ToastUtils
import java.util.concurrent.CancellationException

/**
 * 冷启动
 */
@Route(path = RouterActivityPath.Main.PAGER_SPLASH)
class SplashActivity : BaseActivity<ActivitySplashBinding?, SplashViewModel?>() {

    private val MSG_IN_MAIN: Int = 100 // 消息类型
    private val DELAY_MILLIS: Long = 100L // 延长时间
    private var mSavedInstanceState: Bundle? = null
    private var mIsH5DomainEmpty: Boolean = false
    private var mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            inMain()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSavedInstanceState = savedInstanceState

        if (BuildConfig.DEBUG) {
            ToastUtils.showLong("Debug Model")
        }
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_splash
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initView() {
        init()
        initTag()
        setThirdFasterDomain()
        setFasterApiDomain()
        setFasterH5Domain()
        //setFasterApiDomain()
        //getThirdFastestDomain()
    }

    companion object {

        /**
         * 当前预埋域名列表
         */
        lateinit var mCurH5DomainList: MutableList<String>
        lateinit var mCurApiDomainList: MutableList<String>
        lateinit var mThirdDomainList: MutableList<String>
    }

    init {
        mCurH5DomainList = ArrayList()
        mCurApiDomainList = ArrayList()
        mThirdDomainList = ArrayList()
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
                e.printStackTrace()
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
                    "$host/point.bmp",
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
                    RetrofitClient.init() // 重置URL
                    viewModel?.reNewViewModel?.postValue(null)
                    data
                }
            }
            try {
                fastest(domainTasks, uid = "the_fastest_api")
            } catch (e: Exception) {
                CfLog.e(e.toString())
                e.printStackTrace()
                if (e !is CancellationException) {
                    if (isThird) {
                        viewModel?.noWebData?.postValue(null)
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
                e.printStackTrace()
                if (!isH5) {
                    viewModel?.noWebData?.postValue(null)
                } else {
                    mIsH5DomainEmpty = true
                    getFastestApiDomain(isThird = false)
                }
            }
        }
    }

    private fun init() {
        val api = getString(R.string.domain_api) // 不能为空,必须正确
        val url = getString(R.string.domain_url) // 如果为空或者不正确,转用API的

        if (api.startsWith("http://") || api.startsWith("https://")) {
            DomainUtil.setApiUrl(url)
        }

        if (url.startsWith("http://") || url.startsWith("https://")) {
            DomainUtil.setDomainUrl(url)
        } else {
            DomainUtil.setDomainUrl(api)
        }
    }

    /**
     * 线路竞速
     */
    private fun setFasterApiDomain() {
        val apis = getString(R.string.domain_api_list) // 不能为空,必须正确
        val apiList = listOf(*apis.split(";".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray())
        addApiDomainList(apiList)
    }

    private fun setFasterH5Domain() {
        var urls = getString(R.string.domain_url_list) // 如果为空或者不正确,转用API的
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
        val urls = getString(R.string.domain_url_list_third)
        val list = listOf(*urls.split(";".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray())
        addThirdDomainList(list)
    }

    override fun initViewObservable() {
        viewModel?.inMainData?.observe(this) {
            mHandler.sendEmptyMessageDelayed(MSG_IN_MAIN, DELAY_MILLIS)
        }
        viewModel?.reNewViewModel?.observe(this) {
            RetrofitClient.init()
            AppViewModelFactory.init()
            //解除Messenger注册
            Messenger.getDefault().unregister(viewModel)
            if (viewModel != null) {
                viewModel!!.removeRxBus()
            }
            if (binding != null) {
                binding!!.unbind()
            }
            viewModel = null
            initViewDataBinding(mSavedInstanceState)
            viewModel?.setModel(AppViewModelFactory.getInstance(application).getRepository())
            val token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN)
            if (!TextUtils.isEmpty(token)) {
                CfLog.i("getFBGameTokenApi init")
                viewModel?.getFBGameTokenApi()
                viewModel?.getFBXCGameTokenApi()
                viewModel?.getPMXCGameTokenApi()
                viewModel?.getPMGameTokenApi()
            } else {
                mHandler.sendEmptyMessageDelayed(MSG_IN_MAIN, DELAY_MILLIS)
            }
        }
        viewModel?.noWebData?.observe(this) {
            ToastUtils.showLong("网络异常，请检查手机网络连接情况")
            //binding?.root?.postDelayed({ finish() }, DELAY_MILLIS)
            binding?.root?.postDelayed({ inMain() }, DELAY_MILLIS)
        }
    }

    private fun initTag() {
        val token = arrayOfNulls<String>(2)
        token[0] = getString(R.string.mixpanel_token)
        token[1] = getString(R.string.ms_secret_key)
        val channel = getString(R.string.channel_name)
        val userId = SPUtils.getInstance().getString(SPKeyGlobal.USER_ID)
        var isTag = resources.getBoolean(R.bool.is_tag) && !BuildConfig.DEBUG
        TagUtils.init(baseContext, token, channel, userId, isTag)
        TagUtils.tagDailyEvent(baseContext)
    }

    /**
     * 进入主页面
     */
    private fun inMain() {
        mHandler.removeMessages(MSG_IN_MAIN)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun initViewModel(): SplashViewModel? {
        val factory: AppViewModelFactory =
            AppViewModelFactory.getInstance(
                application
            )
        return ViewModelProvider(this, factory)[SplashViewModel::class.java]
    }
}