package com.xtree.mine.ui.fragment

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.gson.Gson
import com.lxj.xpopup.XPopup
import com.xtree.base.global.Constant
import com.xtree.base.global.SPKeyGlobal
import com.xtree.base.router.RouterFragmentPath
import com.xtree.base.utils.ClickUtil
import com.xtree.base.utils.DomainUtil
import com.xtree.base.utils.UuidUtil
import com.xtree.base.vo.ProfileVo
import com.xtree.base.widget.BrowserDialog
import com.xtree.base.widget.LoadingDialog
import com.xtree.mine.BR
import com.xtree.mine.R
import com.xtree.mine.databinding.FragmentFundsPwdBinding
import com.xtree.mine.ui.viewmodel.VerifyViewModel
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory
import me.xtree.mvvmhabit.base.BaseFragment
import me.xtree.mvvmhabit.utils.SPUtils
import me.xtree.mvvmhabit.utils.ToastUtils

/**
 * 修改资金密码
 */
@Route(path = RouterFragmentPath.Mine.PAGER_FUNDS_PWD)
class FundsPwdFragment : BaseFragment<FragmentFundsPwdBinding, VerifyViewModel>() {
    private lateinit var mProfileVo: ProfileVo

    override fun initView() {
        val json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE)
        mProfileVo = Gson().fromJson(json, ProfileVo::class.java)
        mProfileVo.apply {
            //如果没有资金密码，旧的输入用户资金密码框不显示
            //标题为设置资金密码
            if (!has_securitypwd) {
                binding.etPwd.visibility = View.INVISIBLE
                binding.ckbEye.visibility = View.GONE
                binding.tvTitle.setText(R.string.txt_funds_pwd_set)
            }
        }
        binding.ivwBack.setOnClickListener { requireActivity().finish() }

        binding.ckbEye.setOnCheckedChangeListener { _, isChecked -> setEdtPwd(isChecked, binding.etPwd) }
        binding.ckbEyeNew.setOnCheckedChangeListener { _, isChecked -> setEdtPwd(isChecked, binding.etNewPwd) }
        binding.ckbEyeAgain.setOnCheckedChangeListener { _, isChecked -> setEdtPwd(isChecked, binding.etAgainPwd) }

        binding.ivwCs.setOnClickListener {
            // 客服
            val title = getString(R.string.txt_custom_center)
            val url = DomainUtil.getDomain2() + Constant.URL_CUSTOMER_SERVICE
            XPopup.Builder(context).moveUpToKeyboard(false).asCustom(BrowserDialog(requireContext(), title, url)).show()
        }
        binding.ivwMsg.setOnClickListener {
            // 消息
            startContainerFragment(RouterFragmentPath.Mine.PAGER_MSG, null)
        }
        binding.tvwConfirm.setOnClickListener {
            if (ClickUtil.isFastClick()) {
                return@setOnClickListener
            }
            val map = HashMap<String, String>()
            map["nonce"] = UuidUtil.getID16()
            //有资金密码，才传旧的输入用户资金密码框 内容
            if (mProfileVo.has_securitypwd) {
                map["password_original"] = binding.etPwd.text.toString()
            }
            map["password"] = binding.etNewPwd.text.toString()
            map["password_confirmation"] = binding.etAgainPwd.text.toString()
            viewModel.changeFundsPwd(map)
        }
    }

    override fun initViewObservable() {
        viewModel.liveDataChangeFundsPwd.observe(this) {
            if (!mProfileVo.has_securitypwd) {
                ToastUtils.showLong(R.string.txt_set_funds_pwd_succ)
                requireActivity().runOnUiThread {
                    LoadingDialog.show(requireContext())
                }
                viewModel.getProfile2()
            } else {
                ToastUtils.showLong(R.string.txt_change_succ)
                requireActivity().finish()
            }
        }
        viewModel.liveDataProfile2.observe(this) {
            requireActivity().finish()
        }
    }

    override fun initData() {

    }

    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.fragment_funds_pwd
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
     * 设置纯数字密码可见不可见
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