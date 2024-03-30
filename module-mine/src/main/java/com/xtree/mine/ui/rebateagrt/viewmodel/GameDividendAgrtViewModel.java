package com.xtree.mine.ui.rebateagrt.viewmodel;

import static com.xtree.mine.ui.rebateagrt.model.RebateAreegmentTypeEnum.GAMEREBATE;
import static com.xtree.mine.ui.rebateagrt.model.RebateAreegmentTypeEnum.LOTTERIES;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.mvvm.model.ToolbarModel;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.widget.FilterView;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.base.widget.TipDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.ui.rebateagrt.fragment.DividendAgrtCheckDialogFragment;
import com.xtree.mine.ui.rebateagrt.fragment.DividendAgrtSendDialogFragment;
import com.xtree.mine.ui.rebateagrt.model.GameDividendAgrtHeadModel;
import com.xtree.mine.ui.rebateagrt.model.GameDividendAgrtModel;
import com.xtree.mine.ui.rebateagrt.model.GameDividendAgrtSubModel;
import com.xtree.mine.ui.rebateagrt.model.GameDividendAgrtTotalModel;
import com.xtree.mine.ui.rebateagrt.model.RebateAreegmentTypeEnum;
import com.xtree.mine.vo.StatusVo;
import com.xtree.mine.vo.request.DividendAgrtCheckRequest;
import com.xtree.mine.vo.request.DividendAutoSendRequest;
import com.xtree.mine.vo.request.DividendAutoSentQuery;
import com.xtree.mine.vo.request.GameDividendAgrtRequest;
import com.xtree.mine.vo.response.DividendAutoSendResponse;
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
import me.xtree.mvvmhabit.http.BaseResponse2;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by KAKA on 2024/3/14.
 * Describe: 分红契约 viewmodel
 */
public class GameDividendAgrtViewModel extends BaseViewModel<MineRepository> implements ToolbarModel {

    private final MutableLiveData<String> titleData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<ArrayList<BindModel>>(new ArrayList<>());
    public MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_game_dividendagrt);
                    add(R.layout.item_game_dividendagrt_head);
                    add(R.layout.item_game_dividendagrt_sub);
                    add(R.layout.item_game_dividendagrt_total);
                }
            });
    private RebateAreegmentTypeEnum type;
    private WeakReference<FragmentActivity> mActivity = null;
    private GameDividendAgrtResponse dividendAgrtData = null;
    @SuppressLint("StaticFieldLeak")
    private BasePopupView pop = null;

    private final GameDividendAgrtHeadModel headModel = new GameDividendAgrtHeadModel(new GameDividendAgrtHeadModel.OnCallBack() {


        @Override
        public void sort(String title, ObservableField<StatusVo> sort, List<FilterView.IBaseVo> list) {
            showFilter(title, sort, list);
        }

        @Override
        public void cyclicality(String title, ObservableField<StatusVo> cycly, List<FilterView.IBaseVo> list) {
            showFilter(title, cycly, list);
        }

        @Override
        public void status(String title, ObservableField<StatusVo> statu, List<FilterView.IBaseVo> list) {
            showFilter(title, statu, list);
        }

        @Override
        public void check() {
            getDividendData();
        }

        @Override
        public void myAgrt() {
            DividendAgrtCheckRequest dividendAgrtCheckRequest = new DividendAgrtCheckRequest();
            dividendAgrtCheckRequest.setUserid(SPUtils.getInstance().getString(SPKeyGlobal.USER_ID));
            dividendAgrtCheckRequest.setType(headModel.type);
            startCheckAgrt(dividendAgrtCheckRequest);
        }
    });

    private final GameDividendAgrtSubModel subModel = new GameDividendAgrtSubModel(new GameDividendAgrtSubModel.OnCallBack() {
        @Override
        public void autoSend() {
            getAutoSend();
        }

        @Override
        public void send() {
            GameDividendAgrtRequest gameDividendAgrtRequest = new GameDividendAgrtRequest();
            String[] split = headModel.sortData.get().getShowId().split("_");
            if (split.length > 0) {
                gameDividendAgrtRequest.sort = split[split.length - 1];
                gameDividendAgrtRequest.orderby = split[0];
            }
            if (headModel.cyclyData.get() != null) {
                gameDividendAgrtRequest.cycle_id = headModel.cyclyData.get().getShowId();
            }
            gameDividendAgrtRequest.type = headModel.type;
            gameDividendAgrtRequest.username = headModel.userNameData.get();
            gameDividendAgrtRequest.p = headModel.p;
            gameDividendAgrtRequest.pn = headModel.pn;

            DividendAgrtSendDialogFragment.show(mActivity.get(), gameDividendAgrtRequest);
        }
    });

    private final ArrayList<BindModel> bindModels = new ArrayList<BindModel>() {{
        headModel.setItemType(1);
        subModel.setItemType(2);

        add(headModel);
    }};

    public GameDividendAgrtViewModel(@NonNull Application application) {
        super(application);
    }

    public GameDividendAgrtViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }    /**
     * 列表加载
     */
    public OnLoadMoreListener onLoadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            headModel.p++;
            getDividendData();
        }
    };

    public void initData(RebateAreegmentTypeEnum type) {
        //init data
        this.type = type;
        switch (type) {
            case LOTTERIES:  //彩票报表
                headModel.type = "1";
                titleData.setValue(LOTTERIES.getName());
                break;
            case GAMEREBATE: //游戏报表
                headModel.type = "20";
                titleData.setValue(GAMEREBATE.getName());
                break;
        }
        datas.setValue(bindModels);
        getDividendData();
    }

    public void setActivity(FragmentActivity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
    }
    private void showFilter(String title, ObservableField<StatusVo> value, List<FilterView.IBaseVo> listStatus) {
        FilterView.showDialog(mActivity.get(), title, listStatus, new FilterView.ICallBack() {
            @Override
            public void onTypeChanged(FilterView.IBaseVo vo) {
                value.set(new StatusVo(vo.getShowId(), vo.getShowName()));
            }
        });
    }

    /**
     * 跳转契约详情
     *
     * @param dividendAgrtCheckRequest 请求参数
     */
    private void startCheckAgrt(DividendAgrtCheckRequest dividendAgrtCheckRequest) {
        DividendAgrtCheckDialogFragment.show(mActivity.get(), dividendAgrtCheckRequest);
    }

    private synchronized void getDividendData() {
        if (getmCompositeDisposable() != null) {
            getmCompositeDisposable().clear();
        }
        GameDividendAgrtRequest gameDividendAgrtRequest = new GameDividendAgrtRequest();
        String[] split = headModel.sortData.get().getShowId().split("_");
        if (split.length > 0) {
            gameDividendAgrtRequest.sort = split[split.length - 1];
            gameDividendAgrtRequest.orderby = split[0];
        }
        if (headModel.cyclyData.get() != null) {
            gameDividendAgrtRequest.cycle_id = headModel.cyclyData.get().getShowId();
        }
        gameDividendAgrtRequest.type = headModel.type;
        gameDividendAgrtRequest.username = headModel.userNameData.get();
        gameDividendAgrtRequest.p = headModel.p;
        gameDividendAgrtRequest.pn = headModel.pn;
        Disposable disposable = model.getGameDividendAgrtData(gameDividendAgrtRequest)
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
                            dividendAgrtData = vo;

                            //p<=1说明是第一页数据
                            if (gameDividendAgrtRequest.p <= 1) {
                                bindModels.clear();
                                HashMap<String, String> billStatus = vo.getBillStatus();
                                HashMap<String, GameDividendAgrtResponse.CyclesDTO> cycles = vo.getCycles();

                                ArrayList<FilterView.IBaseVo> status = new ArrayList<>();
                                for (Map.Entry<String, String> stringStringEntry : billStatus.entrySet()) {
                                    status.add(new StatusVo(stringStringEntry.getKey(), stringStringEntry.getValue()));
                                }
                                headModel.setStatusList(status);
                                ArrayList<FilterView.IBaseVo> cs = new ArrayList<>();
                                for (Map.Entry<String, GameDividendAgrtResponse.CyclesDTO> stringCyclesDTOEntry : cycles.entrySet()) {
                                    StatusVo statusVo = new StatusVo(stringCyclesDTOEntry.getKey(), stringCyclesDTOEntry.getValue().getTitle());
                                    cs.add(statusVo);
                                    //设置当前筛选的周期
                                    if (vo.getGet().getCycle_id().equals(stringCyclesDTOEntry.getKey())) {
                                        headModel.cyclyData.set(statusVo);
                                    }
                                }
                                headModel.setCyclytList(cs);


                                //sub
                                if (vo.getSelfPayStatus() != null) {
                                    subModel.setPayoff(vo.getSelfPayStatus().getPayoff());
                                    subModel.setOwe(vo.getSelfPayStatus().getOwe());
                                }

                                //total
                                GameDividendAgrtTotalModel totalModel = new GameDividendAgrtTotalModel();
                                totalModel.setItemType(3);
                                totalModel.setCycle(headModel.cyclyData.get().getShowName());

                                //自己的契约数据
                                GameDividendAgrtResponse.SelfBillDTO selfBill = vo.getSelfBill();
                                if (selfBill != null) {
                                    totalModel.setBet(selfBill.getBet());
                                    totalModel.setNetloss(selfBill.getNetloss());
                                    totalModel.setPeople(selfBill.getPeople());
                                    totalModel.setCycle_percent(selfBill.getCycle_percent());
                                    totalModel.setUserid(selfBill.getUserid());
                                    totalModel.setSubMoney(selfBill.getSub_money());
                                    totalModel.setSelfMoney(selfBill.getSelf_money());
                                    totalModel.setProfitloss(selfBill.getProfitloss());
                                    totalModel.setCycle(headModel.cyclyData.get().getShowName());
                                    //设置契约状态
                                    if (selfBill.getPay_status() != null) {
                                        totalModel.setPayStatu(selfBill.getPay_status());
                                    }
                                    for (Map.Entry<String, String> entry : vo.getBillStatus().entrySet()) {
                                        if (entry.getKey().equals(totalModel.getPayStatu())) {
                                            totalModel.setPayStatuText(entry.getValue());
                                        }
                                    }

                                    headModel.setSelf_money(selfBill.getSelf_money());
                                    headModel.setSettle_accounts(selfBill.getSettle_accounts());
                                    headModel.setSub_money(selfBill.getSub_money());
                                }
                                bindModels.add(headModel);
                                bindModels.add(subModel);
                                bindModels.add(totalModel);
                            }

                            GameDividendAgrtResponse.ChildrenBillDTO childrenBill = vo.getChildrenBill();
                            if (childrenBill != null) {
                                List<GameDividendAgrtResponse.ChildrenBillDTO.DataDTO> data = childrenBill.getData();
                                if (data != null) {
                                    for (GameDividendAgrtResponse.ChildrenBillDTO.DataDTO dataDTO : data) {
                                        GameDividendAgrtModel gameDividendAgrtModel = new GameDividendAgrtModel();
                                        gameDividendAgrtModel.setBet(dataDTO.getBet());
                                        gameDividendAgrtModel.setNetloss(dataDTO.getNetloss());
                                        gameDividendAgrtModel.setPeople(dataDTO.getPeople());
                                        gameDividendAgrtModel.setCycle_percent(dataDTO.getCycle_percent());
                                        gameDividendAgrtModel.setUserName(dataDTO.getUsername());
                                        gameDividendAgrtModel.setUserid(dataDTO.getUserid());
                                        gameDividendAgrtModel.setSubMoney(dataDTO.getSub_money());
                                        gameDividendAgrtModel.setSelfMoney(dataDTO.getSelf_money());
                                        gameDividendAgrtModel.setProfitloss(dataDTO.getProfitloss());
                                        gameDividendAgrtModel.setCycle(headModel.cyclyData.get().getShowName());
                                        //设置契约状态
                                        gameDividendAgrtModel.setPayStatu(dataDTO.getPay_status());
                                        for (Map.Entry<String, String> entry : vo.getBillStatus().entrySet()) {
                                            if (entry.getKey().equals(gameDividendAgrtModel.getPayStatu())) {
                                                gameDividendAgrtModel.setPayStatuText(entry.getValue());
                                            }
                                        }
                                        gameDividendAgrtModel.setCallBack(v -> {
                                            DividendAgrtCheckRequest dividendAgrtCheckRequest = new DividendAgrtCheckRequest();
                                            dividendAgrtCheckRequest.setUserid(v.getUserid());
                                            dividendAgrtCheckRequest.setType(headModel.type);
                                            startCheckAgrt(dividendAgrtCheckRequest);
                                        });
                                        bindModels.add(gameDividendAgrtModel);
                                    }
                                }
                            }

                            datas.setValue(bindModels);

                            BaseResponse2.MobilePageVo mobilePage = vo.mobile_page;
                            if (mobilePage != null &&
                                    mobilePage.total_page.equals(String.valueOf(gameDividendAgrtRequest.p))) {
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

    private synchronized void getAutoSend() {
        if (getmCompositeDisposable() != null) {
            getmCompositeDisposable().clear();
        }
        if (dividendAgrtData == null || dividendAgrtData.getGet() == null) {
            return;
        }

        DividendAutoSentQuery query = new DividendAutoSentQuery();
        DividendAutoSendRequest request = new DividendAutoSendRequest();
        request.setType(headModel.type);
        request.setCycle_id(dividendAgrtData.getGet().getCycle_id());
        Disposable disposable = model.getDividendAutoSendData(query, request)
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        LoadingDialog.show(mActivity.get());
                    }
                })
                .subscribeWith(new HttpCallBack<DividendAutoSendResponse>() {
                    @Override
                    public void onResult(DividendAutoSendResponse vo) {
                        if (vo.getStatus() == 1) {
                            showTipDialog(getApplication().getString(R.string.txt_send_dividend_tip4));
                        } else {
                            showTipDialog(vo.getMsg());
                        }
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                        showTipDialog(t.message);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 提示弹窗
     */
    private void showTipDialog(String msg) {
        MsgDialog dialog = new MsgDialog(mActivity.get(), getApplication().getString(R.string.txt_kind_tips), msg, true, new TipDialog.ICallBack() {
            @Override
            public void onClickLeft() {

            }

            @Override
            public void onClickRight() {
                if (pop != null) {
                    pop.dismiss();
                }
            }
        });

        pop = new XPopup.Builder(mActivity.get())
                .dismissOnTouchOutside(true)
                .dismissOnBackPressed(true)
                .asCustom(dialog).show();
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
