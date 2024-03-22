package com.xtree.mine.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.util.XPopupUtils
import com.xtree.mine.R
import com.xtree.mine.databinding.DialogRebateDetailsBinding
import com.xtree.mine.vo.X0

/**
 * 返点详情弹窗
 */
class RebateDetailsDialog(context: Context, private val prizeGroups: HashMap<Int, List<X0>>) : BottomPopupView(context) {
    private lateinit var binding: DialogRebateDetailsBinding


    override fun onCreate() {
        super.onCreate()
        binding = DialogRebateDetailsBinding.bind(findViewById(R.id.cl_root))
        binding.ivwClose.setOnClickListener { this@RebateDetailsDialog.dismiss() }
        val adapter = RebateDetailsAdapter(context)
        val list = hashMapToKeyValueList(prizeGroups)
        adapter.addAll(list)
        binding.rvRebate.adapter = adapter
        binding.rvRebate.layoutManager = LinearLayoutManager(context)
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_rebate_details
    }

    override fun getMaxHeight(): Int {
        return XPopupUtils.getScreenHeight(context) * 80 / 100
    }

    /**
     * hashMap转ArrayList<ListValue>
     */
    private fun hashMapToKeyValueList(hashMap: HashMap<Int, List<X0>>): List<KeyValue> {
        val keyValueList = mutableListOf<KeyValue>()
        for ((key, value) in hashMap) {
            keyValueList.add(KeyValue(key, value))
        }
        return keyValueList
    }
}