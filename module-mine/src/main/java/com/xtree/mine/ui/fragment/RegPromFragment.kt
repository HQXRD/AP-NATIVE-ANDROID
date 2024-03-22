package com.xtree.mine.ui.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.xtree.base.adapter.CacheViewHolder
import com.xtree.base.adapter.CachedAutoRefreshAdapter
import com.xtree.base.databinding.ItemTextBinding
import com.xtree.base.global.SPKeyGlobal
import com.xtree.base.utils.CfLog
import com.xtree.base.utils.NumberUtils
import com.xtree.base.utils.UuidUtil
import com.xtree.base.vo.ProfileVo
import com.xtree.base.widget.ListDialog
import com.xtree.mine.BR
import com.xtree.mine.R
import com.xtree.mine.databinding.FragmentPromLinksBinding
import com.xtree.mine.ui.viewmodel.MineViewModel
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory
import com.xtree.mine.vo.MarketingVo
import me.xtree.mvvmhabit.base.BaseFragment
import me.xtree.mvvmhabit.utils.KLog
import me.xtree.mvvmhabit.utils.SPUtils
import me.xtree.mvvmhabit.utils.ToastUtils
import java.math.BigDecimal


class RegPromFragment : BaseFragment<FragmentPromLinksBinding, MineViewModel>(), RegInterface {

    private lateinit var ppw: BasePopupView
    private lateinit var ppwLottery: BasePopupView
    private lateinit var ppwReal: BasePopupView
    private lateinit var ppwSports: BasePopupView
    private lateinit var ppwChess: BasePopupView
    private lateinit var ppwGame: BasePopupView

    private lateinit var linkPpw: BasePopupView

    private lateinit var mProfileVo: ProfileVo


    override fun initView() {
        val json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE)
        mProfileVo = Gson().fromJson(json, ProfileVo::class.java)
        initDialog()
        setRebate0()
        saveUpdate()
    }

    /**
     * 保存更新按钮点击事件
     */
    private fun saveUpdate() {
        binding.btSaveUpdate.setOnClickListener {
            if (binding.tvSelectType.text.toString().isEmpty()) {
                ToastUtils.showLong(getString(R.string.txt_choose_type_pls))
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
            val map = HashMap<String, Any>()
            map["emails"] = arrayListOf<Any>()
            map["nonce"] = UuidUtil.getID()
            map["qqs"] = arrayListOf<Any>()
            map["usertype"] = mProfileVo.zhaoshang.toString()
            map["zhaoshang"] = type.toString()
            map["point"] = bd.typeLottery.removePercentage()
            map["livepoint"] = bd.typeReal.removePercentage()
            map["sportpoint"] = bd.typeSports.removePercentage()
            map["pokerpoint"] = bd.typeChess.removePercentage()
            map["esportspoint"] = bd.typeGame.removePercentage()

            viewModel.postMarketing(map, requireContext())

        }
    }

    override fun initData() {
        viewModel.marketing()
    }

    override fun initViewObservable() {
        viewModel.liveDataMarketing.observe(this) {
            if (it.links.isNotEmpty()) {
                val linkList = ArrayList<String>()
                for (i in it.domainList) {
                    linkList.add(i + it.links[0].domain)
                }
                binding.tvLink.text = linkList[0]
                val adapter: CachedAutoRefreshAdapter<String> = object : CachedAutoRefreshAdapter<String>() {
                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
                        return CacheViewHolder(LayoutInflater.from(context).inflate(R.layout.item_text, parent, false))
                    }

                    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
                        val binding2 = ItemTextBinding.bind(holder.itemView)
                        binding2.tvwTitle.text = get(position)
                        binding2.tvwTitle.setOnClickListener {
                            binding.tvLink.text = get(position)
                            linkPpw.dismiss()
                        }
                    }
                }

                adapter.addAll(linkList)
                linkPpw = XPopup.Builder(context).asCustom(ListDialog(requireContext(), "", adapter))
                binding.tvLink.setOnClickListener {
                    linkPpw.show()
                }
                binding.tvwCopy.setOnClickListener { copy(binding.tvLink.text.toString()) }

                binding.tvSelectType.text = when (it.links[0].zhaoshang.toInt()) {
                    rootmanager, dazhaoshang -> {
                        binding.include0.layout.visibility = View.VISIBLE
                        binding.include1.layout.visibility = View.GONE
                        binding.include2.layout.visibility = View.GONE
                        mList[0]
                    }

                    zhaoshang -> {
                        binding.include0.layout.visibility = View.INVISIBLE
                        binding.include1.layout.visibility = View.VISIBLE
                        binding.include2.layout.visibility = View.GONE
                        mList[1]
                    }

                    member -> {
                        binding.include0.layout.visibility = View.INVISIBLE
                        binding.include1.layout.visibility = View.GONE
                        binding.include2.layout.visibility = View.VISIBLE
                        mList[2]
                    }

                    else -> {
                        "未知"
                    }
                }
            }

            if (it.prizeGroups != null) {
                /**
                 * 返点详情弹窗
                 */
                val rebateDialog = XPopup.Builder(context).asCustom(RebateDetailsDialog(requireContext(),it.prizeGroups))
                binding.tvRebateDetails.setOnClickListener {
                      rebateDialog.show()
                }
            }

            setRebate1(it)
            setRebate2(it.selectedPoint)
        }

        viewModel.liveDataPostMark.observe(this) {
            //保存更新后更新本地数据
            viewModel.marketing()
        }
    }

    private fun copy(txt: String) {
        CfLog.d(txt)
        val cm = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val cd = ClipData.newPlainText("txt", txt)
        cm.setPrimaryClip(cd)
        ToastUtils.showLong(R.string.txt_copied)
    }

    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.fragment_prom_links
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): MineViewModel {
        val factory = AppViewModelFactory.getInstance(requireActivity().application)
        return ViewModelProvider(this, factory)[MineViewModel::class.java]
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
        adapter.addAll(mList)
        ppw = XPopup.Builder(context).asCustom(ListDialog(requireContext(), "", adapter))

        binding.tvSelectType.text = when (mProfileVo.zhaoshang) {
            rootmanager, dazhaoshang -> ""
            zhaoshang, member -> {
                binding.include0.layout.visibility = View.INVISIBLE
                binding.include1.layout.visibility = View.GONE
                binding.include2.layout.visibility = View.VISIBLE
                binding.tvSelectType.isEnabled = false
                mList[2]
            }

            else -> {
                binding.tvSelectType.isEnabled = false
                binding.btSaveUpdate.isEnabled = false
                "未知"
            }
        }
    }

    /**
     * 大招商快速返点设置
     */
    private fun setRebate0() {
        binding.include0.apply {
            typeLottery.text = mProfileVo.rebate_percentage
            typeLottery.isEnabled = false
            tvLotteryRebate.text = getString(R.string.txt_reg_rebate).plus("0.0%")

            typeReal.text = mProfileVo.maxLivePoint.toString().plus("%")
            typeReal.isEnabled = false
            tvRealRebate.text = getString(R.string.txt_reg_rebate).plus("0.0%")

            typeSports.text = mProfileVo.maxSportPoint.toString().plus("%")
            typeSports.isEnabled = false
            tvSportsRebate.text = getString(R.string.txt_reg_rebate).plus("0.0%")

            typeChess.text = mProfileVo.maxEsportsPoint.toString().plus("%")
            typeChess.isEnabled = false
            tvChessRebate.text = getString(R.string.txt_reg_rebate).plus("0.0%")

            typeGame.text = mProfileVo.maxPokerPoint.toString().plus("%")
            typeGame.isEnabled = false
            tvGameRebate.text = getString(R.string.txt_reg_rebate).plus("0.0%")
        }


    }

    /**
     * 招商快速返点设置
     */
    private fun setRebate1(vo: MarketingVo) {
        binding.include1.apply {
            typeLottery.text = mProfileVo.rebate_percentage
            typeLottery.isEnabled = false
            tvLotteryRebate.text = getString(R.string.txt_reg_rebate).plus("0.0%")

            if (vo.livePoint == "1.0") {
                vo.livePoint = "0.9"
            }
            typeReal.text = vo.livePoint.plus("%")
            typeReal.isEnabled = true
            tvRealRebate.text = getString(R.string.txt_reg_rebate)
                .plus(NumberUtils.sub(mProfileVo.maxLivePoint, vo.livePoint.toDouble()).toString() + "%")

            if (vo.sportPoint == "1.0") {
                vo.sportPoint = "0.9"
            }
            typeSports.text = vo.sportPoint.plus("%")
            typeSports.isEnabled = true
            tvSportsRebate.text = getString(R.string.txt_reg_rebate)
                .plus(NumberUtils.sub(mProfileVo.maxSportPoint, vo.sportPoint.toDouble()).toString() + "%")

            if (vo.chessPoint == "1.0") {
                vo.chessPoint = "0.9"
            }
            typeChess.text = vo.chessPoint.plus("%")
            typeChess.isEnabled = true
            tvChessRebate.text = getString(R.string.txt_reg_rebate)
                .plus(NumberUtils.sub(mProfileVo.maxEsportsPoint, vo.chessPoint.toDouble()).toString() + "%")

            if (vo.esportsPoint == "1.0") {
                vo.esportsPoint = "0.9"
            }
            typeGame.text = vo.esportsPoint.plus("%")
            typeGame.isEnabled = true
            tvGameRebate.text = getString(R.string.txt_reg_rebate)
                .plus(NumberUtils.sub(mProfileVo.maxPokerPoint, vo.esportsPoint.toDouble()).toString() + "%")


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
                    ppwChess = createPpw(mProfileVo.maxEsportsPoint, typeChess, tvChessRebate, list) { ppwChess.dismiss() }
                }
                ppwChess.show()
            }
            typeGame.setOnClickListener {
                //未初始化，创建ppw
                if (!::ppwGame.isInitialized) {
                    ppwGame = createPpw(mProfileVo.maxPokerPoint, typeGame, tvGameRebate, list) { ppwGame.dismiss() }
                }
                ppwGame.show()
            }

        }


    }

    /**
     * 会员快速返点设置
     */
    private fun setRebate2(selectedPoint: String) {
        binding.include2.apply {
            val max = mProfileVo.rebate_percentage.replace("%", "").toDouble()

            typeLottery.text = selectedPoint.plus("%")  //
            typeLottery.isEnabled = true
            val self = NumberUtils.sub(max, selectedPoint.toDouble())
            tvLotteryRebate.text = getString(R.string.txt_reg_rebate).plus("$self%")

            typeReal.text = "0.5%"
            typeReal.isEnabled = false
            tvRealRebate.text = getString(R.string.txt_reg_rebate).plus(NumberUtils.sub(mProfileVo.maxLivePoint, 0.5).toString() + "%")

            typeSports.text = "0.5%"
            typeSports.isEnabled = false
            tvSportsRebate.text = getString(R.string.txt_reg_rebate).plus(NumberUtils.sub(mProfileVo.maxSportPoint, 0.5).toString() + "%")

            typeChess.text = "0.5%"
            typeChess.isEnabled = false
            tvChessRebate.text = getString(R.string.txt_reg_rebate).plus(NumberUtils.sub(mProfileVo.maxEsportsPoint, 0.5).toString() + "%")

            typeGame.text = "0.5%"
            typeGame.isEnabled = false
            tvGameRebate.text = getString(R.string.txt_reg_rebate).plus(NumberUtils.sub(mProfileVo.maxPokerPoint, 0.5).toString() + "%")


            KLog.i("rebate_percentage", mProfileVo.rebate_percentage)
            if (mProfileVo.rebate_percentage == null) {
                return
            }

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