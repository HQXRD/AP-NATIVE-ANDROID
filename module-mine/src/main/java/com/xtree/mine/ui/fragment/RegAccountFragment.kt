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
import com.xtree.mine.ui.viewmodel.MineViewModel
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory
import com.xtree.mine.vo.request.AdduserRequest
import me.xtree.mvvmhabit.base.BaseFragment
import me.xtree.mvvmhabit.utils.KLog
import me.xtree.mvvmhabit.utils.SPUtils
import me.xtree.mvvmhabit.utils.ToastUtils
import java.math.BigDecimal


class RegAccountFragment : BaseFragment<FragmentRegAccountBinding, MineViewModel>(), RegInterface {

    private lateinit var ppw: BasePopupView
    private lateinit var ppwLottery: BasePopupView
    private lateinit var ppwReal: BasePopupView
    private lateinit var ppwSports: BasePopupView
    private lateinit var ppwChess: BasePopupView
    private lateinit var ppwGame: BasePopupView

    private lateinit var mProfileVo: ProfileVo

    override fun initView() {
        val json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE)
        mProfileVo = Gson().fromJson(json, ProfileVo::class.java)
        initDialog()
        setRebate0()
        setRebate1()
        setRebate2()

        binding.ckbEye.setOnCheckedChangeListener { _, isChecked -> setEdtPwd(isChecked, binding.etLoginPwd) }
        initCreateUser()
    }

    /**
     * 初始化创建用户点击事件
     */
    private fun initCreateUser() {
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
                        type = dazhaoshang
                        binding.include0
                    }

                    mList[1] -> {
                        type = zhaoshang
                        binding.include1
                    }

                    mList[2] -> {
                        type = member
                        binding.include2
                    }

                    else -> {
                        return@setOnClickListener
                    }
                }

                LoadingDialog.show(requireContext())
                viewModel.adduser(
                    AdduserRequest(
                        UuidUtil.getID(), "insert", mProfileVo.zhaoshang.toString(), type.toString(),
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
                            binding.include2.layout.visibility = View.GONE
                        }

                        1 -> {
                            //INVISIBLE 留个占位
                            binding.include0.layout.visibility = View.INVISIBLE
                            binding.include1.layout.visibility = View.VISIBLE
                            binding.include2.layout.visibility = View.GONE
                        }

                        2 -> {
                            binding.include0.layout.visibility = View.INVISIBLE
                            binding.include1.layout.visibility = View.GONE
                            binding.include2.layout.visibility = View.VISIBLE
                        }
                    }

                    binding.tvSelectType.text = get(position)
                    ppw.dismiss()
                }
            }
        }

        KLog.i("zhaoshang", mProfileVo.zhaoshang)
        binding.tvSelectType.text = when (mProfileVo.zhaoshang) {
            rootmanager, dazhaoshang -> mList[0]
            zhaoshang, member -> {
                binding.include0.layout.visibility = View.INVISIBLE
                binding.include1.layout.visibility = View.GONE
                binding.include2.layout.visibility = View.VISIBLE
                binding.tvSelectType.isEnabled = false
                mList[2]
            }

            else -> {
                binding.tvSelectType.isEnabled = false
                binding.btCreateUser.isEnabled = false
                "未知"
            }
        }
        adapter.addAll(mList)
        ppw = XPopup.Builder(context).asCustom(ListDialog(requireContext(), "", adapter))

    }

    /**
     * 大招商快速返点设置
     */
    private fun setRebate0() {
        binding.include0.apply {
            KLog.i("mProfileVo", Gson().toJson(mProfileVo))
            typeLottery.text = mProfileVo.rebate_percentage
            typeLottery.isEnabled = false
            tvLotteryRebate.text = getString(R.string.txt_reg_rebate).plus("0.0%")

            //状态等于0不显示
            if (mProfileVo.liveStatus == 0) {
                layoutReal.visibility = View.GONE
            } else {
                typeReal.text = mProfileVo.maxLivePoint.toString().plus("%")
                typeReal.isEnabled = false
                tvRealRebate.text = getString(R.string.txt_reg_rebate).plus("0.0%")
            }

            //状态等于0不显示
            if (mProfileVo.sportStatus == 0) {
                layoutSports.visibility = View.GONE
            } else {
                typeSports.text = mProfileVo.maxSportPoint.toString().plus("%")
                typeSports.isEnabled = false
                tvSportsRebate.text = getString(R.string.txt_reg_rebate).plus("0.0%")
            }

            //状态等于0不显示
            if (mProfileVo.pokerStatus == 0) {
                layoutChess.visibility = View.GONE
            } else {
                typeChess.text = mProfileVo.maxPokerPoint.toString().plus("%")
                typeChess.isEnabled = false
                tvChessRebate.text = getString(R.string.txt_reg_rebate).plus("0.0%")
            }

            //状态等于0不显示
            if (mProfileVo.esportsStatus == 0) {
                layoutGame.visibility = View.GONE
            } else {
                typeGame.text = mProfileVo.maxEsportsPoint.toString().plus("%")
                typeGame.isEnabled = false
                tvGameRebate.text = getString(R.string.txt_reg_rebate).plus("0.0%")
            }
        }


    }

    /**
     * 招商快速返点设置
     */
    private fun setRebate1() {
        binding.include1.apply {
            typeLottery.text = mProfileVo.rebate_percentage
            typeLottery.isEnabled = false
            tvLotteryRebate.text = getString(R.string.txt_reg_rebate).plus("0.0%")

            //状态等于0不显示
            if (mProfileVo.liveStatus == 0) {
                layoutReal.visibility = View.GONE
            } else {
                typeReal.text = "0.9%"
                typeReal.isEnabled = true
                tvRealRebate.text = getString(R.string.txt_reg_rebate).plus(NumberUtils.sub(mProfileVo.maxLivePoint, 0.9).toString() + "%")
            }

            //状态等于0不显示
            if (mProfileVo.sportStatus == 0) {
                layoutSports.visibility = View.GONE
            } else {
                typeSports.text = "0.9%"
                typeSports.isEnabled = true
                tvSportsRebate.text = getString(R.string.txt_reg_rebate).plus(NumberUtils.sub(mProfileVo.maxSportPoint, 0.9).toString() + "%")
            }

            //状态等于0不显示
            if (mProfileVo.pokerStatus == 0) {
                layoutChess.visibility = View.GONE
            } else {
                typeChess.text = "0.9%"
                typeChess.isEnabled = true
                tvChessRebate.text = getString(R.string.txt_reg_rebate).plus(NumberUtils.sub(mProfileVo.maxPokerPoint, 0.9).toString() + "%")
            }

            //状态等于0不显示
            if (mProfileVo.esportsStatus == 0) {
                layoutGame.visibility = View.GONE
            } else {
                typeGame.text = "0.9%"
                typeGame.isEnabled = true
                tvGameRebate.text = getString(R.string.txt_reg_rebate).plus(NumberUtils.sub(mProfileVo.maxEsportsPoint, 0.9).toString() + "%")
            }

            val list = arrayListOf(0.9, 0.8, 0.7, 0.6, 0.5)
            typeReal.setOnClickListener {
                //未初始化，创建ppw
                if (!::ppwReal.isInitialized) {
                    ppwReal = createPpw(mProfileVo.maxLivePoint, typeReal, tvRealRebate, list) { ppwReal.dismiss() }
                }
                ppwReal.show()
            }

            typeSports.setOnClickListener {
                //未初始化，创建ppw
                if (!::ppwSports.isInitialized) {
                    ppwSports = createPpw(mProfileVo.maxSportPoint, typeSports, tvSportsRebate, list) { ppwSports.dismiss() }
                }
                ppwSports.show()
            }
            typeChess.setOnClickListener {
                //未初始化，创建ppw
                if (!::ppwChess.isInitialized) {
                    ppwChess = createPpw(mProfileVo.maxPokerPoint, typeChess, tvChessRebate, list) { ppwChess.dismiss() }
                }
                ppwChess.show()
            }
            typeGame.setOnClickListener {
                //未初始化，创建ppw
                if (!::ppwGame.isInitialized) {
                    ppwGame = createPpw(mProfileVo.maxEsportsPoint, typeGame, tvGameRebate, list) { ppwGame.dismiss() }
                }
                ppwGame.show()
            }
        }
    }

    /**
     * 会员快速返点设置
     */
    private fun setRebate2() {
        binding.include2.apply {
            typeLottery.text = mProfileVo.rebate_percentage
            typeLottery.isEnabled = true
            tvLotteryRebate.text = getString(R.string.txt_reg_rebate).plus("0.0%")

            //状态等于0不显示
            if (mProfileVo.liveStatus == 0) {
                layoutReal.visibility = View.GONE
            } else {
                typeReal.text = "0.5%"
                typeReal.isEnabled = false
                tvRealRebate.text = getString(R.string.txt_reg_rebate).plus(NumberUtils.sub(mProfileVo.maxLivePoint, 0.5).toString() + "%")
            }

            //状态等于0不显示
            if (mProfileVo.sportStatus == 0) {
                layoutSports.visibility = View.GONE
            } else {
                typeSports.text = "0.5%"
                typeSports.isEnabled = false
                tvSportsRebate.text = getString(R.string.txt_reg_rebate).plus(NumberUtils.sub(mProfileVo.maxSportPoint, 0.5).toString() + "%")
            }

            //状态等于0不显示
            if (mProfileVo.pokerStatus == 0) {
                layoutChess.visibility = View.GONE
            } else {
                typeChess.text = "0.5%"
                typeChess.isEnabled = false
                tvChessRebate.text = getString(R.string.txt_reg_rebate).plus(NumberUtils.sub(mProfileVo.maxPokerPoint, 0.5).toString() + "%")
            }

            //状态等于0不显示
            if (mProfileVo.esportsStatus == 0) {
                layoutGame.visibility = View.GONE
            } else {
                typeGame.text = "0.5%"
                typeGame.isEnabled = false
                tvGameRebate.text = getString(R.string.txt_reg_rebate).plus(NumberUtils.sub(mProfileVo.maxEsportsPoint, 0.5).toString() + "%")
            }

            KLog.i("rebate_percentage", mProfileVo.rebate_percentage)
            if (mProfileVo.rebate_percentage == null) {
                return
            }
            val max = mProfileVo.rebate_percentage.replace("%", "").toDouble()
            val start = BigDecimal.valueOf(max)
            val end = BigDecimal.ZERO
            val step = BigDecimal.valueOf(0.1)

            val arraySize = ((start - end) / step).toInt() + 1

            val arrayList = ArrayList<Double>()
            for (i in 0 until arraySize) {
                val value = start - i.toBigDecimal() * step
                arrayList.add(value.toDouble())
            }

            typeLottery.setOnClickListener {
                //未初始化，创建ppw
                if (!::ppwLottery.isInitialized) {
                    ppwLottery = createPpw(max, typeLottery, tvLotteryRebate, arrayList) { ppwLottery.dismiss() }
                }
                ppwLottery.show()
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