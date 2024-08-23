package com.xtree.main.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.xtree.base.global.SPKeyGlobal
import com.xtree.base.router.RouterActivityPath
import com.xtree.base.utils.DomainUtil
import com.xtree.base.utils.TagUtils
import com.xtree.main.BR
import com.xtree.main.BuildConfig
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
@Route(path = RouterActivityPath.Main.PAGER_SPLASH)
class SplashActivity : BaseActivity<ActivitySplashBinding?, SplashViewModel?>() {

    private val MSG_IN_MAIN: Int = 100 // 消息类型
    private val DELAY_MILLIS: Long = 100L // 延长时间
    private var mSavedInstanceState: Bundle? = null
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
        binding?.root?.postDelayed({ inMain() }, DELAY_MILLIS)
    }

    private fun init() {
        val api = getString(R.string.domain_api) // 不能为空,必须正确
        val url = getString(R.string.domain_url) // 如果为空或者不正确,转用API的

        if (api.startsWith("http://") || api.startsWith("https://")) {
            DomainUtil.setApiUrl(api)
        }

        if (url.startsWith("http://") || url.startsWith("https://")) {
            DomainUtil.setH5Url(url)
        } else {
            DomainUtil.setH5Url(api)
        }
    }

    override fun initViewObservable() {
        viewModel?.inMainData?.observe(this) {
            mHandler.sendEmptyMessageDelayed(MSG_IN_MAIN, DELAY_MILLIS)
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