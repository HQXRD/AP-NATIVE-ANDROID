package com.xtree.mine.ui.activity;

import static com.xtree.base.router.RouterActivityPath.Mine.PAGER_CHOOSE_WITHDRAW;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.CfLog;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentChooseWithdrawBinding;
import com.xtree.mine.ui.fragment.AwardsRecordDialog;
import com.xtree.mine.ui.fragment.withdrawal.BankWithdrawalDialog;
import com.xtree.mine.ui.fragment.withdrawal.ChooseWithdrawalDialog;
import com.xtree.mine.ui.fragment.FundPSWVerifyFragment;
import com.xtree.mine.ui.fragment.FundPassWordFragment;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.AwardsRecordVo;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/*提款activity 涉及流程:1、检测礼物流水；2、输入资金密码；3、弹出提款列表*/
@Route(path = PAGER_CHOOSE_WITHDRAW)
public class ChooseActivity extends BaseActivity<FragmentChooseWithdrawBinding, ChooseWithdrawViewModel> {

    private BasePopupView awardsRecordPopView = null;
    private BasePopupView baseChoosePopupView = null;
    private BasePopupView fundPSWPopView; // 资金密码
    private BasePopupView fundPSWVerifyPopView;//资金密码 谷歌验证码输入View
    private AwardsRecordVo awardsRecordVo;
    private int viewType;
    private static String checkCode;//输入资金密码 返回的Code 带入到请求提款列表接口使用
    private boolean isNetworkAwards = false;//礼物流水网络请求是否已刷新标志位
    private ProfileVo mProfileVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        mProfileVo = new Gson().fromJson(json, ProfileVo.class);

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
                isNetworkAwards = true;//增加网络回调标识
//                withdraw_dispensing_money 礼物流水 locked_award_sum
//                locked_award_sum 锁定金额 || !TextUtils.equals("0.00", awardsRecordVo.locked_award_sum)
                if (awardsRecordVo != null && ((!TextUtils.equals("0.00", awardsRecordVo.withdraw_dispensing_money)) || (!TextUtils.equals("0.00", awardsRecordVo.locked_award_sum)))) {
                    showAwardsRecord();
                } else if (awardsRecordVo.networkStatus == 1) {
                    //链接超时
                    showNetError();
                    finish();
                    return;
                } else {
                    showFundPSWVerifyDialog();
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
        if (awardsRecordPopView == null) {
            awardsRecordPopView = new XPopup.Builder(this).dismissOnBackPressed(false)
                    .dismissOnTouchOutside(false)
                    .asCustom(AwardsRecordDialog.newInstance(this, this, awardsRecordVo, new AwardsRecordDialog.IAwardsDialogBack() {
                        @Override
                        public void closeAwardsDialog() {
                            awardsRecordPopView.dismiss();
                            finish();
                            CfLog.i("AwardsRecordDialog  dismiss");
                        }
                    }));
        }

        awardsRecordPopView.show();

    }

    /**
     * 显示提款页面
     */
    private void showChooseList(final String checkCode) {
        if (baseChoosePopupView == null) {
            baseChoosePopupView = new XPopup.Builder(this).dismissOnBackPressed(false)
                    .dismissOnTouchOutside(false)
                    .moveUpToKeyboard(false)
                    .asCustom(ChooseWithdrawalDialog.newInstance(this, this, new ChooseWithdrawalDialog.IChooseDialogBack() {
                        @Override
                        public void closeDialog() {
                            closeFundPSWView();
                            finish();
                        }

                        @Override
                        public void closeDialogByError() {
                            closeFundPSWView();
                            showNetError();
                            finish();
                        }

                        @Override
                        public void closeDialogByBind() {
                            finish();
                        }

                    }, new BankWithdrawalDialog.BankWithdrawalClose() {
                        @Override
                        public void closeBankWithdrawal() {
                            CfLog.e("closeDialog  --> closeBankWithdrawal");
                            closeFundPSWView();
                            baseChoosePopupView.dismiss();
                            finish();

                        }

                        @Override
                        public void closeBankByPSW() {
                            CfLog.e("closeDialog --->closeBankByPSW");
                            closeFundPSWView();
                            baseChoosePopupView.dismiss();
                            finish();

                        }
                    }, checkCode, this));
        }

        baseChoosePopupView.show();
    }

    /*关闭资金密码输入页面*/
    private void closeFundPSWView() {
        if (fundPSWPopView != null) {
            fundPSWPopView.dismiss();
            fundPSWPopView = null;
        }
    }

    /*显示魔域资金密码输入页面*/
    private void showFundPSWVerifyDialog() {
        if ((mProfileVo != null) && (mProfileVo.twofa == 1)  && (mProfileVo.twofa_withdraw_enabled ==1))//已完成谷歌动态口令绑定
        {
            showPSWAndVerifyDialog();
        } else {
            showFundPSWDialog();
        }
    }

    private void showFundPSWDialog() {
        if (fundPSWPopView == null) {
            fundPSWPopView = new XPopup.Builder(this).dismissOnBackPressed(false)
                    .dismissOnTouchOutside(false)
                    .moveUpToKeyboard(false)
                    .asCustom(FundPassWordFragment.newInstance(this, this, new FundPassWordFragment.IFundPassWordCallBack() {
                        @Override
                        public void closeFundPWDialog() {
                            //showNetError();
                            hideKeyBoard();
                            if (fundPSWPopView != null) {
                                fundPSWPopView.dismiss();
                            }

                            finish();
                        }

                        @Override
                        public void closeFundPWDialogWithCode(String checkCode) {
                            // hideKeyBoard();

                            showChooseList(checkCode);
                            ViewGroup parent = (ViewGroup) fundPSWPopView.getParent();
                            parent.removeView(fundPSWPopView);
                        }

                    }));
        }
        fundPSWPopView.show();
    }

    private void showPSWAndVerifyDialog() {
        if (fundPSWVerifyPopView == null) {

            fundPSWVerifyPopView = new XPopup.Builder(this).dismissOnBackPressed(false)
                    .dismissOnTouchOutside(false)
                    .moveUpToKeyboard(false)
                    .asCustom(FundPSWVerifyFragment.newInstance(this, this, new FundPSWVerifyFragment.IFundPWVerifyCallBack() {
                        @Override
                        public void closeFundPWDialog() {
                            //showNetError();
                            hideKeyBoard();
                            if (fundPSWVerifyPopView != null) {
                                fundPSWVerifyPopView.dismiss();
                            }

                            finish();
                        }

                        @Override
                        public void closeFundPWDialogWithCode(String checkCode) {
                            // hideKeyBoard();

                            showChooseList(checkCode);
                            ViewGroup parent = (ViewGroup) fundPSWVerifyPopView.getParent();
                            parent.removeView(fundPSWVerifyPopView);
                        }

                    }));
        }
        fundPSWVerifyPopView.show();
    }

    /*显示网络异常Toast*/
    private void showNetError() {
        ToastUtils.showError(this.getString(R.string.txt_network_error));
    }

}
