package com.xtree.mine.ui.activity;

import static com.xtree.base.router.RouterActivityPath.Mine.PAGER_CHOOSE_WITHDRAW;

import android.os.Bundle;
import android.view.MotionEvent;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.utils.CfLog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentChooseWithdrawBinding;
import com.xtree.mine.ui.fragment.AwardsRecordDialog;
import com.xtree.mine.ui.fragment.BankWithdrawalDialog;
import com.xtree.mine.ui.fragment.ChooseWithdrawalDialog;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.AwardsRecordVo;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.utils.ToastUtils;

@Route(path = PAGER_CHOOSE_WITHDRAW)
public class ChooseActivity extends BaseActivity<FragmentChooseWithdrawBinding, ChooseWithdrawViewModel > {

    private BasePopupView basePopupView = null;
    private BasePopupView baseMessagePopupView = null ;
    private AwardsRecordVo awardsRecordVo;
    private int viewType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoadingDialog.show(this);
        viewModel.getAwardRecord();
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.fragment_choose_withdraw;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {

    }

    @Override
    public ChooseWithdrawViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(this.getApplication());
        return new ViewModelProvider(this, factory).get(ChooseWithdrawViewModel.class);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initViewObservable() {
        if (viewModel != null) {
            viewModel.awardrecordVoMutableLiveData.observe(this, vo -> {
                awardsRecordVo = vo;
                if (awardsRecordVo != null && awardsRecordVo.list != null && awardsRecordVo.list.size() != 0) {
                    showAwardsRecord();
                } else if (awardsRecordVo.networkStatus == 1) {
                    //链接超时
                    showNetError();
                    finish();
                    return;
                } else {
                    showChoose();
                }
            });
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 显示资金流水
     */
    private void showAwardsRecord() {

        LoadingDialog.show(this);
        basePopupView = new XPopup.Builder(this).dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .asCustom(AwardsRecordDialog.newInstance(this, this, awardsRecordVo, new AwardsRecordDialog.IAwardsDialogBack() {
                    @Override
                    public void closeAwardsDialog() {
                        LoadingDialog.finish();
                        basePopupView.dismiss();
                        finish();
                        CfLog.i("AwardsRecordDialog  dismiss");
                    }
                }));

        basePopupView.show();

    }
    private void  showNumberDialog(final String message){
        baseMessagePopupView  = new XPopup.Builder(this).dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .asCustom(AwardsRecordDialog.newInstance(this, this, new AwardsRecordDialog.IAwardsDialogBack() {
                    @Override
                    public void closeAwardsDialog() {
                        LoadingDialog.finish();
                        baseMessagePopupView.dismiss();
                       // finish();
                        CfLog.i("AwardsRecordDialog  dismiss");
                    }
                }));

        baseMessagePopupView.show();
    }
    /**
     * 显示提款页面
     */
    private void showChoose() {
        LoadingDialog.show(this);
        basePopupView = new  XPopup.Builder(this).dismissOnBackPressed(false).dismissOnTouchOutside(false)
                .moveUpToKeyboard(false).asCustom(ChooseWithdrawalDialog.newInstance(this, this, new ChooseWithdrawalDialog.IChooseDialogBack() {
                    @Override
                    public void closeDialog() {
                        LoadingDialog.finish();
                        finish();
                    }

                    @Override
                    public void closeDialogByError() {
                        LoadingDialog.finish();
                        showNetError();
                        finish();
                    }
                }, new BankWithdrawalDialog.BankWithdrawalClose() {
                    @Override
                    public void closeBankWithdrawal() {

                    }

                    @Override
                    public void closeBankByNumber() {
                        //弹出 提款流水 您今日没有可用提款次数
                        showNumberDialog("您今日没有可用提款次数");
                    }
                }));

        basePopupView.show();

    }

    /*显示网络异常Toast*/
    private void showNetError() {
        ToastUtils.showError(this.getString(R.string.txt_network_error));
    }

}
