package com.xtree.mine.ui.fragment

import PromoRebateModel
import PromoRegModel
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
import java.math.BigDecimal


class RegAccountFragment : BaseFragment<FragmentRegAccountBinding, MineViewModel>() {

    private lateinit var ppw: BasePopupView
    private lateinit var ppwLottery: BasePopupView
    private lateinit var ppwReal: BasePopupView
    private lateinit var ppwSports: BasePopupView
    private lateinit var ppwChess: BasePopupView
    private lateinit var ppwGame: BasePopupView

    private lateinit var mProfileVo: ProfileVo
    val mList = arrayListOf("大招商", "招商", "会员")

    //未知
    private val unknown = 0

    //管理员
    private val rootmanager = 1

    //大招商
    private val dazhaoshang = 2

    //招商
    private val zhaoshang = 3

    //会员
    private val member = 4

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

    private fun initCreateUser() {
        binding.apply {
            btCreateUser.setOnClickListener {
                if (ClickUtil.isFastClick()) {
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
                        UuidUtil.getID(), "insert", mProfileVo.zhaoshang.toString(), type.toString(), etName.text.toString(),
                        etLoginPwd.text.toString(), etUserName.text.toString(),
                        bd.typeLottery.removePercentage(), bd.typeReal.removePercentage(), bd.typeSports.removePercentage(),
                        bd.typeChess.removePercentage(), bd.typeGame.removePercentage()
                    )
                )
            }
        }
    }

    /**
     * 去除百分比符号
     */
    private fun TextView.removePercentage(): String {
        return text.toString().replace("%", "")
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
        PromoRegModel.typeRebate[0].let {
            binding.include0.apply {
                setQuickRebate(it)
            }

        }
    }

    /**
     * 招商快速返点设置
     */
    private fun setRebate1() {
        PromoRegModel.typeRebate[1].let { promoRebate ->
            binding.include1.apply {
                setQuickRebate(promoRebate)
                val list = arrayListOf(0.9, 0.8, 0.7, 0.6, 0.5)
                typeReal.setOnClickListener {
                    //未初始化，创建ppw
                    if (!::ppwReal.isInitialized) {
                        ppwReal = createPpw(promoRebate[1], typeReal, tvRealRebate, list) { ppwReal.dismiss() }
                    }
                    ppwReal.show()
                }

                typeSports.setOnClickListener {
                    //未初始化，创建ppw
                    if (!::ppwSports.isInitialized) {
                        ppwSports = createPpw(promoRebate[2], typeSports, tvSportsRebate, list) { ppwSports.dismiss() }
                    }
                    ppwSports.show()
                }
                typeChess.setOnClickListener {
                    //未初始化，创建ppw
                    if (!::ppwChess.isInitialized) {
                        ppwChess = createPpw(promoRebate[3], typeChess, tvChessRebate, list) { ppwChess.dismiss() }
                    }
                    ppwChess.show()
                }
                typeGame.setOnClickListener {
                    //未初始化，创建ppw
                    if (!::ppwGame.isInitialized) {
                        ppwGame = createPpw(promoRebate[4], typeGame, tvGameRebate, list) { ppwGame.dismiss() }
                    }
                    ppwGame.show()
                }

            }

        }
    }

    /**
     * 会员快速返点设置
     */
    private fun setRebate2() {
        PromoRegModel.typeRebate[2].let { promoRebate ->
            binding.include2.apply {
                setQuickRebate(promoRebate)
                KLog.i("rebate_percentage", mProfileVo.rebate_percentage)
                if (mProfileVo.rebate_percentage == null) {
                    return
                }
                val start = BigDecimal.valueOf(mProfileVo.rebate_percentage.replace("%", "").toDouble())
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
                        ppwLottery = createPpw(promoRebate[0], typeLottery, tvLotteryRebate, arrayList) { ppwLottery.dismiss() }
                    }
                    ppwLottery.show()
                }

            }

        }
    }

    private fun createPpw(
        pr: PromoRebateModel,
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
                    val result = BigDecimal(pr.maxRebate.toString()).subtract(BigDecimal(get(position).toString()))
                    rebate.text = getString(R.string.txt_reg_rebate).plus("$result%")
                    dismiss.invoke()
                }
            }
        }

        adapter.addAll(list)
        return XPopup.Builder(context).asCustom(ListDialog(requireContext(), "", adapter))
    }


    private fun LayoutQuickRebateBinding.setQuickRebate(it: List<PromoRebateModel>) {
        typeLottery.text = it[0].subRebate.toString().plus("%")
        typeLottery.isEnabled = it[0].status
        tvLotteryRebate.text = getString(R.string.txt_reg_rebate).plus(it[0].selfRebate.toString() + "%")
        typeReal.text = it[1].subRebate.toString().plus("%")
        typeReal.isEnabled = it[1].status
        tvRealRebate.text = getString(R.string.txt_reg_rebate).plus(it[1].selfRebate.toString() + "%")
        typeSports.text = it[2].subRebate.toString().plus("%")
        typeSports.isEnabled = it[2].status
        tvSportsRebate.text = getString(R.string.txt_reg_rebate).plus(it[2].selfRebate.toString() + "%")
        typeChess.text = it[3].subRebate.toString().plus("%")
        typeChess.isEnabled = it[3].status
        tvChessRebate.text = getString(R.string.txt_reg_rebate).plus(it[3].selfRebate.toString() + "%")
        typeGame.text = it[4].subRebate.toString().plus("%")
        typeGame.isEnabled = it[4].status
        tvGameRebate.text = getString(R.string.txt_reg_rebate).plus(it[4].selfRebate.toString() + "%")
    }

}