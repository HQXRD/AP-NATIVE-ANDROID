package com.xtree.mine.ui.activity;

import static com.xtree.base.router.RouterActivityPath.Mine.PAGER_CHOOSE_WITHDRAW;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.utils.CfLog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.base.widget.TipDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentChooseWithdrawBinding;
import com.xtree.mine.ui.fragment.AwardsRecordDialog;
import com.xtree.mine.ui.fragment.BankWithdrawalDialog;
import com.xtree.mine.ui.fragment.ChooseWithdrawalDialog;
import com.xtree.mine.ui.fragment.WithdrawFlowDialog;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.AwardsRecordVo;
import com.xtree.mine.vo.ChooseInfoVo;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.utils.ToastUtils;

/*提款流程Activity*/
@Route(path = PAGER_CHOOSE_WITHDRAW)
public class ChooseActivity extends BaseActivity<FragmentChooseWithdrawBinding, ChooseWithdrawViewModel> {
    public static final String PAGER_CHOOSE_WITHDRAW_KEY = "ViewType";
    public static final String PAGER_SOURCE_HOME = "HomeView";//主页面
    public static final String PAGER_SOURCE_WALLET = "Wallet";//我的钱包
    private BasePopupView basePopupView = null;
    private BasePopupView baseMessagePopupView = null;
    private BasePopupView loadingView = null;

    private BasePopupView baseGiftFlowView = null;//礼物流水
    private BasePopupView baseFlowErrorView = null;//流水异常
    private BasePopupView activityFlowView = null;//活动提款流水
    private AwardsRecordVo awardsRecordVo;
    private ChooseInfoVo chooseInfoVo;
    private String type;
    //1我的钱包点击 礼金  显示钱包流水
    //2我首页点击提款 【我的钱包 点击 去取款】 有提款流水 显示提款流水
    //没有提款流水 则显示 提款列表
    //从绑定页面 去提款 来的，不传viewType
    private int viewType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //修复Android 8.0版本 Activity 设置为透明主题造成的崩溃
        if (Build.VERSION.SDK_INT == 26 && isTranslucentOrFloating()) {
            fixOrientation(this);
        }

        super.onCreate(savedInstanceState);

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
        LoadingDialog.show(this);
        viewModel.getAwardRecord();
        //showAwardsRecord 显示活动流水
        //showChoose 显示提款列表

    }

    @Override
    public void initViewObservable() {
        if (viewModel != null) {
            viewModel.awardrecordVoMutableLiveData.observe(this, vo -> {
                awardsRecordVo = vo;
                //withdraw_dispensing_money 礼物流水
                //locked_award_sum 锁定金额

                if (awardsRecordVo != null && awardsRecordVo.list != null && (!TextUtils.equals("0.00", awardsRecordVo.withdraw_dispensing_money) || !TextUtils.equals("0.00", awardsRecordVo.locked_award_sum))) {
                    showWithdrawFlow();
                } else if (awardsRecordVo.networkStatus == 1) {
                    //链接超时
                    showNetError();
                    finish();
                    return;
                } else {
                    showChooseList();
                }
            });
            //提款列表數據
            viewModel.chooseInfoVoMutableLiveData.observe(this, vo -> {
                showMaskLoading();
                chooseInfoVo = vo;
                if (chooseInfoVo.networkStatus == 1) {
                    //网络异常
                    //  callBack.closeDialogByError();
                } else {

                    if (!TextUtils.isEmpty(chooseInfoVo.msg_type) && TextUtils.equals("2", chooseInfoVo.msg_type)) {
                        //异常状态
                        showErrorDialog(chooseInfoVo.message);

                    } else if ("chooseInfoVo.wdChannelList is Null".equals(chooseInfoVo.error)) {
                        //异常状态 提款列表数据为空
                        ToastUtils.showError(this.getString(R.string.txt_network_error));
                        return;
                    } else if (TextUtils.isEmpty(chooseInfoVo.error) || chooseInfoVo.error == null) {
                        // referUI();
                        showChooseList();
                    } else {
                        //showErrorDialog(chooseInfoVo.message);
                        //callBack.closeDialogByFlow(chooseInfoVo.message);
                        showErrorDialog(chooseInfoVo.message);
                    }
                }
            });

        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 显示提款流水
     */
    private void showWithdrawFlow() {
        basePopupView = new XPopup.Builder(this).dismissOnBackPressed(true)
                .dismissOnTouchOutside(false)
                .asCustom(WithdrawFlowDialog.newInstance(this, this, awardsRecordVo, new WithdrawFlowDialog.IWithdrawFlowDialogCallBack() {
                    @Override
                    public void closeWithdrawFlowDialog() {
                        LoadingDialog.finish();
                        // basePopupView = null;
                        finish();
                        CfLog.e("showWithdrawFlow  dismiss");
                    }
                }));
        basePopupView.show();

    }

    /**
     * 显示活动提款流水
     */
    private void showActivityFlow() {
        activityFlowView = new XPopup.Builder(this).dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .asCustom(AwardsRecordDialog.newInstance(this, this, awardsRecordVo, 0, () -> {
                    activityFlowView.dismiss();
                    finish();
                }));
        activityFlowView.show();
    }

    /* 流水不足显示弹窗*/
    private void showError(final AwardsRecordVo awardsRecordVo) {
        if (baseGiftFlowView == null) {
            final String title = this.getString(R.string.txt_kind_tips);
            final Double flag = Double.valueOf(awardsRecordVo.withdraw_dispensing_money) + Double.valueOf(awardsRecordVo.locked_award_sum);
            final String showMessage = String.format(this.getString(R.string.txt_awards_flow_title), String.valueOf(flag));
            baseGiftFlowView = new XPopup.Builder(this).asCustom(new MsgDialog(this, title, showMessage, false, new TipDialog.ICallBack() {
                @Override
                public void onClickLeft() {
                    baseGiftFlowView.dismiss();
                    finish();
                }

                @Override
                public void onClickRight() {
                    baseGiftFlowView.dismiss();
                    finish();
                }
            }));
        }
        baseGiftFlowView.show();
    }

    private void showNumberDialog(final String message) {
        baseMessagePopupView = new XPopup.Builder(this).dismissOnBackPressed(false)
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

    @Deprecated
    private void requestWithdrawInfo() {
        viewModel.getChooseWithdrawInfo();
    }

    /**
     * 显示提款页面
     */
    private void showChooseList() {

        showMaskLoading();
        // LoadingDialog.show(this);
        basePopupView = new XPopup.Builder(this).dismissOnBackPressed(false).dismissOnTouchOutside(false)
                .moveUpToKeyboard(false)
                .asCustom(ChooseWithdrawalDialog.newInstance(this, this, new ChooseWithdrawalDialog.IChooseDialogBack() {
                    @Override
                    public void closeDialog() {
                        LoadingDialog.finish();
                        finish();
                    }

                    @Override
                    public void closeDialogByError() {
                        LoadingDialog.finish();
                        finish();
                    }

                    @Override
                    public void closeDialogByFlow(String money) {

                        LoadingDialog.finish();
                        showErrorDialog(money);
                        // basePopupView.dismiss();
                    }

                    @Override
                    public void closeDialogByBind() {
                        finish();
                    }
                }, new BankWithdrawalDialog.BankWithdrawalClose() {
                    @Override
                    public void closeBankWithdrawal() {

                    }

                    @Override
                    public void closeBankByNumber() {
                        showNumberDialog("您今日没有可用提款次数");
                    }
                }, this));
        basePopupView.show();

    }

    /**
     * 显示异常Dialog
     */
    private void showErrorDialog(String showMessage) {
        if (baseFlowErrorView == null) {
            baseFlowErrorView = new XPopup.Builder(this)
                    .asCustom(new MsgDialog(this, this.getString(R.string.txt_kind_tips), showMessage, false, new MsgDialog.ICallBack() {
                        @Override
                        public void onClickLeft() {
                            //baseFlowErrorView.dismiss();
                            finish();
                        }

                        @Override
                        public void onClickRight() {
                            // baseFlowErrorView.dismiss();
                            finish();
                        }
                    }));
        }
        baseFlowErrorView.show();
    }

    /*显示网络异常Toast*/
    private void showNetError() {
        ToastUtils.showError(this.getString(R.string.txt_network_error));
    }

    /*显示銀行卡提款loading */
    private void showMaskLoading() {
        if (loadingView == null) {
            loadingView = new XPopup.Builder(this).asCustom(new LoadingDialog(this));
        }

        loadingView.show();
    }

    /*关闭loading*/
    private void dismissMasksLoading() {
        loadingView.dismiss();
    }

}
