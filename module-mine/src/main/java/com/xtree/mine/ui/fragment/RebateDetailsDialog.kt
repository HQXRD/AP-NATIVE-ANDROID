package com.xtree.mine.ui.fragment

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.util.XPopupUtils
import com.xtree.mine.R
import com.xtree.mine.databinding.DialogRebateDetailsBinding
import com.xtree.mine.vo.X0
import me.xtree.mvvmhabit.utils.KLog


/**
 * 返点详情弹窗
 */
class RebateDetailsDialog(context: Context, private val prizeGroups: Any) : BottomPopupView(context) {
    private lateinit var binding: DialogRebateDetailsBinding


    override fun onCreate() {
        super.onCreate()
        binding = DialogRebateDetailsBinding.bind(findViewById(R.id.cl_root))
        binding.ivwClose.setOnClickListener { this@RebateDetailsDialog.dismiss() }
        val adapter = RebateDetailsAdapter(context)
        val list = parseResponse(prizeGroups)
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
    private fun hashMapToKeyValueList(hashMap: HashMap<Int, List<X0>>): ArrayList<List<X0>> {
        val list = ArrayList<List<X0>>()
        for (value in hashMap.values) {
            list.add(value)
        }
        return list
    }

    private fun parseResponse(response: Any): ArrayList<List<X0>> {
        when (response) {
            is List<*> -> {
                val type = object : TypeToken<ArrayList<List<X0>>>() {}.type
                return Gson().fromJson(response.toString(), type)
            }

            is Map<*, *> -> {
                val type = object : TypeToken<HashMap<Int, List<X0>>>() {}.type
                return hashMapToKeyValueList(Gson().fromJson(response.toString(), type))
            }

            else -> {
                // 处理其他类型的响应
                KLog.i("Unknown response type")
                return ArrayList()
            }
        }
    }

}