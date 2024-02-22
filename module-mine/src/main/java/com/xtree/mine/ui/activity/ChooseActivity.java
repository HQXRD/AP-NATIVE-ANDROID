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

@Route(path = PAGER_CHOOSE_WITHDRAW)
public class ChooseActivity extends BaseActivity<FragmentChooseWithdrawBinding, ChooseWithdrawViewModel > {

    private BasePopupView basePopupView = null;
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
                        basePopupView.dismiss();
                        finish();
                        CfLog.i("AwardsRecordDialog  dismiss");
                    }
                }));

        basePopupView.show();

    }

    /**
     * 显示提款页面
     */
    private void showChoose() {
        LoadingDialog.show(this);
        basePopupView = new XPopup.Builder(this).dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .asCustom(ChooseWithdrawalDialog.newInstance(this, this, new ChooseWithdrawalDialog.IChooseDialogBack() {
                    @Override
                    public void closeDialog() {
                        finish();
                    }
                }, () -> {
                    basePopupView.dismiss();
                    finish();
                    CfLog.i("closeDialog");
                }));
        basePopupView.show();

    }


}
