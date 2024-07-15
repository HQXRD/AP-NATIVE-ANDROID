package com.xtree.bet.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.bet.bean.ui.BetConfirmOption;
import com.xtree.bet.bean.ui.BtResult;
import com.xtree.bet.bean.ui.CgOddLimit;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.data.BetRepository;
import com.xtree.bet.manager.BtCarManager;

import java.util.List;

import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;

public abstract class TemplateBtCarViewModel extends BaseBtViewModel implements BtCarViewModel {
    public SingleLiveData<List<BetConfirmOption>> btConfirmInfoDate = new SingleLiveData<>();
    public SingleLiveData<List<CgOddLimit>> cgOddLimitDate = new SingleLiveData<>();
    public SingleLiveData<List<BtResult>> btResultInfoDate = new SingleLiveData<>();
    public SingleLiveData<Void> noBetAmountDate = new SingleLiveData<>();

    public TemplateBtCarViewModel(@NonNull Application application, BetRepository model) {
        super(application, model);
    }

    public int getOrderBy(int index) {
        return index == 1 ? 0 : 1;
    }

    public int getOrderByPosition(int orderBy) {
        return orderBy == 1 ? 0 : 1;
    }

    public int getMarket(int index) {
        return index == 0 ? 1 : 2;
    }

    public int getMarketPosition(int market) {
        return market == 2 ? 1 : 0;
    }

    /**
     * 投注
     */
    public void bet(List<BetConfirmOption> betConfirmOptionList, List<CgOddLimit> cgOddLimitList, int acceptOdds) {
        if (betConfirmOptionList.size() > 1) {
            betMultiple(betConfirmOptionList, cgOddLimitList, acceptOdds);
        } else {
            singleBet(betConfirmOptionList, cgOddLimitList, acceptOdds);
        }
    }

    //投注后续有需要加个投注弹窗
    //private BasePopupView loadingDialog = null;
    //
    //public void showLoading(Context context) {
    //    if (loadingDialog == null) {
    //        loadingDialog = new XPopup.Builder(context)
    //                .dismissOnTouchOutside(false)
    //                .dismissOnBackPressed(true)
    //                .asCustom(new LoadingDialog(context));
    //    }
    //
    //    loadingDialog.show();
    //}
    //
    ///*关闭loading*/
    //public void dismissLoading() {
    //    loadingDialog.dismiss();
    //}

    /**
     * 打开今日
     */
    public void gotoToday() {
        BtCarManager.setIsCg(false);
        //BtCarManager.clearBtCar();
        RxBus.getDefault().postSticky(new BetContract(BetContract.ACTION_OPEN_TODAY));
    }

    /**
     * 打开串关
     *
     * @param betConfirmOption
     */
    public void gotoCg(BetConfirmOption betConfirmOption) {
        BtCarManager.addBtCar(betConfirmOption);
        BtCarManager.setIsCg(true);
        RxBus.getDefault().post(new BetContract(BetContract.ACTION_OPEN_CG));
    }
}
