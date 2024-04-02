package com.xtree.mine.ui.fragment

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.xtree.base.adapter.CacheViewHolder
import com.xtree.base.adapter.CachedAutoRefreshAdapter
import com.xtree.base.databinding.ItemTextBinding
import com.xtree.base.global.SPKeyGlobal
import com.xtree.base.utils.ClickUtil
import com.xtree.base.utils.NumberUtils
import com.xtree.base.utils.UuidUtil
import com.xtree.base.vo.ProfileVo
import com.xtree.base.widget.ListDialog
import com.xtree.base.widget.LoadingDialog
import com.xtree.mine.BR
import com.xtree.mine.R
import com.xtree.mine.databinding.FragmentRegAccountBinding
import com.xtree.mine.databinding.LayoutQuickRebateBinding
import com.xtree.mine.ui.viewmodel.MineViewModel
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory
import com.xtree.mine.vo.request.AdduserRequest
import me.xtree.mvvmhabit.base.BaseFragment
import me.xtree.mvvmhabit.utils.KLog
import me.xtree.mvvmhabit.utils.SPUtils
import me.xtree.mvvmhabit.utils.ToastUtils


class RegAccountFragment : BaseFragment<FragmentRegAccountBinding, MineViewModel>(), RegInterface {

    private lateinit var ppw: BasePopupView
    private lateinit var ppwLottery: BasePopupView
    private lateinit var ppwReal: BasePopupView
    private lateinit var ppwSports: BasePopupView
    private lateinit var ppwChess: BasePopupView
    private lateinit var ppwGame: BasePopupView
    private lateinit var ppwLottery1: BasePopupView
    private lateinit var ppwReal1: BasePopupView
    private lateinit var ppwSports1: BasePopupView
    private lateinit var ppwChess1: BasePopupView
    private lateinit var ppwGame1: BasePopupView

    private lateinit var mProfileVo: ProfileVo

    override fun initView() {
        val json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE)
        mProfileVo = Gson().fromJson(json, ProfileVo::class.java)
        initDialog()
        createRebatePpw()
        setRebate(binding.include0, 0)
        setRebate(binding.include1, 1)


        binding.ckbEye.setOnCheckedChangeListener { _, isChecked -> setEdtPwd(isChecked, binding.etLoginPwd) }
        initCreateUser()
    }

    private fun createRebatePpw() {

    }

    /**
     * 初始化创建用户点击事件
     */
    private fun initCreateUser() {
        //mProfileVo.usertype == 0 是会员
        //mProfileVo.usertype == 1 是代理

        binding.apply {
            btCreateUser.setOnClickListener {
                if (ClickUtil.isFastClick()) {
                    return@setOnClickListener
                }
                val name = etName.text.toString().trim()
                val pwd = etLoginPwd.text.toString().trim()
                val nickname = etUserName.text.toString().trim()
                if (name.isEmpty() || name.length < 6 || name[0].toString() == "0" || name[0].toString() == "o") {
                    ToastUtils.showLong("请输入正确格式的用户名")
                    return@setOnClickListener
                }

                //由字号和数字组成的6–16个字符，且必须包含字母和数宇
                val regex2 = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,16}$")
                if (!regex2.matches(pwd)) {
                    ToastUtils.showLong(getString(R.string.txt_correct_pwd))
                    return@setOnClickListener
                }

                if (nickname.isEmpty()) {
                    ToastUtils.showLong("请输入昵称")
                    return@setOnClickListener
                }

                val type: Int
                val bd = when (binding.tvSelectType.text) {
                    mList[0] -> {
                        type = daili
                        binding.include0
                    }

                    mList[1] -> {
                        type = member
                        binding.include1
                    }

                    else -> {
                        return@setOnClickListener
                    }
                }

                LoadingDialog.show(requireContext())
                viewModel.adduser(
                    AdduserRequest(
                        UuidUtil.getID(), "insert", type.toString(),
                        name, pwd, nickname,
                        bd.typeLottery.removePercentage(), bd.typeReal.removePercentage(), bd.typeSports.removePercentage(),
                        bd.typeChess.removePercentage(), bd.typeGame.removePercentage()
                    )
                )
            }
        }
    }


    override fun initViewObservable() {
        viewModel.liveDataAdduser.observe(this) {
            ToastUtils.showLong(it.msg_detail)
        }
    }

    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.fragment_reg_account
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): MineViewModel {

        val factory = AppViewModelFactory.getInstance(requireActivity().application)
        return ViewModelProvider(this, factory)[MineViewModel::class.java]
    }

    private fun setEdtPwd(isChecked: Boolean, edt: EditText) {
        if (isChecked) {
            edt.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            edt.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        edt.setSelection(edt.length())
    }


    private fun initDialog() {
        binding.tvSelectType.setOnClickListener {
            ppw.show()
        }
        val adapter: CachedAutoRefreshAdapter<String> = object : CachedAutoRefreshAdapter<String>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
                return CacheViewHolder(LayoutInflater.from(context).inflate(R.layout.item_text, parent, false))
            }

            override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
                val binding2 = ItemTextBinding.bind(holder.itemView)
                binding2.tvwTitle.text = get(position)
                binding2.tvwTitle.setOnClickListener {
                    when (position) {
                        0 -> {
                            binding.include0.layout.visibility = View.VISIBLE
                            binding.include1.layout.visibility = View.GONE
                        }

                        1 -> {
                            //INVISIBLE 留个占位
                            binding.include0.layout.visibility = View.INVISIBLE
                            binding.include1.layout.visibility = View.VISIBLE
                        }
                    }

                    binding.tvSelectType.text = get(position)
                    ppw.dismiss()
                }
            }
        }

        KLog.i("usertype", mProfileVo.usertype)
        binding.tvSelectType.text = mList[0]
        adapter.addAll(mList)
        ppw = XPopup.Builder(context).asCustom(ListDialog(requireContext(), "", adapter))

    }

    /**
     * 代理/会员快速返点设置
     */
    private fun setRebate(
        include: LayoutQuickRebateBinding,
        type: Int,
    ) {
        include.apply {
            typeLottery.text = mProfileVo.rebate_percentage
            tvLotteryRebate.text = getString(R.string.txt_reg_rebate).plus("0.0%")
            KLog.i("mProfileVo", Gson().toJson(mProfileVo))
            if (mProfileVo.rebate_percentage == null) {
                return
            }
            val max = mProfileVo.rebate_percentage.replace("%", "").toDouble()
            val arrayList = getRebateList(max)
            typeLottery.setOnClickListener {
                if (type == 0) {
                    //未初始化，创建ppw
                    if (!::ppwLottery.isInitialized) {
                        ppwLottery = createPpw(max, typeLottery, tvLotteryRebate, arrayList) { ppwLottery.dismiss() }
                    }
                    ppwLottery.show()
                } else {
                    if (!::ppwLottery1.isInitialized) {
                        ppwLottery1 = createPpw(max, typeLottery, tvLotteryRebate, arrayList) { ppwLottery1.dismiss() }
                    }
                    ppwLottery1.show()
                }
            }

            //状态等于0不显示
            if (mProfileVo.liveStatus == 0) {
                layoutReal.visibility = View.GONE
            } else {
                typeReal.text = mProfileVo.maxLivePoint.toString().plus("%")
                tvRealRebate.text = getString(R.string.txt_reg_rebate).plus("0.0%")
                typeReal.setOnClickListener {
                    if (type == 0) {
                        //未初始化，创建ppw
                        if (!::ppwReal.isInitialized) {
                            ppwReal = createPpw(mProfileVo.maxLivePoint, typeReal, tvRealRebate, getRebateList(mProfileVo.maxLivePoint))
                            { ppwReal.dismiss() }
                        }
                        ppwReal.show()
                    } else {
                        if (!::ppwReal1.isInitialized) {
                            ppwReal1 = createPpw(mProfileVo.maxLivePoint, typeReal, tvRealRebate, getRebateList(mProfileVo.maxLivePoint))
                            { ppwReal1.dismiss() }
                        }
                        ppwReal1.show()
                    }
                }
            }

            //状态等于0不显示
            if (mProfileVo.sportStatus == 0) {
                layoutSports.visibility = View.GONE
            } else {
                typeSports.text = mProfileVo.maxSportPoint.toString().plus("%")
                tvSportsRebate.text = getString(R.string.txt_reg_rebate).plus("0.0%")
                typeSports.setOnClickListener {
                    if (type == 0) {
                        //未初始化，创建ppw
                        if (!::ppwSports.isInitialized) {
                            ppwSports =
                                createPpw(mProfileVo.maxSportPoint, typeSports, tvSportsRebate, getRebateList(mProfileVo.maxSportPoint))
                                { ppwSports.dismiss() }
                        }
                        ppwSports.show()
                    } else {
                        if (!::ppwSports1.isInitialized) {
                            ppwSports1 =
                                createPpw(mProfileVo.maxSportPoint, typeSports, tvSportsRebate, getRebateList(mProfileVo.maxSportPoint))
                                { ppwSports1.dismiss() }
                        }
                        ppwSports1.show()
                    }
                }
            }

            //状态等于0不显示
            if (mProfileVo.pokerStatus == 0) {
                layoutChess.visibility = View.GONE
            } else {
                typeChess.text = mProfileVo.maxPokerPoint.toString().plus("%")
                tvChessRebate.text = getString(R.string.txt_reg_rebate).plus("0.0%")
                typeChess.setOnClickListener {
                    if (type == 0) {
                        //未初始化，创建ppw
                        if (!::ppwChess.isInitialized) {
                            ppwChess =
                                createPpw(mProfileVo.maxPokerPoint, typeChess, tvChessRebate, getRebateList(mProfileVo.maxPokerPoint))
                                { ppwChess.dismiss() }
                        }
                        ppwChess.show()
                    } else {
                        if (!::ppwChess1.isInitialized) {
                            ppwChess1 =
                                createPpw(mProfileVo.maxPokerPoint, typeChess, tvChessRebate, getRebateList(mProfileVo.maxPokerPoint))
                                { ppwChess1.dismiss() }
                        }
                        ppwChess1.show()
                    }
                }
            }

            //状态等于0不显示
            if (mProfileVo.esportsStatus == 0) {
                layoutGame.visibility = View.GONE
            } else {
                typeGame.text = mProfileVo.maxEsportsPoint.toString().plus("%")
                tvGameRebate.text = getString(R.string.txt_reg_rebate).plus("0.0%")
                typeGame.setOnClickListener {
                    if (type == 0) {
                        //未初始化，创建ppw
                        if (!::ppwGame.isInitialized) {
                            ppwGame =
                                createPpw(mProfileVo.maxEsportsPoint, typeGame, tvGameRebate, getRebateList(mProfileVo.maxEsportsPoint))
                                { ppwGame.dismiss() }
                        }
                        ppwGame.show()
                    } else {
                        if (!::ppwGame1.isInitialized) {
                            ppwGame1 =
                                createPpw(mProfileVo.maxEsportsPoint, typeGame, tvGameRebate, getRebateList(mProfileVo.maxEsportsPoint))
                                { ppwGame1.dismiss() }
                        }
                        ppwGame1.show()
                    }
                }
            }
        }


    }


    /**
     * 创建返点弹窗
     */
    private fun createPpw(
        max: Double,
        type: TextView,
        rebate: TextView,
        list: List<Double>,
        dismiss: () -> Unit
    ): BasePopupView {
        val adapter: CachedAutoRefreshAdapter<Double> = object : CachedAutoRefreshAdapter<Double>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
                return CacheViewHolder(LayoutInflater.from(context).inflate(R.layout.item_text, parent, false))
            }

            override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
                val binding2 = ItemTextBinding.bind(holder.itemView)
                binding2.tvwTitle.text = get(position).toString().plus("%")
                binding2.tvwTitle.setOnClickListener {
                    type.text = get(position).toString().plus("%")

                    val result = NumberUtils.sub(max, get(position)).toString()
                    rebate.text = getString(R.string.txt_reg_rebate).plus("$result%")
                    dismiss.invoke()
                }
            }
        }

        adapter.addAll(list)
        return XPopup.Builder(context).asCustom(ListDialog(requireContext(), "", adapter))
    }


}