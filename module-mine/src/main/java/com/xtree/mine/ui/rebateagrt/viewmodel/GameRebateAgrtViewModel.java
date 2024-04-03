package com.xtree.mine.ui.rebateagrt.viewmodel;

import static com.xtree.mine.ui.rebateagrt.fragment.RebateAgrtCreateDialogFragment.CHECK_MODE;
import static com.xtree.mine.ui.rebateagrt.fragment.RebateAgrtCreateDialogFragment.CREATE_MODE;
import static com.xtree.mine.ui.rebateagrt.model.RebateAreegmentTypeEnum.CHESS;
import static com.xtree.mine.ui.rebateagrt.model.RebateAreegmentTypeEnum.DAYREBATE;
import static com.xtree.mine.ui.rebateagrt.model.RebateAreegmentTypeEnum.EGAME;
import static com.xtree.mine.ui.rebateagrt.model.RebateAreegmentTypeEnum.LIVE;
import static com.xtree.mine.ui.rebateagrt.model.RebateAreegmentTypeEnum.SPORT;
import static com.xtree.mine.ui.rebateagrt.model.RebateAreegmentTypeEnum.USER;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.drake.brv.BindingAdapter;
import com.google.android.material.tabs.TabLayout;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.xtree.base.mvvm.model.ToolbarModel;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.widget.DateTimePickerDialog;
import com.xtree.base.widget.FilterView;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.base.widget.TipDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.data.source.APIManager;
import com.xtree.mine.ui.rebateagrt.fragment.RebateAgrtCreateDialogFragment;
import com.xtree.mine.ui.rebateagrt.model.GameRebateAgrtHeadModel;
import com.xtree.mine.ui.rebateagrt.model.GameRebateAgrtModel;
import com.xtree.mine.ui.rebateagrt.model.GameRebateAgrtTotalModel;
import com.xtree.mine.ui.rebateagrt.model.GameSubordinateagrtHeadModel;
import com.xtree.mine.ui.rebateagrt.model.GameSubordinateagrtModel;
import com.xtree.mine.ui.rebateagrt.model.GameSubordinaterebateHeadModel;
import com.xtree.mine.ui.rebateagrt.model.GameSubordinaterebateModel;
import com.xtree.mine.ui.rebateagrt.model.RebateAgrtDetailModel;
import com.xtree.mine.ui.rebateagrt.model.RebateAreegmentTypeEnum;
import com.xtree.mine.vo.StatusVo;
import com.xtree.mine.vo.request.GameRebateAgrtRequest;
import com.xtree.mine.vo.request.GameSubordinateAgrteRequest;
import com.xtree.mine.vo.request.GameSubordinateRebateRequest;
import com.xtree.mine.vo.response.GameRebateAgrtResponse;
import com.xtree.mine.vo.response.GameSubordinateAgrteResponse;
import com.xtree.mine.vo.response.GameSubordinateRebateResponse;

import org.reactivestreams.Subscription;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.http.BaseResponse2;
import me.xtree.mvvmhabit.http.BusinessException;

/**
 * Created by KAKA on 2024/3/9.
 * Describe: 游戏场馆返水契约viewmodel
 */
public class GameRebateAgrtViewModel extends BaseViewModel<MineRepository> implements ToolbarModel, TabLayout.OnTabSelectedListener {

    //真人返水
    public static final int REBATE_AGRT_TAB = 0;
    //下级契约
    public static final int Subordinate_Agrte_TAB = 1;
    //下级返水
    public static final int Subordinate_Rebate_TAB = 2;
    private final MutableLiveData<String> titleData = new MutableLiveData<>(
            getApplication().getString(R.string.rebate_agrt_title)
    );
    public MutableLiveData<ArrayList<String>> tabs = new MutableLiveData<>();
    private RebateAreegmentTypeEnum type;
    private BasePopupView showPop;
    private WeakReference<FragmentActivity> mActivity = null;
    /**
     * 下级数据，保存用于创建契约
     */
    private GameSubordinateAgrteResponse subData;

    //选项卡索引
    public ObservableInt tabPosition = new ObservableInt(0);
    public MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<ArrayList<BindModel>>(new ArrayList<>());
    public MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(0,R.layout.item_game_rebateagrt);
                    add(1,R.layout.item_game_rebateagrt_head);
                    add(2,R.layout.item_game_subordinateagrt);
                    add(3,R.layout.item_game_subordinateagrt_head);
                    add(4,R.layout.item_game_subordinaterebate);
                    add(5,R.layout.item_game_subordinaterebate_head);
                    add(6,R.layout.item_game_rebateagrt_total);
                    add(7,R.layout.item_empty);
                    add(8,R.layout.item_game_rebateagrt_dayrebate);
                    add(9,R.layout.item_game_rebateagrt_dayrebate_total);
                }
            });

    public final BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {

        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {
            if (itemViewType == R.layout.item_game_subordinateagrt) {
                view.findViewById(R.id.item_check).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int p = bindingViewHolder.getModelPosition() + bindingViewHolder.getAdapter().getHeaderCount();
                        //查看契约
                        if (subordinateAgrtDatas.size() > 0) {
                            GameSubordinateagrtModel bindModel = (GameSubordinateagrtModel) subordinateAgrtDatas.get(p);
                            //未创建
                            if (bindModel.getStatus().equals("-1")) {
                                createRebateAgrt(bindModel);
                            } else {
                                //其他
                                checkRebateAgrt(bindModel);
                            }
                        }
                    }
                });
            }
        }

        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {

        }

    };

    public OnLoadMoreListener onLoadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            switch (tabPosition.get()) {
                //真人返水
                case REBATE_AGRT_TAB:
                    gameRebateAgrtHeadModel.p++;
                    getRebatAgrteData();
                    break;
                //下级契约
                case Subordinate_Agrte_TAB:
                    gameSubordinateagrtHeadModel.p++;
                    getSubordinateAgrteData();
                    break;
                //下级返水
                case Subordinate_Rebate_TAB:
                    gameSubordinaterebateHeadModel.p++;
                    getSubordinateRebateData();
                    break;
            }
        }
    };

    private final GameRebateAgrtHeadModel.onCallBack gameRebateAgrtHeadModelCallBack = new GameRebateAgrtHeadModel.onCallBack() {

        @Override
        public void selectStartDate(ObservableField<String> startDate) {
            setStartDate(startDate);
        }

        @Override
        public void selectEndDate(ObservableField<String> endDate) {
            setEndDate(endDate);
        }

        @Override
        public void selectStatus(ObservableField<StatusVo> state, List<FilterView.IBaseVo> listStatus) {
            FilterView.showDialog(mActivity.get(), getApplication().getString(R.string.status), listStatus, new FilterView.ICallBack() {
                @Override
                public void onTypeChanged(FilterView.IBaseVo vo) {
                    state.set(new StatusVo(vo.getShowId(), vo.getShowName()));
                }
            });
        }

        @Override
        public void check(StatusVo state, String startDate, String endDate) {
            getRebatAgrteData();
        }

        @Override
        public void showTip() {
            String content = "";
            switch (type) {
                case LIVE:
                    content = getApplication().getString(R.string.txt_rebateagrt_tip4);
                    break;
                case SPORT:
                    content = getApplication().getString(R.string.txt_rebateagrt_tip5);
                    break;
                case CHESS:
                    content = getApplication().getString(R.string.txt_rebateagrt_tip6);
                    break;
                case EGAME:
                    content = getApplication().getString(R.string.txt_rebateagrt_tip7);
                    break;
                case DAYREBATE:
                    content = getApplication().getString(R.string.txt_rebateagrt_tip8);
                    break;
            }
            MsgDialog dialog = new MsgDialog(mActivity.get(), getApplication().getString(R.string.txt_kind_tips),
                    content,
                    true,
                    new TipDialog.ICallBack() {
                @Override
                public void onClickLeft() {

                }

                @Override
                public void onClickRight() {
                    if (showPop != null) {
                        showPop.dismiss();
                    }
                }
            });

            showPop = new XPopup.Builder(mActivity.get())
                    .dismissOnTouchOutside(true)
                    .dismissOnBackPressed(true)
                    .asCustom(dialog).show();
        }

        @Override
        public void showRules(String rules) {
            showAgrtDetail(rules);
        }
    };

    private final GameSubordinateagrtHeadModel.onCallBack gameSubordinateagrtHeadModelCallBack = new GameSubordinateagrtHeadModel.onCallBack() {
        @Override
        public void selectStatus(ObservableField<StatusVo> state, List<FilterView.IBaseVo> listStatus) {
            FilterView.showDialog(mActivity.get(), getApplication().getString(R.string.status), listStatus, new FilterView.ICallBack() {
                @Override
                public void onTypeChanged(FilterView.IBaseVo vo) {
                    state.set(new StatusVo(vo.getShowId(), vo.getShowName()));
                }
            });
        }

        @Override
        public void check(StatusVo state, String searchName) {
            getSubordinateAgrteData();
        }
    };

    private final GameSubordinaterebateHeadModel.onCallBack gameSubordinaterebateHeadModelCallBack = new GameSubordinaterebateHeadModel.onCallBack() {
        @Override
        public void selectStartDate(ObservableField<String> startDate) {
            setStartDate(startDate);
        }

        @Override
        public void selectEndDate(ObservableField<String> endDate) {
            setEndDate(endDate);
        }

        @Override
        public void check(String userName, String startDate, String endDate) {
            getSubordinateRebateData();
        }
    };

    private final GameRebateAgrtHeadModel gameRebateAgrtHeadModel = new GameRebateAgrtHeadModel(gameRebateAgrtHeadModelCallBack);

    private final GameSubordinateagrtHeadModel gameSubordinateagrtHeadModel = new GameSubordinateagrtHeadModel(gameSubordinateagrtHeadModelCallBack);

    private final GameSubordinaterebateHeadModel gameSubordinaterebateHeadModel = new GameSubordinaterebateHeadModel(gameSubordinaterebateHeadModelCallBack);

    private final ArrayList<BindModel> gameRebateDatas = new ArrayList<BindModel>() {{
        gameRebateAgrtHeadModel.setItemType(1);
        add(gameRebateAgrtHeadModel);
    }};
    private final ArrayList<BindModel> subordinateAgrtDatas = new ArrayList<BindModel>() {{
        gameSubordinateagrtHeadModel.setItemType(3);
        add(gameSubordinateagrtHeadModel);
    }};
    private final ArrayList<BindModel> subordinateRebateDatas = new ArrayList<BindModel>() {
        {
            gameSubordinaterebateHeadModel.setItemType(5);
            add(gameSubordinaterebateHeadModel);
        }
    };
    private final BindModel empty = new BindModel();

    public GameRebateAgrtViewModel(@NonNull Application application) {
        super(application);
    }
    public GameRebateAgrtViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    public void initData(RebateAreegmentTypeEnum type) {
        //init data
        this.type = type;
        //设置给头布局场馆类型
        gameRebateAgrtHeadModel.setTypeEnum(type);
        initTab();
        empty.setItemType(7);
        datas.setValue(gameRebateDatas);
        getRebatAgrteData();
    }

    private void initTab() {
        ArrayList<String> tabList = new ArrayList<>();
        switch (type) {
            case LIVE:
                titleData.setValue(LIVE.getName());
                tabList.add("真人返水");
                tabList.add("下级契约");
                tabList.add("下级返水");
                tabs.setValue(tabList);
                break;
            case SPORT:
                titleData.setValue(SPORT.getName());
                tabList.add("体育返水");
                tabList.add("下级契约");
                tabList.add("下级返水");
                tabs.setValue(tabList);
                break;
            case CHESS:
                titleData.setValue(CHESS.getName());
                tabList.add("棋牌返水");
                tabList.add("下级契约");
                tabList.add("下级返水");
                tabs.setValue(tabList);
                break;
            case EGAME:
                titleData.setValue(EGAME.getName());
                tabList.add("电竞返水");
                tabList.add("下级契约");
                tabs.setValue(tabList);
                break;
            case USER:
                titleData.setValue(USER.getName());
                tabList.add("我的日薪");
                tabList.add("下级契约");
                tabList.add("下级日薪");
                tabs.setValue(tabList);
                //隐藏温馨提示
                gameRebateAgrtHeadModel.tipVisible.set(false);
                break;
            case DAYREBATE:
                titleData.setValue(DAYREBATE.getName());
                break;
            default:
                break;
        }
    }
    private void setStartDate(ObservableField<String> date) {
        new XPopup.Builder(mActivity.get())
                .asCustom(DateTimePickerDialog.newInstance(mActivity.get(), getApplication().getString(R.string.start_date), 3,
                        date::set))
                .show();
    }

    private void setEndDate(ObservableField<String> date) {
        new XPopup.Builder(mActivity.get())
                .asCustom(DateTimePickerDialog.newInstance(mActivity.get(), getApplication().getString(R.string.end_date), 3,
                        date::set))
                .show();
    }

    /**
     * 显示契约详情弹窗
     * @param content 内容
     */
    private void showAgrtDetail(String content) {
        MsgDialog dialog = new MsgDialog(mActivity.get(), getApplication().getString(R.string.txt_contractual_norms),
                content,
                true,
                new TipDialog.ICallBack() {
                    @Override
                    public void onClickLeft() {

                    }

                    @Override
                    public void onClickRight() {
                        if (showPop != null) {
                            showPop.dismiss();
                        }
                    }
                });

        showPop = new XPopup.Builder(mActivity.get())
                .dismissOnTouchOutside(true)
                .dismissOnBackPressed(true)
                .asCustom(dialog).show();
    }
    public void setActivity(FragmentActivity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
    }

    @Override
    public void onBack() {
        finish();
    }
    @Override
    public MutableLiveData<String> getTitle() {
        return titleData;
    }

    /**
     * 不同场馆有不同的请求接口
     *
     * @return URL
     */
    private String getRebatAgrteDataURL() {
        switch (type) {
            case LIVE:
                return APIManager.GAMEREBATEAGRT_LIVE_URL;
            case SPORT:
                return APIManager.GAMEREBATEAGRT_SPORT_URL;
            case CHESS:
                return APIManager.GAMEREBATEAGRT_CHESS_URL;
            case EGAME:
                return APIManager.GAMEREBATEAGRT_EGAME_URL;
            case USER:
                return APIManager.GAMEREBATEAGRT_USER_URL;
            case DAYREBATE:
                return APIManager.GAMEREBATEAGRT_DAY_URL;
            default:
                return "";
        }
    }
    private String getSubordinateAgrteDataURL() {
        switch (type) {
            case LIVE:
                return APIManager.GAMESUBORDINATEAGRTE_LIVE_URL;
            case SPORT:
                return APIManager.GAMESUBORDINATEAGRTE_SPORT_URL;
            case CHESS:
                return APIManager.GAMESUBORDINATEAGRTE_CHESS_URL;
            case EGAME:
                return APIManager.GAMESUBORDINATEAGRTE_EGAME_URL;
            case USER:
                return APIManager.GAMESUBORDINATEAGRTE_USER_URL;
            default:
                return "";
        }
    }
    private String getSubordinateRebateDataURL() {
        switch (type) {
            case LIVE:
                return APIManager.GAMESUBORDINATEREBATE_LIVE_URL;
            case SPORT:
                return APIManager.GAMESUBORDINATEREBATE_SPORT_URL;
            case CHESS:
                return APIManager.GAMESUBORDINATEREBATE_CHESS_URL;
            case USER:
                return APIManager.GAMESUBORDINATEREBATE_USER_URL;
            default:
                return "";
        }
    }
    private synchronized void getRebatAgrteData() {
        if (getmCompositeDisposable() != null) {
            getmCompositeDisposable().clear();
        }
        GameRebateAgrtRequest gameRebateAgrtRequest = new GameRebateAgrtRequest();
        switch (type) {
            //日分红和其他请求入参不同
            case DAYREBATE:
                gameRebateAgrtRequest.startdate = gameRebateAgrtHeadModel.startDate.get();
                gameRebateAgrtRequest.enddate = gameRebateAgrtHeadModel.endDate.get();
                break;
            default:
                gameRebateAgrtRequest.starttime = gameRebateAgrtHeadModel.startDate.get();
                gameRebateAgrtRequest.endtime = gameRebateAgrtHeadModel.endDate.get();
                break;
        }
        gameRebateAgrtRequest.pstatus = gameRebateAgrtHeadModel.state.get().getShowId();
        gameRebateAgrtRequest.p = gameRebateAgrtHeadModel.p;
        gameRebateAgrtRequest.pn = gameRebateAgrtHeadModel.pn;
        Disposable disposable = model.getGameRebateAgrtData(getRebatAgrteDataURL(), gameRebateAgrtRequest)
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        //重新加载弹dialog
                        if (gameRebateAgrtRequest.p <= 1) {
                            LoadingDialog.show(mActivity.get());
                        }
                    }
                })
                .subscribeWith(new HttpCallBack<GameRebateAgrtResponse>() {
                    @Override
                    public void onResult(GameRebateAgrtResponse vo) {

                        if (vo != null) {

                            //p<=1说明是第一页数据
                            if (gameRebateAgrtRequest.p <= 1) {
                                gameRebateDatas.clear();
                                gameRebateAgrtHeadModel.yesterdayRebate.set(vo.getUser().getIscreditaccount());
                                if (vo.getContract() != null && vo.getContract().getRule() != null && vo.getContract().getRule().size() > 0) {
                                    //设置规则提示
                                    gameRebateAgrtHeadModel.setRules(vo.getContract().getRule());
                                }

                                gameRebateDatas.add(gameRebateAgrtHeadModel);
                                GameRebateAgrtResponse.TotalDTO total = vo.getTotal();
                                if (total != null) {
                                    GameRebateAgrtTotalModel totalModel = new GameRebateAgrtTotalModel();
                                    switch (type) {
                                        case DAYREBATE:
                                            totalModel.setItemType(9);
                                            totalModel.setSum_bet(total.getSum_bet());
                                            totalModel.setLossAmout(total.getSumLossAmount());
                                            totalModel.setPeople(total.getSumPeople());
                                            totalModel.setSum_sub_money(total.getSum_sub_money());
                                            totalModel.setSum_self_money(total.getSum_self_money());
                                            break;
                                        default:
                                            totalModel.setItemType(6);
                                            totalModel.setSum_bet(total.getSum_bet());
                                            totalModel.setSum_total_money(total.getSum_total_money());
                                            totalModel.setSum_effective_bet(total.getSum_effective_bet());
                                            totalModel.setSum_sub_money(total.getSum_sub_money());
                                            totalModel.setSum_self_money(total.getSum_self_money());
                                            break;
                                    }
                                    gameRebateDatas.add(totalModel);
                                }
                            }

                            if (vo.getData() != null) {
                                for (GameRebateAgrtResponse.DataDTO dataDTO : vo.getData()) {
                                    GameRebateAgrtModel model = new GameRebateAgrtModel();
                                    model.setTypeEnum(type);
                                    switch (type) {
                                        case DAYREBATE:
                                            model.setItemType(8);
                                            model.setDate(dataDTO.getCycleDay());
                                            model.setStatus(dataDTO.getPayStatus());
                                            model.setBetAmoutDay(dataDTO.getBet());
                                            model.setLossAmount(dataDTO.getLossAmount());
                                            model.setPeople(dataDTO.getPeople());
                                            model.setSubMoney(dataDTO.getSub_money());
                                            model.setMineMoney(dataDTO.getSelfMoney());
                                            break;
                                        default:
                                            model.setItemType(0);
                                            model.setDate(dataDTO.getDate());
                                            model.setStatus(dataDTO.getPstatus());
                                            model.setBetAmoutDay(dataDTO.getBet());
                                            model.setBetAmoutValidity(dataDTO.getEffective_bet());
                                            model.setRebatePercentage(dataDTO.getRatio());
                                            model.setRebateAmout(dataDTO.getTotal_money());
                                            model.setSubMoney(dataDTO.getSub_money());
                                            model.setMineMoney(dataDTO.getSelfMoney());
                                            break;
                                    }
                                    gameRebateDatas.add(model);
                                }
                            } else {
                                //如果小于等于头数据数量则展示缺省条目
                                if (gameRebateDatas.size() <= 1) {
                                    gameRebateDatas.add(empty);
                                }
                            }
                            datas.setValue(gameRebateDatas);

                            //分页状态
                            BaseResponse2.MobilePageVo mobilePage = vo.mobile_page;
                            if (mobilePage != null &&
                                    mobilePage.total_page.equals(String.valueOf(gameRebateAgrtRequest.p))) {
                                loadMoreWithNoMoreData();
                            } else {
                                finishLoadMore(true);
                            }
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
    private synchronized void getSubordinateAgrteData() {
        if (getmCompositeDisposable() != null) {
            getmCompositeDisposable().clear();
        }

        GameSubordinateAgrteRequest gameSubordinateAgrteRequest = new GameSubordinateAgrteRequest();
        gameSubordinateAgrteRequest.pstatus = gameSubordinateagrtHeadModel.state.get().getShowId();
        gameSubordinateAgrteRequest.username = gameSubordinateagrtHeadModel.serachName.get();
        gameSubordinateAgrteRequest.p = gameSubordinateagrtHeadModel.p;
        gameSubordinateAgrteRequest.pn = gameSubordinateagrtHeadModel.pn;

        Disposable disposable = model.getGameSubordinateAgrteData(getSubordinateAgrteDataURL(), gameSubordinateAgrteRequest)
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        //重新加载弹dialog
                        if (gameSubordinateAgrteRequest.p <= 1) {
                            LoadingDialog.show(mActivity.get());
                        }
                    }
                })
                .subscribeWith(new HttpCallBack<GameSubordinateAgrteResponse>() {
                    @Override
                    public void onResult(GameSubordinateAgrteResponse vo) {
                        if (vo != null) {

                            if (gameSubordinateAgrteRequest.p <= 1) {
                                subData = vo;
                                subordinateAgrtDatas.clear();
                                subordinateAgrtDatas.add(gameSubordinateagrtHeadModel);
                            }
                            if (vo.getData() != null && vo.getData().size() > 0) {
                                for (GameSubordinateAgrteResponse.DataDTO dataDTO : vo.getData()) {
                                    GameSubordinateagrtModel model = new GameSubordinateagrtModel();
                                    model.setTypeEnum(type);
                                    model.setItemType(2);
                                    model.setUserName(dataDTO.getUsername());
                                    model.setUserID(dataDTO.getUserid());
                                    model.setSignTime(dataDTO.getSign_time());
                                    model.setEffectDate(dataDTO.getEffect_date());
                                    List<GameSubordinateAgrteResponse.DataDTO.RuleDTO> rule = dataDTO.getRule();
                                    if (rule != null && rule.size() > 0) {
                                        model.setRules(dataDTO.getRule());
                                    }
                                    model.setCreateTime(dataDTO.getCreate_time());
                                    model.setStatus(dataDTO.getStatus());
                                    model.setSname(dataDTO.getSname());
                                    model.setRatioCallback(v -> {
                                        showAgrtDetail(v);
                                    });
                                    subordinateAgrtDatas.add(model);

                                    //如果不是第一次请求则直接插入更新subData
                                    if (gameSubordinateAgrteRequest.p > 1) {
                                        subData.getData().add(dataDTO);
                                    }
                                }
                            } else {
                                if (gameSubordinateAgrteRequest.p <= 1) {
                                    subordinateAgrtDatas.add(empty);
                                }
                            }

                            datas.setValue(subordinateAgrtDatas);
                            finishLoadMore(true);
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
    private synchronized void getSubordinateRebateData() {
        if (getmCompositeDisposable() != null) {
            getmCompositeDisposable().clear();
        }

        GameSubordinateRebateRequest gameSubordinateRebateRequest = new GameSubordinateRebateRequest();
        gameSubordinateRebateRequest.username = gameSubordinaterebateHeadModel.userName.get();
        gameSubordinateRebateRequest.starttime = gameSubordinaterebateHeadModel.startDate.get();
        gameSubordinateRebateRequest.endtime = gameSubordinaterebateHeadModel.endDate.get();
        gameSubordinateRebateRequest.p = gameSubordinaterebateHeadModel.p;
        gameSubordinateRebateRequest.pn = gameSubordinaterebateHeadModel.pn;
        Disposable disposable = model.getGameSubordinateRebateData(getSubordinateRebateDataURL(), gameSubordinateRebateRequest)
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        //重新加载弹dialog
                        if (gameSubordinateRebateRequest.p <= 1) {
                            LoadingDialog.show(mActivity.get());
                        }
                    }
                })
                .subscribeWith(new HttpCallBack<GameSubordinateRebateResponse>() {
                    @Override
                    public void onResult(GameSubordinateRebateResponse vo) {
                        if (vo != null) {

                            if (gameSubordinateRebateRequest.p <= 1) {
                                subordinateRebateDatas.clear();
                                subordinateRebateDatas.add(gameSubordinaterebateHeadModel);
                            }

                            if (vo.getData() != null && vo.getData().size() > 0) {
                                for (GameSubordinateRebateResponse.DataDTO dataDTO : vo.getData()) {
                                    GameSubordinaterebateModel model = new GameSubordinaterebateModel();
                                    model.setItemType(4);
                                    model.setUserName(dataDTO.getUsername());
                                    model.setBet(dataDTO.getBet());
                                    model.setEffectBet(dataDTO.getEffective_bet());
                                    model.setRatio(dataDTO.getRatio());
                                    model.setTotalMoney(dataDTO.getTotal_money());
                                    model.setSelfMoney(dataDTO.getSelf_money());
                                    model.setSubMoney(dataDTO.getSub_money());
                                    model.setType(dataDTO.getType());
                                    model.setCreateTime(dataDTO.getCreate_time());
                                    subordinateRebateDatas.add(model);
                                }
                            } else {
                                if (gameSubordinateRebateRequest.p <= 1) {
                                    subordinateRebateDatas.add(empty);
                                }
                            }

                            datas.setValue(subordinateRebateDatas);
                            BaseResponse2.MobilePageVo mobilePage = vo.mobile_page;
                            if (mobilePage != null &&
                                    mobilePage.total_page.equals(String.valueOf(gameSubordinateRebateRequest.p))) {
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
    public void onTabSelected(TabLayout.Tab tab) {
        tabPosition.set(tab.getPosition());
        finishLoadMore(true);
        switch (tab.getPosition()) {
            //真人返水
            case REBATE_AGRT_TAB:
                datas.setValue(gameRebateDatas);
                getRebatAgrteData();
                break;
            //下级契约
            case Subordinate_Agrte_TAB:
                datas.setValue(subordinateAgrtDatas);
                getSubordinateAgrteData();
                break;
            //下级返水
            case Subordinate_Rebate_TAB:
                datas.setValue(subordinateRebateDatas);
                getSubordinateRebateData();
                break;
        }
    }
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }
    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
    /**
     * 创建契约
     */
    public void createRebateAgrt() {
        //下级契约
        if (tabPosition.get() == Subordinate_Agrte_TAB) {
            RebateAgrtDetailModel rebateAgrtDetailModel = new RebateAgrtDetailModel(CREATE_MODE);
            rebateAgrtDetailModel.setSubData(subData);
            RebateAgrtCreateDialogFragment.show(mActivity.get(), rebateAgrtDetailModel);
        }
    }
    public void createRebateAgrt(GameSubordinateagrtModel subordinateagrtModel) {
        //下级契约
        if (tabPosition.get() == Subordinate_Agrte_TAB) {
            RebateAgrtDetailModel rebateAgrtDetailModel = new RebateAgrtDetailModel(CREATE_MODE);
            rebateAgrtDetailModel.setSubData(subData);
            rebateAgrtDetailModel.setCheckUserId(subordinateagrtModel.getUserID());
            RebateAgrtCreateDialogFragment.show(mActivity.get(), rebateAgrtDetailModel);
        }
    }
    /**
     * 查看契约
     */
    public void checkRebateAgrt(GameSubordinateagrtModel subordinateagrtModel) {
        //下级契约
        if (tabPosition.get() == Subordinate_Agrte_TAB) {
            RebateAgrtDetailModel rebateAgrtDetailModel = new RebateAgrtDetailModel(CHECK_MODE);
            rebateAgrtDetailModel.setSubData(subData);
            rebateAgrtDetailModel.setCheckUserId(subordinateagrtModel.getUserID());
            RebateAgrtCreateDialogFragment.show(mActivity.get(), rebateAgrtDetailModel);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        switch (tabPosition.get()) {
            //真人返水
            case REBATE_AGRT_TAB:
                getRebatAgrteData();
                break;
            //下级契约
            case Subordinate_Agrte_TAB:
                getSubordinateAgrteData();
                break;
            //下级返水
            case Subordinate_Rebate_TAB:
                getSubordinateRebateData();
                break;
        }
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
