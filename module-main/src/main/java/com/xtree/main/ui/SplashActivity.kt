package com.xtree.main.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.launcher.ARouter
import com.drake.net.Get
import com.drake.net.NetConfig
import com.drake.net.tag.RESPONSE
import com.drake.net.transform.transform
import com.drake.net.utils.fastest
import com.drake.net.utils.scopeNet
import com.xtree.base.BuildConfig
import com.xtree.base.global.Constant
import com.xtree.base.global.SPKeyGlobal
import com.xtree.base.net.RetrofitClient
import com.xtree.base.router.RouterActivityPath
import com.xtree.base.utils.CfLog
import com.xtree.base.utils.DomainUtil
import com.xtree.main.BR
import com.xtree.main.R
import com.xtree.main.databinding.ActivitySplashBinding
import com.xtree.main.ui.viewmodel.SplashViewModel
import com.xtree.main.ui.viewmodel.factory.AppViewModelFactory
import me.xtree.mvvmhabit.base.BaseActivity
import me.xtree.mvvmhabit.utils.SPUtils
import me.xtree.mvvmhabit.utils.ToastUtils

/**
 * 冷启动
 */
class SplashActivity : BaseActivity<ActivitySplashBinding?, SplashViewModel?>() {
    var mSavedInstanceState: Bundle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSavedInstanceState = savedInstanceState;
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_splash
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initView() {
        init()
        setFasterDomain()
    }

    companion object {

        /**
         * 当前预埋域名列表
         */
        lateinit var mCurDomainList: HashSet<String>
    }

    init {
        mCurDomainList = HashSet()
    }

    fun addDomainList(domainList: List<String>) {
        domainList.forEachIndexed { _, s ->
            run {
                mCurDomainList.add(s)
            }
        }
    }

    fun getFastestDomain() {
        scopeNet {
            // 并发请求本地配置的域名 命名参数 uid = "the fastest line" 用于库自动取消任务
            val domainTasks = mCurDomainList.map { host ->
                Get<String>(
                    "$host",
                    absolutePath = true,
                    tag = RESPONSE,
                    uid = "the_fastest_line"
                ).transform { data ->
                    CfLog.e("$host")
                    NetConfig.host = host
                    DomainUtil.setDomainUrl(host)
                    RetrofitClient.init()
                    viewModel?.reNewViewModel?.postValue(null)
                    data
                }
            }
            try {
                fastest(domainTasks, uid = "the_fastest_line")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun init() {
        val url = getString(R.string.domain_url)
        if (url.startsWith("http://") || url.startsWith("https://")) {
            DomainUtil.setDomainUrl(url)
        }
        if (BuildConfig.DEBUG) {
            ToastUtils.showLong("Debug model")
        }
    }

    /**
     * 线路竞速
     */
    private fun setFasterDomain() {
        val urls = getString(R.string.domain_url_list)
        val list = listOf(*urls.split(";".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray())
        addDomainList(list)
        getFastestDomain()
    }

    override fun initViewObservable() {
        viewModel?.inMainData?.observe(this) { inMain() }
        viewModel?.reNewViewModel?.observe(this) {
            initViewDataBinding(mSavedInstanceState)
            val token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN)
            if (!TextUtils.isEmpty(token)) {
                viewModel?.getFBGameTokenApi()
            } else {
                inMain()
            }
        }
    }

    /**
     * 进入主页面
     */
    private fun inMain() {

        /*if (TextUtils.isEmpty(SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN))) {
            ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER)
                .withInt(
                    Constant.LoginRegisterActivity_ENTER_TYPE,
                    Constant.LoginRegisterActivity_LOGIN_TYPE
                )
                .navigation();
            finish();
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish();
        }*/
        startActivity(Intent(this, MainActivity::class.java))
        finish();

    }

    override fun initViewModel(): SplashViewModel? {
        val factory: AppViewModelFactory =
            AppViewModelFactory.getInstance(
                application
            )
        return ViewModelProvider(this, factory)[SplashViewModel::class.java]
    }
}