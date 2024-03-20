package com.xtree.mine.ui.rebateagrt.viewmodel;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.drake.brv.BindingAdapter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.xtree.base.mvvm.model.ToolbarModel;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.widget.FilterView;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.ui.rebateagrt.model.DividendAgrtSendHeadModel;
import com.xtree.mine.ui.rebateagrt.model.DividendAgrtSendModel;
import com.xtree.mine.ui.rebateagrt.model.GameDividendAgrtModel;
import com.xtree.mine.ui.rebateagrt.model.GameDividendAgrtTotalModel;
import com.xtree.mine.ui.rebateagrt.model.GameSubordinateagrtModel;
import com.xtree.mine.ui.rebateagrt.model.RebateAgrtCreateHeadModel;
import com.xtree.mine.vo.StatusVo;
import com.xtree.mine.vo.request.DividendAgrtCheckRequest;
import com.xtree.mine.vo.request.GameDividendAgrtRequest;
import com.xtree.mine.vo.response.GameDividendAgrtResponse;

import org.reactivestreams.Subscription;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by KAKA on 2024/3/19.
 * Describe:
 */
public class DividendAgrtSendViewModel extends BaseViewModel<MineRepository> implements ToolbarModel {

    public DividendAgrtSendViewModel(@NonNull Application application) {
        super(application);
    }

    public DividendAgrtSendViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    private GameDividendAgrtRequest gameDividendAgrtRequest;
    private WeakReference<FragmentActivity> mActivity = null;
    private final MutableLiveData<String> titleData = new MutableLiveData<>(getApplication().getString(R.string.txt_manualdividendpayout_title));

    public final MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<>(new ArrayList<>());

    public final MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_dividendagrt_send);
                    add(R.layout.item_dividendagrt_send_head);
                }
            });

    public final BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {

        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {

        }

        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {
            if (itemViewType == R.layout.item_dividendagrt_send) {
                DividendAgrtSendModel bindModel = (DividendAgrtSendModel) bindModels.get(layoutPosition);
                bindModel.checked.set(Boolean.FALSE.equals(bindModel.checked.get()));
            }
        }

    };

    /**
     * 列表加载
     */
    public OnLoadMoreListener onLoadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            headModel.p++;
            getDividendData();
        }
    };
    private final DividendAgrtSendHeadModel headModel = new DividendAgrtSendHeadModel();

    private final ArrayList<BindModel> bindModels = new ArrayList<BindModel>() {{
        headModel.setItemType(1);
        add(headModel);
    }};

    //合计数额
    public MutableLiveData<String> total = new MutableLiveData<>();
    //可用余额
    public MutableLiveData<String> availablebalance = new MutableLiveData<>();

    public void initData(GameDividendAgrtRequest response) {
        //init data
        gameDividendAgrtRequest = response;
        datas.setValue(bindModels);
        getDividendData();
    }

    public void setActivity(FragmentActivity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
    }

    /**
     * 手动分红发放
     */
    public void sendDividend() {


    }

    private synchronized void getDividendData() {
        if (getmCompositeDisposable() != null) {
            getmCompositeDisposable().clear();
        }

        gameDividendAgrtRequest.p = headModel.p;
        Disposable disposable = (Disposable) model.getGameDividendAgrtData(gameDividendAgrtRequest)
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        //重新加载弹dialog
                        if (gameDividendAgrtRequest.p <= 1) {
                            LoadingDialog.show(mActivity.get());
                        }
                    }
                })
                .subscribeWith(new HttpCallBack<GameDividendAgrtResponse>() {
                    @Override
                    public void onResult(GameDividendAgrtResponse vo) {

                        if (vo != null) {

                            //p<=1说明是第一页数据
                            if (gameDividendAgrtRequest.p <= 1) {
                                bindModels.clear();
                                if (vo.getSelfPayStatus() != null) {
                                    headModel.setPayoff(vo.getSelfPayStatus().getPayoff());
                                    headModel.setOwe(vo.getSelfPayStatus().getOwe());
                                }
                                bindModels.add(headModel);
                            }

                            GameDividendAgrtResponse.ChildrenBillDTO childrenBill = vo.getChildrenBill();
                            if (childrenBill != null) {
                                List<GameDividendAgrtResponse.ChildrenBillDTO.DataDTO> data = childrenBill.getData();
                                if (data != null) {
                                    for (GameDividendAgrtResponse.ChildrenBillDTO.DataDTO dataDTO : data) {
                                        DividendAgrtSendModel sendModel = new DividendAgrtSendModel();
                                        sendModel.bet = dataDTO.getBet();
                                        sendModel.netloss = dataDTO.getNetloss();
                                        sendModel.people = dataDTO.getPeople();
                                        sendModel.cycle_percent = dataDTO.getCycle_percent();
                                        sendModel.userName = dataDTO.getUsername();
                                        sendModel.userid = dataDTO.getUserid();
                                        sendModel.subMoney = dataDTO.getSub_money();
                                        sendModel.profitloss = dataDTO.getProfitloss();
                                        if (vo.getCurrentCycle() != null) {
                                            sendModel.cycle = vo.getCurrentCycle().getTitle();
                                        }
                                        //设置契约状态
                                        sendModel.payStatu = dataDTO.getPay_status();
                                        for (Map.Entry<String, String> entry : vo.getBillStatus().entrySet()) {
                                            if (entry.getKey().equals(sendModel.payStatu)) {
                                                sendModel.payStatuText = entry.getValue();
                                            }
                                        }
                                        bindModels.add(sendModel);
                                    }
                                }
                            }

                            datas.setValue(bindModels);

                            //更新总数据
                            availablebalance.setValue(vo.getUser().getAvailablebalance());

                            GameDividendAgrtResponse.MobilePageDTO mobilePage = vo.getMobile_page();
                            if (mobilePage != null &&
                                    mobilePage.getTotal_page().equals(String.valueOf(gameDividendAgrtRequest.p))) {
                                loadMoreWithNoMoreData();
                            } else {
                                finishLoadMore(true);
                            }
                        } else {
                            finishLoadMore(false);
                        }
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                        finishLoadMore(false);
                    }
                });
        addSubscribe(disposable);
    }


    @Override
    public void onBack() {
        finish();
    }

    @Override
    public MutableLiveData<String> getTitle() {
        return titleData;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mActivity != null) {
            mActivity.clear();
            mActivity = null;
        }
    }
}
