package com.xtree.mine.ui.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.util.XPopupUtils
import com.xtree.base.adapter.CacheViewHolder
import com.xtree.base.adapter.CachedAutoRefreshAdapter
import com.xtree.base.databinding.ItemTextBinding
import com.xtree.base.widget.ListDialog
import com.xtree.mine.R
import com.xtree.mine.databinding.DialogSetQuestionBinding
import me.xtree.mvvmhabit.utils.ToastUtils

/**
 * 密保问题设置弹窗
 */
class SetQuestionDialog(context: Context) : BottomPopupView(context) {
    private lateinit var binding: DialogSetQuestionBinding
    private lateinit var ppw: BasePopupView
    private val mList = arrayListOf(
        QA("4", "您母亲的姓名是？"),
        QA("8", "您配偶的生日是？"),
        QA("13", "您的学号（或工号）是？"),
        QA("5", "您母亲的生日是？"),
        QA("12", "您高中班主任的名字是？"),
        QA("1", "您父亲的姓名是？"),
        QA("10", "您小学班主任的名字是？"),
        QA("2", "您父亲的生日是？"),
        QA("7", "您配偶的姓名是？"),
        QA("11", "您初中班主任的名字是？"),
        QA("16", "您最熟悉的童年好友名字是？"),
        QA("17", "您最熟悉的学校宿舍室友名字是？"),
        QA("18", "您影响最大的人名字是？")
    )

    override fun onCreate() {
        super.onCreate()
        binding = DialogSetQuestionBinding.bind(findViewById(R.id.cl_root))
        binding.ivwClose.setOnClickListener { this@SetQuestionDialog.dismiss() }
        binding.tvQuestion1.setOnClickListener {
            showChooseDialog()
        }
        binding.tvQuestion2.setOnClickListener {
            showChooseDialog()
        }
        binding.btnSet.setOnClickListener {
            if (binding.tvQuestion1.text.toString() == binding.tvQuestion2.text.toString()) {
                ToastUtils.showLong("问题一与问题二不允许一致！")
            } else if (binding.etAnswer1.text.isEmpty() || binding.etAnswer2.text.isEmpty()) {
                ToastUtils.showLong("请输入密保问题")
            } else {

            }
        }
        initDialog()
    }

    private fun initDialog() {
        val adapter: CachedAutoRefreshAdapter<QA> = object : CachedAutoRefreshAdapter<QA>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
                return CacheViewHolder(LayoutInflater.from(context).inflate(R.layout.item_text, parent, false))
            }

            override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
                val binding2 = ItemTextBinding.bind(holder.itemView)
                binding2.tvwTitle.text = get(position).value
                binding2.tvwTitle.setOnClickListener { _ ->
                    binding.tvQuestion1.text = get(position).value
                    ppw.dismiss()
                }
            }
        }
        adapter.addAll(mList)
        ppw = XPopup.Builder(context).asCustom(ListDialog(context, "", adapter))
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_set_question
    }

    override fun getMaxHeight(): Int {
        return XPopupUtils.getScreenHeight(context) * 92 / 100
    }

    private fun showChooseDialog() {
        ppw.show()
    }

    class QA(
        var key: String,
        var value: String
    )

}