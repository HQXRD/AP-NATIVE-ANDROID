package com.xtree.mine.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.xtree.base.adapter.CacheViewHolder
import com.xtree.base.adapter.CachedAutoRefreshAdapter
import com.xtree.base.router.RouterFragmentPath
import com.xtree.base.utils.AppUtil
import com.xtree.base.utils.CfLog
import com.xtree.base.utils.DomainUtil
import com.xtree.base.widget.LoadingDialog
import com.xtree.base.widget.MsgDialog
import com.xtree.base.widget.TipDialog
import com.xtree.mine.BR
import com.xtree.mine.R
import com.xtree.mine.databinding.FragmentBindAwBinding
import com.xtree.mine.databinding.ItemBindAwBinding
import com.xtree.mine.ui.viewmodel.BindCardViewModel
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory
import com.xtree.mine.vo.AWVo
import com.xtree.mine.vo.UserBindBaseVo
import me.xtree.mvvmhabit.base.BaseFragment


/**
 * 绑定支付宝、微信列表
 */
@Route(path = RouterFragmentPath.Mine.PAGER_BIND_ALIPAY_WECHAT)
class BindAlipayWechatFragment : BaseFragment<FragmentBindAwBinding, BindCardViewModel>() {
    private var reBindDialog: BasePopupView? = null
    private val ARG_TOKEN_SIGN = "tokenSign"
    private val ARG_MARK = "mark"
    private var tokenSign: String? = null
    private var mark: String? = null
    lateinit var mAdapter: CachedAutoRefreshAdapter<AWVo>
    override fun onResume() {
        super.onResume()
        getAwList() // 获取支付宝或微信列表
    }

    override fun initView() {
        initArguments()
        binding.apply {
            ivwBack.setOnClickListener { v: View? -> requireActivity().finish() }
            mAdapter = object : CachedAutoRefreshAdapter<AWVo>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
                    return CacheViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bind_aw, parent, false))
                }

                override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
                    val binding2 = ItemBindAwBinding.bind(holder.itemView)
                    val vo = get(position)
                    when (mark) {
                        getString(R.string.txt_bind_zfb_type) -> {
                            binding2.tvwUserName.text = getString(R.string.txt_alipay_phone).plus(vo.wxzfb_id)
                            binding2.tvName.setText(R.string.txt_alipay_name)
                            binding2.tvNickname.setText(R.string.txt_alipay_nickname)
                            binding2.tvCode.setText(R.string.txt_alipay_code)
                        }

                        getString(R.string.txt_bind_wechat_type) -> {
                            binding2.tvwUserName.text = getString(R.string.txt_wechat_phone).plus(vo.wxzfb_id)
                            binding2.tvName.setText(R.string.txt_wechat_name)
                            binding2.tvNickname.setText(R.string.txt_wechat_nickname)
                            binding2.tvCode.setText(R.string.txt_wechat_code)
                        }
                    }

                    binding2.tvNameContent.text = vo.wxzfb_username
                    binding2.tvNicknameContent.text = vo.nickname
                    binding2.tvwBindTime.text = vo.utime
                    binding2.tvwRebind.setOnClickListener {
                        showRebindDialog()
                    }
                    binding2.ivCode.setOnClickListener {
                        XPopup.Builder(context)
                            .asCustom(AWCodeDialog(requireContext(), DomainUtil.getDomain2() + vo.qrcode_url))
                            .show()
                    }
                }
            }
            rcvMain.layoutManager = LinearLayoutManager(context)
            rcvMain.adapter = mAdapter
        }

    }

    private fun showRebindDialog() {
        if (reBindDialog == null) {
            reBindDialog = XPopup.Builder(requireContext())
                .asCustom(
                    MsgDialog(
                        requireContext(),
                        this.getString(R.string.txt_kind_tips),
                        "为了您的资金安全，重新绑定请联系您的上级代理或客服处理",
                        getString(R.string.txt_contact_custom),
                        getString(R.string.txt_back),
                        object : TipDialog.ICallBack {
                            override fun onClickLeft() {
                                AppUtil.goCustomerService(context)
                                reBindDialog?.dismiss();
                            }

                            override fun onClickRight() {
                                reBindDialog?.dismiss();

                            }
                        })
                )
        }
        reBindDialog?.show()
    }

    private fun initArguments() {
        if (arguments != null) {
            tokenSign = requireArguments().getString(ARG_TOKEN_SIGN)
            mark = requireArguments().getString(ARG_MARK)
            when (mark) {
                getString(R.string.txt_bind_zfb_type) -> {
                    binding.tvwTitle.text = getString(R.string.txt_manage_alipay)
                }

                getString(R.string.txt_bind_wechat_type) -> {
                    binding.tvwTitle.text = getString(R.string.txt_manage_wechat)
                }
            }
        }
    }

    override fun initContentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.fragment_bind_aw
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): BindCardViewModel {
        val factory = AppViewModelFactory.getInstance(requireActivity().application)
        return ViewModelProvider(this, factory)[BindCardViewModel::class.java]
    }

    override fun initViewObservable() {
        viewModel.liveDataAWList.observe(this) { vo: UserBindBaseVo<AWVo?> ->
            CfLog.i("******")
            binding.tvwAdd.setOnClickListener { v: View? ->
                CfLog.i("****** add")
                val bundle = Bundle()
                bundle.putString("mark", mark)
                bundle.putString("tokenSign", tokenSign)
                bundle.putString("accountName", vo.accountname)
                startContainerFragment(RouterFragmentPath.Mine.PAGER_BIND_AW_ADD, bundle)
            }
            // 如果是列表为空的情况,跳到增加页,并关闭当前页(关闭是因为有时会提示最多只能绑定0张卡,或者死循环)
            if (vo.banklist.isEmpty()) {
                binding.tvwAdd.performClick() // 跳到增加绑定页
                requireActivity().finish()
                return@observe
            }
            binding.tvTipTitle.visibility = View.VISIBLE

            if (!TextUtils.isEmpty(vo.num)) {
                var txt = ""
                when (mark) {
                    getString(R.string.txt_bind_zfb_type) -> {
                        txt = getString(R.string.txt_alipay)
                    }

                    getString(R.string.txt_bind_wechat_type) -> {
                        txt = getString(R.string.txt_wechat)
                    }
                }
                binding.tvwTip.text = String.format(getString(R.string.txt_bind_most_aw), vo.num, txt)
                val max = vo.num.toInt()
                val count = vo.binded.toInt()
                if (count < max) {
                    binding.tvwAdd.visibility = View.VISIBLE
                } else {
                    binding.tvwAdd.visibility = View.GONE
                }
            }

            if (vo.banklist != null && vo.banklist.isNotEmpty()) {
                CfLog.i("****** 这是列表")
                mAdapter.clear()
                mAdapter.addAll(vo.banklist)
            }
        }
    }

    private fun getAwList() {
        val map = HashMap<String, Any?>()
        map["check"] = tokenSign
        map["mark"] = mark
        map["client"] = "m"
        when (mark) {
            getString(R.string.txt_bind_zfb_type) -> {
                map["action"] = "useronepayzfbinfo"
            }

            getString(R.string.txt_bind_wechat_type) -> {
                map["action"] = "useronepaywxinfo"
            }
        }
        LoadingDialog.show(context)
        viewModel.getAWList(map)
    }
}