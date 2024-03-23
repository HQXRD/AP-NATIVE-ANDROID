package com.xtree.mine.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.xtree.base.global.SPKeyGlobal
import com.xtree.base.router.RouterActivityPath
import com.xtree.base.router.RouterFragmentPath
import com.xtree.base.utils.AppUtil
import com.xtree.base.utils.CfLog
import com.xtree.base.utils.ClickUtil
import com.xtree.base.utils.RSAEncrypt
import com.xtree.base.utils.UuidUtil
import com.xtree.base.vo.ProfileVo
import com.xtree.mine.BR
import com.xtree.mine.R
import com.xtree.mine.databinding.FragmentChangePwdBinding
import com.xtree.mine.ui.viewmodel.VerifyViewModel
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory
import me.xtree.mvvmhabit.base.BaseFragment
import me.xtree.mvvmhabit.utils.SPUtils
import me.xtree.mvvmhabit.utils.ToastUtils

/**
 * 修改登录密码
 */
@Route(path = RouterFragmentPath.Mine.PAGER_CHANGE_PWD)
class ChangePwdFragment : BaseFragment<FragmentChangePwdBinding, VerifyViewModel>() {
    private lateinit var mProfileVo: ProfileVo

    override fun initView() {
        val json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE)
        mProfileVo = Gson().fromJson(json, ProfileVo::class.java)

        binding.ivwBack.setOnClickListener { requireActivity().finish() }

        binding.ckbEye.setOnCheckedChangeListener { _, isChecked -> setEdtPwd(isChecked, binding.etPwd) }
        binding.ckbEyeNew.setOnCheckedChangeListener { _, isChecked -> setEdtPwd(isChecked, binding.etNewPwd) }
        binding.ckbEyeAgain.setOnCheckedChangeListener { _, isChecked -> setEdtPwd(isChecked, binding.etAgainPwd) }

        binding.ivwCs.setOnClickListener { AppUtil.goCustomerService(context) }

        binding.ivwMsg.setOnClickListener {
            // 消息
            startContainerFragment(RouterFragmentPath.Mine.PAGER_MSG, null)
        }

        binding.tvwConfirm.setOnClickListener {
            if (ClickUtil.isFastClick()) {
                return@setOnClickListener
            }
            var pwd1 = binding.etNewPwd.text.toString().trim()
            val pwd2 = binding.etAgainPwd.text.toString().trim()
            if (pwd1.isEmpty()) {
                ToastUtils.showLong(R.string.txt_pwd_cannot_empty)
                return@setOnClickListener
            }
            //由字号和数字组成的6–16个字符，且必须包含字母和数宇
            val regex2 = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,16}$")
            if (!regex2.matches(pwd1)) {
                ToastUtils.showLong(getString(R.string.txt_correct_pwd))
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(pwd2)) {
                ToastUtils.showLong(R.string.txt_enter_pwd_again)
                return@setOnClickListener
            }
            if (pwd1 != pwd2) {
                ToastUtils.showLong(R.string.txt_pwd_not_same)
                return@setOnClickListener
            }
            val public_key = SPUtils.getInstance().getString(
                "public_key",
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDW+Gv8Xmk+EdTLQUU5fEAzhlVuFrI7GN4a8N\\/B0Oe63ORK8oBE1pK+t5U5Iz89K4zf7nX+tqQvzND5Z57NMwyqTYYb3TMbrKgjqF1K2YW08OaubjpdohMnDIibmPXNtrbRZpOf2xIaApR+wpqGS+Xw0LzKA8JPYDOPO4lseAtqVwIDAQAB"
            )
            pwd1 = RSAEncrypt.encrypt2(pwd1, public_key)
            val map = HashMap<String, String>()
            //map["login_pwd_status"] = null
            map["nonce"] = UuidUtil.getID()
            map["password_original"] = RSAEncrypt.encrypt2(binding.etPwd.text.toString().trim(), public_key)

            map["password"] = pwd1
            map["password_confirmation"] = pwd1

            viewModel.changeLoginPwd(map)
        }
    }

    override fun initViewObservable() {
        viewModel.liveDataChangePwd.observe(this) {
            CfLog.i("******")
            ToastUtils.showLong(R.string.txt_change_succ)
            requireActivity().finish()
            ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation()
        }

    }


    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.fragment_change_pwd
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): VerifyViewModel {
        // return super.initViewModel();
        val factory = AppViewModelFactory.getInstance(activity?.application)
        return ViewModelProvider(this, factory)[VerifyViewModel::class.java]
    }

    /**
     * 设置密码可见不可见
     */
    private fun setEdtPwd(isChecked: Boolean, edt: EditText) {
        if (isChecked) {
            edt.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            edt.transformationMethod = PasswordTransformationMethod.getInstance()
        }
        edt.setSelection(edt.length())
    }
}