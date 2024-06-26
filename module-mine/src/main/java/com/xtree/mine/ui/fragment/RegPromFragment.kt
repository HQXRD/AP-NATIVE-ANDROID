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
import com.xtree.mine.databinding.LayoutQuickRebateBinding
import com.xtree.mine.ui.viewmodel.MineViewModel
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory
import com.xtree.mine.vo.MarketingVo
import me.xtree.mvvmhabit.base.BaseFragment
import me.xtree.mvvmhabit.utils.KLog
import me.xtree.mvvmhabit.utils.SPUtils
import me.xtree.mvvmhabit.utils.ToastUtils


class RegPromFragment : BaseFragment<FragmentPromLinksBinding, MineViewModel>(), RegInterface {

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


    private lateinit var linkPpw: BasePopupView

    private lateinit var mProfileVo: ProfileVo


    override fun initView() {
        val json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE)
        mProfileVo = Gson().fromJson(json, ProfileVo::class.java)
        initDialog()
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
            val map = HashMap<String, Any>()
            map["emails"] = arrayListOf<Any>()
            map["nonce"] = UuidUtil.getID()
            map["qqs"] = arrayListOf<Any>()
            map["usertype"] = type.toString()
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
            if (it.links.isNotEmpty() && it.domainList.isNotEmpty()) {
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

                binding.tvSelectType.text = when (it.links[0].usertype.toInt()) {
                    member -> {
                        binding.include0.layout.visibility = View.INVISIBLE
                        binding.include1.layout.visibility = View.VISIBLE
                        mList[1]
                    }

                    else -> {
                        binding.include0.layout.visibility = View.VISIBLE
                        binding.include1.layout.visibility = View.GONE
                        mList[0]
                    }


                }
            }

            if (it.prizeGroups != null) {
                /**
                 * 返点详情弹窗
                 */
                val rebateDialog = XPopup.Builder(context).asCustom(RebateDetailsDialog(requireContext(), it.prizeGroups))
                binding.tvRebateDetails.setOnClickListener {
                    rebateDialog.show()
                }
            }
            setRebate(binding.include0, it, 0)
            setRebate(binding.include1, it, 1)
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
        if (mProfileVo.usertype == 1 && (mProfileVo.userLevel == 1 || mProfileVo.userLevel == 2)) {//总代与一代只能开户代理
            binding.tvSelectType.isEnabled = false
        }
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
        adapter.addAll(mList)
        ppw = XPopup.Builder(context).asCustom(ListDialog(requireContext(), "", adapter))

        binding.tvSelectType.text = mList[0]
    }

    /**
     * 代理/会员快速返点设置
     */
    private fun setRebate(include: LayoutQuickRebateBinding, vo: MarketingVo, type: Int) {
        include.apply {
            if (mProfileVo.rebate_percentage == null) {
                return
            }
            val max = mProfileVo.rebate_percentage.replace("%", "").toDouble()

            typeLottery.text = vo.selectedPoint.plus("%")
            val self = NumberUtils.sub(max, vo.selectedPoint.toDouble())
            tvLotteryRebate.text = getString(R.string.txt_reg_rebate).plus("$self%")
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
                typeReal.text = vo.livePoint.plus("%")
                tvRealRebate.text = getString(R.string.txt_reg_rebate)
                    .plus(NumberUtils.sub(mProfileVo.maxLivePoint, vo.livePoint.toDouble()).toString() + "%")
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
                typeSports.text = vo.sportPoint.plus("%")
                tvSportsRebate.text = getString(R.string.txt_reg_rebate)
                    .plus(NumberUtils.sub(mProfileVo.maxSportPoint, vo.sportPoint.toDouble()).toString() + "%")
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
                typeChess.text = vo.chessPoint.plus("%")
                tvChessRebate.text = getString(R.string.txt_reg_rebate)
                    .plus(NumberUtils.sub(mProfileVo.maxPokerPoint, vo.chessPoint.toDouble()).toString() + "%")
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
                typeGame.text = vo.esportsPoint.plus("%")
                tvGameRebate.text = getString(R.string.txt_reg_rebate)
                    .plus(NumberUtils.sub(mProfileVo.maxEsportsPoint, vo.esportsPoint.toDouble()).toString() + "%")
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