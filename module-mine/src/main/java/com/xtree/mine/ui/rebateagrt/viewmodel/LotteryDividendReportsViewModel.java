package com.xtree.mine.ui.rebateagrt.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.xtree.base.mvvm.model.ToolbarModel;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.widget.FilterView;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.ui.rebateagrt.model.DividendAgrtCheckFoot;
import com.xtree.mine.ui.rebateagrt.model.LotteryDividendReportsFootModel;
import com.xtree.mine.ui.rebateagrt.model.LotteryDividendReportsHeadModel;
import com.xtree.mine.ui.rebateagrt.model.LotteryDividendReportsModel;
import com.xtree.mine.ui.rebateagrt.model.LotteryDividendReportsTotalModel;
import com.xtree.mine.vo.StatusVo;
import com.xtree.mine.vo.request.LotteryDividendReportsRequest;
import com.xtree.mine.vo.response.LotteryDividendReportsResponse;

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

/**
 * Created by KAKA on 2024/8/16.
 * Describe: 彩票分红报表viewModel
 */
public class LotteryDividendReportsViewModel extends BaseViewModel<MineRepository> implements ToolbarModel {

    public LotteryDividendReportsViewModel(@NonNull Application application) {
        super(application);
    }

    public LotteryDividendReportsViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    private final MutableLiveData<String> titleData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<ArrayList<BindModel>>(new ArrayList<>());
    public MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_lotterydividend);
                    add(R.layout.item_lotterydividend_head);
                    add(R.layout.item_lotterydividend_total);
                    add(R.layout.item_lotterydividend_foot);
                }
            });
    private WeakReference<FragmentActivity> mActivity = null;

    private final LotteryDividendReportsHeadModel headModel = new LotteryDividendReportsHeadModel(new LotteryDividendReportsHeadModel.OnCallBack() {

        @Override
        public void agent(String title, ObservableField<StatusVo> sort, List<FilterView.IBaseVo> list) {
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
            getReportsData();
        }
    });

    private final ArrayList<BindModel> bindModels = new ArrayList<BindModel>() {{
        headModel.setItemType(1);
        LotteryDividendReportsFootModel footModel = new LotteryDividendReportsFootModel();
        footModel.tip.set(getApplication().getString(R.string.txt_dividend_check_tip3));
        footModel.setItemType(3);
        add(headModel);
        add(footModel);
    }};

    public OnLoadMoreListener onLoadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            headModel.p++;
            getReportsData();
        }
    };

    public void initData() {
        titleData.setValue("彩票分红报表");

        datas.setValue(bindModels);
        getReportsData();
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

    private synchronized void getReportsData() {
        if (getmCompositeDisposable() != null) {
            getmCompositeDisposable().clear();
        }
        LotteryDividendReportsRequest request = new LotteryDividendReportsRequest();

        if (headModel.statuData.get() != null && !TextUtils.isEmpty(headModel.statuData.get().getShowId())) {
            request.setStatus(headModel.statuData.get().getShowId());
        }
        if (headModel.cyclyData.get() != null && !TextUtils.isEmpty(headModel.cyclyData.get().getShowId())) {
            request.setCycleid(headModel.cyclyData.get().getShowId());
        }
        if (headModel.agentData.get() != null && !TextUtils.isEmpty(headModel.agentData.get().getShowId())) {
            request.setAgency_model(headModel.agentData.get().getShowId());
        }

        request.setUsername(headModel.userNameData.get());
        request.setPage(headModel.p);
        request.setLimit(headModel.pn);

        Disposable disposable = model.getLotteryDividendReportsData(request)
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        //重新加载弹dialog
                        if (request.getPage() <= 1) {
                            LoadingDialog.show(mActivity.get());
                        }
                    }
                })
                .subscribeWith(new HttpCallBack<LotteryDividendReportsResponse>() {
                    @Override
                    public void onResult(LotteryDividendReportsResponse vo) {

                        if (vo != null) {
                            //p<=1说明是第一页数据
                            if (request.getPage() <= 1) {
                                bindModels.clear();

                                HashMap<String, String> agentList = vo.getData().getAgentList();
                                List<LotteryDividendReportsResponse.DataDTO.CyclesDTO> cycles = vo.getData().getCycles();
                                ArrayList<FilterView.IBaseVo> agents = new ArrayList<>();
                                if (agentList != null) {
                                    for (Map.Entry<String, String> stringStringEntry : agentList.entrySet()) {
                                        agents.add(new StatusVo(stringStringEntry.getKey(), stringStringEntry.getValue()));
                                    }
                                }
                                headModel.setAgentList(agents);

                                ArrayList<FilterView.IBaseVo> cs = new ArrayList<>();

                                if (cycles != null) {
                                    for (LotteryDividendReportsResponse.DataDTO.CyclesDTO stringCyclesDTOEntry : cycles) {
                                        StatusVo statusVo = new StatusVo(stringCyclesDTOEntry.getId(), stringCyclesDTOEntry.getTitle());
                                        cs.add(statusVo);
                                        //设置当前筛选的周期
                                        if (vo.getData().getS().getCycleid().equals(stringCyclesDTOEntry.getId())) {
                                            headModel.cyclyData.set(statusVo);
                                        }
                                    }
                                }
                                headModel.setCyclytList(cs);

                                //total
                                LotteryDividendReportsTotalModel totalModel = new LotteryDividendReportsTotalModel();
                                totalModel.setItemType(2);

                                LotteryDividendReportsResponse.DataDTO.AllCountDTO allCount = vo.getData().getAllCount();
                                if (allCount != null) {
                                    totalModel.setProfitloss(allCount.getProfitloss());
                                    totalModel.setNetloss(allCount.getNetloss());
                                    totalModel.setSelf_money(allCount.getSelfMoney());
                                }

                                bindModels.add(headModel);
                                bindModels.add(totalModel);
                            }


                            List<LotteryDividendReportsResponse.DataDTO.AListDTO> billData = vo.getData().getAList();

                            if (billData != null) {
                                for (LotteryDividendReportsResponse.DataDTO.AListDTO partDatum : billData) {
                                    LotteryDividendReportsModel lotteryDividendReportsModel = new LotteryDividendReportsModel();
                                    lotteryDividendReportsModel.setUsername(partDatum.getUsername());
                                    lotteryDividendReportsModel.setDay_people(partDatum.getDayPeople());
                                    lotteryDividendReportsModel.setPeople(partDatum.getPeople());
                                    lotteryDividendReportsModel.setNetloss(partDatum.getNetloss());
                                    lotteryDividendReportsModel.setRatio(partDatum.getRatio());
                                    lotteryDividendReportsModel.setProfitloss(partDatum.getProfitloss());
                                    lotteryDividendReportsModel.setSelf_money(partDatum.getSelfMoney());
                                    lotteryDividendReportsModel.setTitle(partDatum.getTitle());
                                    lotteryDividendReportsModel.setSub_money(partDatum.getSubMoney());
                                    lotteryDividendReportsModel.setWeek_people(partDatum.getWeekPeople());

                                    bindModels.add(lotteryDividendReportsModel);
                                }
                            }

                            datas.setValue(bindModels);

                            String totalPage = vo.getData().getTotalPage();
                            String pagestring = "个记录,  分为 ";
                            String pages = vo.getData().getPages();
                            int idex = pages.indexOf(pagestring);
                            if (idex > 0) {
                                int startIndex = idex + pagestring.length();
                                int endIndex = startIndex + 1;
                                String totalPageString = vo.getData().getPages().substring(startIndex, endIndex);
                                if (!TextUtils.isEmpty(totalPageString)) {
                                    totalPage = totalPageString;
                                }
                            }
                            if (totalPage.equals(String.valueOf(request.getPage()))) {
                                loadMoreWithNoMoreData();
                            } else if (billData == null || billData.size() < request.getLimit()) {
                                //返回数据有时页码总数显示错误，所以做此兼容
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
