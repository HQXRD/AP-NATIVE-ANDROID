package com.xtree.mine.ui.fragment;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogWithdrawalDetailBinding;
import com.xtree.mine.databinding.ItemSplitDetailBinding;
import com.xtree.mine.ui.viewmodel.ReportViewModel;
import com.xtree.mine.vo.SplitVo;

import java.util.HashMap;
import java.util.List;

import me.xtree.mvvmhabit.utils.Utils;

public class SplitDetailDialog extends BottomPopupView {
    int maxHeight = 80; // 最大高度百分比 10-100
    String id;
    Context context;
    LifecycleOwner owner;
    DialogWithdrawalDetailBinding binding;
    ItemSplitDetailBinding binding2;

    CachedAutoRefreshAdapter<SplitVo> adapter;
    List<SplitVo> data;
    ReportViewModel viewModel;

    public SplitDetailDialog(@NonNull Context context) {
        super(context);
    }

    public SplitDetailDialog(@NonNull Context context, LifecycleOwner owner, String id) {
        super(context);
        this.context = context;
        this.owner = owner;
        this.id = id;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_withdrawal_detail;
    }

    @Override
    protected int getMaxHeight() {
        return (XPopupUtils.getScreenHeight(getContext()) * 80 / 100);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initData();
        initView();
        initViewObservable();
    }

    private void initView() {
        binding = DialogWithdrawalDetailBinding.bind(findViewById(R.id.ll_root));
        binding.ivwClose.setOnClickListener(v -> dismiss());

        adapter = new CachedAutoRefreshAdapter<SplitVo>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_split_detail, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemSplitDetailBinding.bind(holder.itemView);
                SplitVo vo = get(position);
                binding2.tvwOrderId.setText(vo.orderid);
                binding2.tvwMoney.setText(vo.split_amount);
                binding2.tvwMarginMoney.setText(vo.earnest_money);

                if (vo.split_status.equals("1")) {
                    binding2.tvwResult.setText(R.string.txt_fail);
                    binding2.tvwResultEnd.setText(R.string.txt_fail);
                    binding2.tvwResultEnd.setTextColor(getResources().getColor(R.color.clr_red_01));
                } else if (vo.split_status.equals("2")) {
                    binding2.tvwResult.setText(R.string.txt_succ);
                    binding2.tvwResultEnd.setText(R.string.txt_succ);
                    binding2.tvwResultEnd.setTextColor(getResources().getColor(R.color.clr_green_01));
                } else if (vo.split_status.equals("3")) {
                    binding2.tvwResult.setText(R.string.txt_man);
                    binding2.tvwResultEnd.setText(R.string.txt_man);
                } else if (vo.split_status.equals("4")) {
                    binding2.tvwResult.setText(R.string.txt_match);
                    binding2.tvwResultEnd.setText(R.string.txt_match);
                } else {
                    binding2.tvwResult.setText(R.string.txt_process);
                    binding2.tvwResultEnd.setText(R.string.txt_process);
                }

                binding2.tvwApplyTime.setText(vo.create_time);
                binding2.tvwFinishTime.setText(vo.confirm_time);
            }
        };
        binding.rvwList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvwList.setAdapter(adapter);
    }

    private void initData() {
        viewModel = new ReportViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());

        LoadingDialog.show(context);

        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);
        viewModel.getSpiltDeatil(map);
    }

    private void initViewObservable() {
        viewModel.liveDataSpiltDetail.observe(owner, vo -> {
            binding.tvwFailMoney.setText(vo.fail_amount);
            binding.tvwSuccMoney.setText(vo.succ_amount);
            binding.tvwManMoney.setText(vo.manual_amount);
            binding.tvwUnreceivedMoney.setText(vo.pending_amount);
            binding.tvwOriginMoney.setText(vo.total_amount);
            adapter.setData(vo.data);
            adapter.notifyDataSetChanged();
        });
    }
}
