package com.xtree.mine.ui.rebateagrt.viewmodel;

import static com.xtree.base.mvvm.ExKt.initData;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.tabs.TabLayout;
import com.lxj.xpopup.XPopup;
import com.xtree.base.mvvm.model.ToolbarModel;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.widget.DateTimePickerDialog;
import com.xtree.base.widget.FilterView;
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
import com.xtree.mine.ui.rebateagrt.model.RebateAreegmentTypeEnum;
import com.xtree.mine.vo.StatusVo;
import com.xtree.mine.vo.request.GameRebateAgrtRequest;
import com.xtree.mine.vo.request.GameSubordinateAgrteRequest;
import com.xtree.mine.vo.request.GameSubordinateRebateRequest;
import com.xtree.mine.vo.response.GameRebateAgrtResponse;
import com.xtree.mine.vo.response.GameSubordinateAgrteResponse;
import com.xtree.mine.vo.response.GameSubordinateRebateResponse;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;

/**
 * Created by KAKA on 2024/3/9.
 * Describe: 游戏场馆返水契约viewmodel
 */
public class GameRebateAgrtViewModel extends BaseViewModel<MineRepository> implements ToolbarModel, TabLayout.OnTabSelectedListener {

    public GameRebateAgrtViewModel(@NonNull Application application) {
        super(application);
    }

    public GameRebateAgrtViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);

    }

    private RebateAreegmentTypeEnum type;

    private WeakReference<FragmentActivity> mActivity = null;

    private final MutableLiveData<String> titleData = new MutableLiveData<>(
            getApplication().getString(R.string.rebate_agrt_title)
    );

    public MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<ArrayList<BindModel>>(new ArrayList<>());

    public MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_game_rebateagrt);
                    add(R.layout.item_game_rebateagrt_head);
                    add(R.layout.item_game_subordinateagrt);
                    add(R.layout.item_game_subordinateagrt_head);
                    add(R.layout.item_game_subordinaterebate);
                    add(R.layout.item_game_subordinaterebate_head);
                    add(R.layout.item_game_rebateagrt_total);
                }
            });

    public MutableLiveData<ArrayList<String>> tabs = new MutableLiveData<>();

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

    private final ArrayList<BindModel> gameRebateDatas = new ArrayList<BindModel>(){{
        gameRebateAgrtHeadModel.setItemType(1);
        add(gameRebateAgrtHeadModel);
    }};

    private final ArrayList<BindModel> subordinateAgrtDatas  = new ArrayList<BindModel>(){{
        gameSubordinateagrtHeadModel.setItemType(3);
        add(gameSubordinateagrtHeadModel);
    }};

    private final ArrayList<BindModel> subordinateRebateDatas  = new ArrayList<BindModel>(){
        {
            gameSubordinaterebateHeadModel.setItemType(5);
            add(gameSubordinaterebateHeadModel);
        }
    };

    public void initData(RebateAreegmentTypeEnum type) {
        //init data
        this.type = type;
        initTab();
        datas.setValue(gameRebateDatas);
        getRebatAgrteData();
    }

    private void initTab() {
        ArrayList<String> tabList = new ArrayList<>();
        switch (type) {
            case LIVE:
                titleData.setValue("真人返水契约");
                tabList.add("真人返水");
                tabList.add("下级契约");
                tabList.add("下级返水");
                tabs.setValue(tabList);
                break;
            case SPORT:
                titleData.setValue("体育返水契约");
                tabList.add("体育返水");
                tabList.add("下级契约");
                tabList.add("下级返水");
                tabs.setValue(tabList);
                break;
            case CHESS:
                titleData.setValue("棋牌返水契约");
                tabList.add("棋牌返水");
                tabList.add("下级契约");
                tabList.add("下级返水");
                tabs.setValue(tabList);
                break;
            case EGAME:
                titleData.setValue("电竞返水契约");
                tabList.add("电竞返水");
                tabList.add("下级契约");
                tabs.setValue(tabList);
                break;
            case USER:
                titleData.setValue("时薪");
                tabList.add("我的时薪");
                tabList.add("下级契约");
                tabList.add("下级时薪");
                tabs.setValue(tabList);
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
     * @return URL
     */
    private String getRebatAgrteDataURL(){
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
            default:
                return "";
        }
    }

    private String getSubordinateAgrteDataURL(){
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

    private String getSubordinateRebateDataURL(){
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

    private void getRebatAgrteData() {
        if (getmCompositeDisposable() != null) {
            getmCompositeDisposable().clear();
        }
        GameRebateAgrtRequest gameRebateAgrtRequest = new GameRebateAgrtRequest();
        gameRebateAgrtRequest.starttime = gameRebateAgrtHeadModel.startDate.get();
        gameRebateAgrtRequest.endtime = gameRebateAgrtHeadModel.endDate.get();
        gameRebateAgrtRequest.pstatus = gameRebateAgrtHeadModel.state.get().getShowId();
        Disposable disposable = (Disposable) model.getGameRebateAgrtData(getRebatAgrteDataURL(), gameRebateAgrtRequest)
                .subscribeWith(new HttpCallBack<GameRebateAgrtResponse>() {
                    @Override
                    public void onResult(GameRebateAgrtResponse vo) {

                        if (vo != null) {
                            gameRebateDatas.clear();

                            gameRebateAgrtHeadModel.yesterdayRebate.set(vo.getUser().getIscreditaccount());
                            gameRebateDatas.add(gameRebateAgrtHeadModel);
                            GameRebateAgrtResponse.TotalDTO total = vo.getTotal();
                            if (total != null) {
                                GameRebateAgrtTotalModel totalModel = new GameRebateAgrtTotalModel();
                                totalModel.setItemType(6);
                                totalModel.sum_bet = total.getSum_bet() + "";
                                totalModel.sum_total_money = String.valueOf(total.getSum_total_money());
                                totalModel.sum_effective_bet = total.getSum_effective_bet() + "";
                                totalModel.sum_sub_money = String.valueOf(total.getSum_sub_money());
                                totalModel.sum_liushui = total.getSum_liushui() + "";
                                totalModel.sum_self_money = total.getSum_self_money() + "";
                                gameRebateDatas.add(totalModel);
                            }
                            if (vo.getData() != null) {
                                for (GameRebateAgrtResponse.DataDTO dataDTO : vo.getData()) {
                                    GameRebateAgrtModel model = new GameRebateAgrtModel();
                                    model.date = dataDTO.getDate();
                                    model.setStatus(dataDTO.getPstatus());
                                    model.betAmoutDay = dataDTO.getLiushui();
                                    model.betAmoutValidity = dataDTO.getEffective_bet();
                                    model.rebatePercentage = dataDTO.getRatio();
                                    model.rebateAmout = dataDTO.getTotal_money();
                                    model.subMoney = dataDTO.getSub_money();
                                    model.mineMoney = dataDTO.getSelf_money() + "";
                                    gameRebateDatas.add(model);
                                }
                            }
                            datas.setValue(gameRebateDatas);
                        }

                    }
                });
        addSubscribe(disposable);
    }

    private void getSubordinateAgrteData() {
        if (getmCompositeDisposable() != null) {
            getmCompositeDisposable().clear();
        }

        GameSubordinateAgrteRequest gameSubordinateAgrteRequest = new GameSubordinateAgrteRequest();
        gameSubordinateAgrteRequest.pstatus = gameSubordinateagrtHeadModel.state.get().getShowId();
        gameSubordinateAgrteRequest.username = gameSubordinateagrtHeadModel.serachName.get();
        Disposable disposable = (Disposable) model.getGameSubordinateAgrteData(getSubordinateAgrteDataURL(), gameSubordinateAgrteRequest)
                .subscribeWith(new HttpCallBack<GameSubordinateAgrteResponse>() {
                    @Override
                    public void onResult(GameSubordinateAgrteResponse vo) {
                        if (vo != null) {
                            subordinateAgrtDatas.clear();

                            subordinateAgrtDatas.add(gameSubordinateagrtHeadModel);
                            if (vo.getData() != null) {
                                for (GameSubordinateAgrteResponse.DataDTO dataDTO : vo.getData()) {
                                    GameSubordinateagrtModel model = new GameSubordinateagrtModel();
                                    model.setItemType(2);
                                    model.userName = dataDTO.getUsername();
                                    model.signTime = dataDTO.getSign_time();
                                    model.effectDate = dataDTO.getEffect_date();
                                    List<GameSubordinateAgrteResponse.DataDTO.RuleDTO> rule = dataDTO.getRule();
                                    if (rule != null && rule.size() > 0) {
                                        model.ruleRatio = dataDTO.getRule().get(0).getRatio();
                                    }
                                    model.createTime = dataDTO.getCreate_time();
                                    model.sName = dataDTO.getSname();
                                    subordinateAgrtDatas.add(model);
                                }
                            }
                            datas.setValue(subordinateAgrtDatas);
                        }
                    }
                });
        addSubscribe(disposable);
    }

    private void getSubordinateRebateData() {
        if (getmCompositeDisposable() != null) {
            getmCompositeDisposable().clear();
        }

        GameSubordinateRebateRequest gameSubordinateRebateRequest = new GameSubordinateRebateRequest();
        gameSubordinateRebateRequest.username = gameSubordinaterebateHeadModel.userName.get();
        gameSubordinateRebateRequest.starttime = gameSubordinaterebateHeadModel.startDate.get();
        gameSubordinateRebateRequest.endtime = gameSubordinaterebateHeadModel.endDate.get();
        Disposable disposable = (Disposable) model.getGameSubordinateRebateData(getSubordinateRebateDataURL(), gameSubordinateRebateRequest)
                .subscribeWith(new HttpCallBack<GameSubordinateRebateResponse>() {
                    @Override
                    public void onResult(GameSubordinateRebateResponse vo) {
                        if (vo != null) {
                            subordinateRebateDatas.clear();

                            subordinateRebateDatas.add(gameSubordinaterebateHeadModel);
                            if (vo.getData() != null) {
                                for (GameSubordinateRebateResponse.DataDTO dataDTO : vo.getData()) {
                                    GameSubordinaterebateModel model = new GameSubordinaterebateModel();
                                    model.setItemType(4);
                                    model.userName = dataDTO.getUsername();
                                    model.bet = dataDTO.getBet();
                                    model.effectBet = dataDTO.getEffective_bet();
                                    model.ratio = dataDTO.getRatio();
                                    model.totalMoney = dataDTO.getTotal_money();
                                    model.selfMoney = dataDTO.getSelf_money();
                                    model.subMoney = dataDTO.getSub_money();
                                    model.type = dataDTO.getType();
                                    model.createTime = dataDTO.getCreate_time();
                                    subordinateRebateDatas.add(model);
                                }
                            }
                            datas.setValue(subordinateRebateDatas);
                        }
                    }
                });
        addSubscribe(disposable);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                datas.setValue(gameRebateDatas);
                getRebatAgrteData();
                break;
            case 1:
                datas.setValue(subordinateAgrtDatas);
                getSubordinateAgrteData();
                break;
            case 2:
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

    public void createRebateAgrt() {
        new RebateAgrtCreateDialogFragment().showNow(mActivity.get().getSupportFragmentManager(), RebateAgrtCreateDialogFragment.class.getName());
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
