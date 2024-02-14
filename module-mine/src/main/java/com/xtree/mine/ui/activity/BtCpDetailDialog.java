package com.xtree.mine.ui.activity;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.utils.CfLog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogBtCpDetailBinding;
import com.xtree.mine.ui.viewmodel.ReportViewModel;
import com.xtree.mine.vo.LotteryDetailVo;
import com.xtree.mine.vo.LotteryOrderVo;

import me.xtree.mvvmhabit.utils.Utils;

public class BtCpDetailDialog extends BottomPopupView {

    //private Context ctx;
    LifecycleOwner owner;
    private String id; // D20240203-192***CED

    ReportViewModel viewModel;
    DialogBtCpDetailBinding binding;

    private BtCpDetailDialog(@NonNull Context context) {
        super(context);
    }

    public static BtCpDetailDialog newInstance(Context ctx, LifecycleOwner owner, String id) {
        BtCpDetailDialog dialog = new BtCpDetailDialog(ctx);
        //dialog.ctx = ctx;
        dialog.owner = owner;
        dialog.id = id;

        return dialog;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initView();
        initData();
        initViewObservable();

        LoadingDialog.show(getContext()); // loading
        requestData();
    }

    private void initView() {
        binding = DialogBtCpDetailBinding.bind(findViewById(R.id.ll_root));
        binding.ivwClose.setOnClickListener(v -> dismiss());
    }

    private void initData() {
        viewModel = new ReportViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());
    }

    private void initViewObservable() {
        viewModel.liveDataBtCpDetail.observe(owner, vo -> {
            CfLog.i();
            setView(vo);
        });
    }

    private void setView(LotteryDetailVo t) {
        LotteryOrderVo vo = t.project;

        binding.tvwWriteTime.setText(vo.writetime);
        binding.tvwProjectId.setText(t.id); //
        binding.tvwUsername.setText(t.username); //
        binding.tvwLotteryName.setText(t.lottery); //

        binding.tvwIssue.setText(vo.issue);
        binding.tvwStatus.setText(viewModel.getLotteryStatus(getContext(), vo));
        binding.tvwMethodName.setText(t.method); //
        binding.tvwNoCode.setText(t.nocode); //

        binding.tvwTotalPrice.setText(vo.totalprice);
        binding.tvwBonus.setText(vo.bonus);

        binding.tvwMultiple.setText(vo.multiple);
        binding.tvwModes.setText(vo.modes);
        binding.tvwCode.setText(vo.code);

    }

    private void requestData() {

        viewModel.getBtCpOrderDetail(id); // D20240203-192***CED
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_bt_cp_detail;
    }

    @Override
    protected int getMaxHeight() {
        //return super.getMaxHeight();
        return (XPopupUtils.getScreenHeight(getContext()) * 80 / 100);
    }

}
