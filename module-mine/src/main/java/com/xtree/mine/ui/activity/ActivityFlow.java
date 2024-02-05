package com.xtree.mine.ui.activity;

import android.os.Bundle;
import android.view.MotionEvent;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.utils.CfLog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentChooseWithdrawBinding;
import com.xtree.mine.ui.fragment.AwardsRecordDialog;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.AwardsRecordVo;

import me.xtree.mvvmhabit.base.BaseActivity;

@Route(path = RouterActivityPath.Mine.PAGER_MY_WALLET_FLOW)
/** 活动流水*/
public class ActivityFlow extends BaseActivity<FragmentChooseWithdrawBinding, ChooseWithdrawViewModel> {

    private BasePopupView basePopupView = null;
    private AwardsRecordVo awardsRecordVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        if (viewModel == null) {
            CfLog.i("ChooseActivity viewModel  null");
        } else {
            initViewModel();
        }
    }

    @Override
    public ChooseWithdrawViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(this.getApplication());
        return new ViewModelProvider(this, factory).get(ChooseWithdrawViewModel.class);
    }

    @Override
    public void initData() {
        if (viewModel == null) {
            CfLog.i("ChooseActivity viewModel  null");
        } else {
            viewModel.getAwardRecord();
        }

    }

    @Override
    public void initViewObservable()
    {
        if (viewModel != null) {
            viewModel.awardrecordVoMutableLiveData.observe(this, vo -> {
                awardsRecordVo = vo;
                if (awardsRecordVo != null && awardsRecordVo.list != null && awardsRecordVo.list.size() != 0) {
                    showAwardsRecord();
                }
                else
                {
                    showAwardsRecord();
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
        CfLog.i("awardsRecordVo.locked_award_sum = " +awardsRecordVo.locked_award_sum);
        if(awardsRecordVo != null && !awardsRecordVo.locked_award_sum.equals("0.00"))
        {
            basePopupView = new XPopup.Builder(this).dismissOnBackPressed(false)
                    .dismissOnTouchOutside(false)
                    .asCustom(AwardsRecordDialog.newInstance(this, this, awardsRecordVo, 1 ,new AwardsRecordDialog.IAwardsDialogBack() {
                        @Override
                        public void closeAwardsDialog() {
                            basePopupView.dismiss();
                            finish();
                            CfLog.i("AwardsRecordDialog  dismiss");
                        }
                    }) );
        }
        else
        {
            basePopupView = new XPopup.Builder(this).dismissOnBackPressed(false)
                    .dismissOnTouchOutside(false)
                    .asCustom(AwardsRecordDialog.newInstance(this, this, awardsRecordVo, 0 ,new AwardsRecordDialog.IAwardsDialogBack() {
                        @Override
                        public void closeAwardsDialog() {
                            basePopupView.dismiss();
                            finish();
                            CfLog.i("AwardsRecordDialog  dismiss");
                        }
                    }));
        }

        basePopupView.show();

    }

}
