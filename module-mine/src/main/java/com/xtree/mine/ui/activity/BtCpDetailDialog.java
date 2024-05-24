package com.xtree.mine.ui.activity;

import android.app.Application;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.utils.CfLog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogBtCpDetailBinding;
import com.xtree.mine.ui.viewmodel.ReportViewModel;
import com.xtree.mine.vo.LotteryDetailVo;
import com.xtree.mine.vo.LotteryOrderVo;

import java.util.HashMap;

import me.xtree.mvvmhabit.utils.Utils;

public class BtCpDetailDialog extends BottomPopupView {

    //private Context ctx;
    LifecycleOwner owner;
    private String id; // D20240203-192***CED

    ReportViewModel viewModel;
    DialogBtCpDetailBinding binding;
    BasePopupView ppw = null;
    BasePopupView ppw2 = null;

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
        binding = DialogBtCpDetailBinding.bind(findViewById(R.id.cl_root));
        binding.ivwClose.setOnClickListener(v -> dismiss());

        binding.btnDeleteCp.setOnClickListener(v -> {
            showDialog();
        });

        //binding.btnDeleteCpBig.setOnClickListener(v -> {
        //    showDialog();
        //});
    }

    private void initData() {
        viewModel = new ReportViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());
    }

    private void initViewObservable() {
        viewModel.liveDataBtCpDetail.observe(owner, vo -> {
            CfLog.i();
            setView(vo);
        });

        viewModel.liveDataDeleteCp.observe(owner, vo -> {
            ppw2 = new XPopup.Builder(getContext()).asCustom(new MsgDialog(getContext(), "", vo, true, new MsgDialog.ICallBack() {
                @Override
                public void onClickLeft() {
                    ppw2.dismiss();
                }

                @Override
                public void onClickRight() {
                    binding.btnDeleteCp.setVisibility(View.INVISIBLE);
                    //binding.btnDeleteCpBig.setVisibility(View.INVISIBLE);
                    ppw2.dismiss();
                }
            }));
            ppw2.show();
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

        if (vo.iscancel.equals("0") && vo.isgetprize.equals("0")) {
            binding.btnDeleteCp.setVisibility(View.VISIBLE);
            //binding.btnDeleteCpBig.setVisibility(View.VISIBLE);
        }

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

    private void showDialog() {
        String msg = getContext().getString(R.string.txt_delete_cp_msg);
        ppw = new XPopup.Builder(getContext()).asCustom(new MsgDialog(getContext(), "", msg, new MsgDialog.ICallBack() {
            @Override
            public void onClickLeft() {
                ppw.dismiss();
            }

            @Override
            public void onClickRight() {
                deleteCp();
                ppw.dismiss();
            }
        }));
        ppw.show();
    }

    private void deleteCp() {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("username", binding.tvwUsername.getText().toString());
        map.put("lottery", binding.tvwLotteryName.getText().toString());
        map.put("method", binding.tvwMethodName.getText().toString());
        map.put("issue", binding.tvwIssue.getText().toString());

        viewModel.deleteBtCp(map);
    }
}
