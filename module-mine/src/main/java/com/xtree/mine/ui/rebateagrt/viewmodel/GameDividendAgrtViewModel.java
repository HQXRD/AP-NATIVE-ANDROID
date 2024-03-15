package com.xtree.mine.ui.rebateagrt.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.lxj.xpopup.XPopup;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.xtree.base.global.Constant;
import com.xtree.base.mvvm.model.ToolbarModel;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.widget.FilterView;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.base.widget.TipDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.ui.rebateagrt.model.GameDividendAgrtHeadModel;
import com.xtree.mine.ui.rebateagrt.model.GameDividendAgrtModel;
import com.xtree.mine.ui.rebateagrt.model.GameDividendAgrtSubModel;
import com.xtree.mine.ui.rebateagrt.model.GameDividendAgrtTotalModel;
import com.xtree.mine.ui.rebateagrt.model.GameRebateAgrtModel;
import com.xtree.mine.ui.rebateagrt.model.GameRebateAgrtTotalModel;
import com.xtree.mine.ui.rebateagrt.model.RebateAreegmentTypeEnum;
import com.xtree.mine.vo.StatusVo;
import com.xtree.mine.vo.request.DividendAutoSendRequest;
import com.xtree.mine.vo.request.GameDividendAgrtRequest;
import com.xtree.mine.vo.request.GameRebateAgrtRequest;
import com.xtree.mine.vo.response.DividendAutoSendResponse;
import com.xtree.mine.vo.response.GameDividendAgrtResponse;
import com.xtree.mine.vo.response.GameRebateAgrtResponse;

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
import me.xtree.mvvmhabit.utils.StringUtils;

/**
 * Created by KAKA on 2024/3/14.
 * Describe:
 */
public class GameDividendAgrtViewModel extends BaseViewModel<MineRepository> implements ToolbarModel {

    public GameDividendAgrtViewModel(@NonNull Application application) {
        super(application);
    }

    public GameDividendAgrtViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    private RebateAreegmentTypeEnum type;

    private WeakReference<FragmentActivity> mActivity = null;

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
    });

    private final GameDividendAgrtSubModel subModel = new GameDividendAgrtSubModel(new GameDividendAgrtSubModel.OnCallBack() {
        @Override
        public void autoSend() {
            getAutoSend();
        }

        @Override
        public void send() {

        }
    });

    private final ArrayList<BindModel> bindModels = new ArrayList<BindModel>(){{
        headModel.setItemType(1);
        subModel.setItemType(2);

        add(headModel);
    }};

    public void initData(RebateAreegmentTypeEnum type) {
        //init data
        this.type = type;
        switch (type) {
            case LOTTERIES:  //彩票报表
                headModel.type = 1;
                titleData.setValue("彩票契约分红");
                break;
            case GAMEREBATE: //游戏报表
                headModel.type = 22;
                titleData.setValue("游戏分红");
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
        gameDividendAgrtRequest.username = headModel.userNameData.get();
        gameDividendAgrtRequest.p = headModel.p;
        gameDividendAgrtRequest.pn = headModel.pn;
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
                                HashMap<String, String> billStatus = vo.getBillStatus();
                                HashMap<String, GameDividendAgrtResponse.CyclesDTO> cycles = vo.getCycles();

                                ArrayList<FilterView.IBaseVo> status = new ArrayList<>();
                                for (Map.Entry<String, String> stringStringEntry : billStatus.entrySet()) {
                                    status.add(new StatusVo(stringStringEntry.getKey(), stringStringEntry.getValue()));
                                }
                                headModel.setStatusList(status);
                                ArrayList<FilterView.IBaseVo> cs = new ArrayList<>();
                                for (Map.Entry<String, GameDividendAgrtResponse.CyclesDTO> stringCyclesDTOEntry : cycles.entrySet()) {
                                    cs.add(new StatusVo(stringCyclesDTOEntry.getKey(), stringCyclesDTOEntry.getValue().getTitle()));
                                }
                                headModel.setCyclytList(cs);
                                bindModels.add(headModel);

                                //sub
                                bindModels.add(subModel);
                                //total
                                GameDividendAgrtTotalModel totalModel = new GameDividendAgrtTotalModel();
                                totalModel.setItemType(3);
                                totalModel.cycly = headModel.cyclyData.get().getShowName();
                                bindModels.add(totalModel);
                            }

                            GameDividendAgrtResponse.ChildrenBillDTO childrenBill = vo.getChildrenBill();
                            if (childrenBill != null) {
                                List<GameDividendAgrtResponse.ChildrenBillDTO.DataDTO> data = childrenBill.getData();
                                if (data != null) {
                                    for (GameDividendAgrtResponse.ChildrenBillDTO.DataDTO dataDTO : data) {
                                        GameDividendAgrtModel gameDividendAgrtModel = new GameDividendAgrtModel();
                                        gameDividendAgrtModel.bet = dataDTO.getBet();
                                        gameDividendAgrtModel.netloss = dataDTO.getNetloss();
                                        gameDividendAgrtModel.people = dataDTO.getPeople();
                                        gameDividendAgrtModel.cycle_percent = dataDTO.getCycle_percent();
                                        gameDividendAgrtModel.userName = dataDTO.getUsername();
                                        gameDividendAgrtModel.subMoney = dataDTO.getSub_money();
                                        gameDividendAgrtModel.cycle = headModel.cyclyData.get().getShowName();
                                        bindModels.add(gameDividendAgrtModel);
                                    }
                                }
                            }

                            datas.setValue(bindModels);

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

    private synchronized void getAutoSend() {
        if (getmCompositeDisposable() != null) {
            getmCompositeDisposable().clear();
        }
        DividendAutoSendRequest request = new DividendAutoSendRequest();

        Disposable disposable = (Disposable) model.getDividendAutoSendData(request)
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        LoadingDialog.show(mActivity.get());
                    }
                })
                .subscribeWith(new HttpCallBack<DividendAutoSendResponse>() {
                    @Override
                    public void onResult(DividendAutoSendResponse vo) {
                        if (vo.getData() == null) {
                            MsgDialog dialog = new MsgDialog(mActivity.get(), "温馨提示", vo.getMsg(), new TipDialog.ICallBack() {
                                @Override
                                public void onClickLeft() {

                                }

                                @Override
                                public void onClickRight() {

                                }
                            });
                            new XPopup.Builder(mActivity.get())
                                    .dismissOnTouchOutside(true)
                                    .dismissOnBackPressed(true)
                                    .asCustom(dialog).show();
                        }
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);

                        MsgDialog dialog = new MsgDialog(mActivity.get(), "温馨提示", t.message, new TipDialog.ICallBack() {
                            @Override
                            public void onClickLeft() {

                            }

                            @Override
                            public void onClickRight() {

                            }
                        });
                        new XPopup.Builder(mActivity.get())
                                .dismissOnTouchOutside(true)
                                .dismissOnBackPressed(true)
                                .asCustom(dialog).show();
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
