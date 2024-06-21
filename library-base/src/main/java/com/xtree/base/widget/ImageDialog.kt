package com.xtree.base.widget

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.lxj.xpopup.core.CenterPopupView
import com.xtree.base.R
import com.xtree.base.databinding.DialogAwCodeBinding
import com.xtree.base.global.SPKeyGlobal
import com.xtree.base.utils.CfLog
import com.xtree.base.utils.TagUtils
import me.xtree.mvvmhabit.utils.SPUtils

class ImageDialog(context: Context, private var url: String, var needToken: Boolean, var mCallBack: ICallBack? = null) : CenterPopupView(context) {
    lateinit var binding: DialogAwCodeBinding

    interface ICallBack {
        fun onClose()
    }

    override fun onCreate() {
        super.onCreate()
        initView()
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_aw_code
    }

    private fun initView() {
        binding = DialogAwCodeBinding.bind(findViewById(R.id.ll_root))

        if (needToken) {
            var cookie = ("auth=" + SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN)
                    + ";" + SPUtils.getInstance().getString(SPKeyGlobal.USER_SHARE_COOKIE_NAME)
                    + "=" + SPUtils.getInstance().getString(SPKeyGlobal.USER_SHARE_SESSID)
                    + ";")
            cookie = "auth-expires-in=604800; userPasswordCheck=lowPass; $cookie"
            CfLog.e("cookie: $cookie")

            val glideUrl = GlideUrl(
                url, LazyHeaders.Builder()
                    .addHeader("Content-Type", "application/vnd.sc-api.v1.json")
                    .addHeader("Authorization", "bearer " + SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN))
                    .addHeader("Cookie", cookie)
                    .addHeader("UUID", TagUtils.getDeviceId(context))
                    .build()
            )
            Glide.with(this)
                .load(glideUrl).placeholder(R.mipmap.ic_loading).error(R.mipmap.me_icon_name)
                .into(binding.ivCode)
        } else {
            Glide.with(this)
                .load(url).placeholder(R.mipmap.ic_loading).error(R.mipmap.me_icon_name)
                .into(binding.ivCode)
        }

        binding.ivClose.setOnClickListener {
            dismiss()
        }
    }


}
