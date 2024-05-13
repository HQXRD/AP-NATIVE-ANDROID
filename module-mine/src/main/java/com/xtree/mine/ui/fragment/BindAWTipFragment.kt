package com.xtree.mine.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.xtree.base.router.RouterFragmentPath
import com.xtree.mine.BR
import com.xtree.mine.R
import com.xtree.mine.databinding.FragmentAwTipBinding
import com.xtree.mine.ui.viewmodel.BindCardViewModel
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory
import me.xtree.mvvmhabit.base.BaseFragment

@Route(path = RouterFragmentPath.Mine.PAGER_BIND_AW_TIP)
class BindAWTipFragment : BaseFragment<FragmentAwTipBinding, BindCardViewModel>() {
    companion object {
        private const val ARG_MARK = "mark"
    }

    override fun initView() {
        binding.ivwBack.setOnClickListener {
            requireActivity().finish()
        }
        when (requireArguments().getString(ARG_MARK)) {
            getString(R.string.txt_bind_zfb_type) -> {
                binding.tvwTitle.text = "如何获取支付宝个人收款码"
                binding.ivBg.setImageResource(R.mipmap.me_alipay_tip)
            }

            getString(R.string.txt_bind_wechat_type) -> {
                binding.tvwTitle.text = "如何获取微信个人收款码"
                binding.ivBg.setImageResource(R.mipmap.me_wechat_tip)
            }
        }
    }

    override fun initContentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.fragment_aw_tip
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): BindCardViewModel {
        val factory = AppViewModelFactory.getInstance(requireActivity().application)
        return ViewModelProvider(this, factory)[BindCardViewModel::class.java]
    }


}