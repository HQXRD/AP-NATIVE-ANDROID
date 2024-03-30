package com.xtree.mine.ui.rebateagrt.viewmodel;

import static com.xtree.mine.ui.rebateagrt.model.RebateAreegmentTypeEnum.GAMEREPORTS;
import static com.xtree.mine.ui.rebateagrt.model.RebateAreegmentTypeEnum.LOTTERIESREPORTS;

import android.app.Application;

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
import com.xtree.mine.ui.rebateagrt.model.RebateAreegmentTypeEnum;
import com.xtree.mine.ui.rebateagrt.model.RecommendedReportsHeadModel;
import com.xtree.mine.ui.rebateagrt.model.RecommendedReportsModel;
import com.xtree.mine.vo.StatusVo;
import com.xtree.mine.vo.request.RecommendedReportsRequest;
import com.xtree.mine.vo.response.RecommendedReportsResponse;

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

/**
 * Created by KAKA on 2024/3/15.
 * Describe: 游戏推荐报表 viewmodel
 */
public class RecommendedReportsViewModel extends BaseViewModel<MineRepository> implements ToolbarModel {


    private final MutableLiveData<String> titleData = new MutableLiveData<>();
    private final BindModel empty = new BindModel();
    public MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<ArrayList<BindModel>>(new ArrayList<>());
    public MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_recommendedreports);
                    add(R.layout.item_recommendedreports_head);
                    add(R.layout.item_empty);
                }
            });
    private RebateAreegmentTypeEnum type;
    private WeakReference<FragmentActivity> mActivity = null;
    public RecommendedReportsViewModel(@NonNull Application application) {
        super(application);
    }    /**
     * 列表加载
     */
    public OnLoadMoreListener onLoadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            headModel.p++;
            getRecommendedReportsData();
        }
    };
    private final RecommendedReportsHeadModel headModel = new RecommendedReportsHeadModel(new RecommendedReportsHeadModel.OnCallBack() {
        @Override
        public void cyclicality(String title, ObservableField<StatusVo> cycly, List<FilterView.IBaseVo> list) {
            showFilter(title, cycly, list);
        }

        @Override
        public void check() {
            getRecommendedReportsData();
        }
    });
    private final ArrayList<BindModel> bindModels = new ArrayList<BindModel>() {{
        headModel.setItemType(1);
        add(headModel);
    }};

    public RecommendedReportsViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    public void initData(RebateAreegmentTypeEnum type) {
        //init data
        this.type = type;
        switch (type) {
            case LOTTERIESREPORTS:  //彩票报表
                headModel.type = "21";
                titleData.setValue(LOTTERIESREPORTS.getName());
                break;
            case GAMEREPORTS: //游戏报表
                headModel.type = "22";
                titleData.setValue(GAMEREPORTS.getName());
                break;
        }
        empty.setItemType(2);
        datas.setValue(bindModels);
        getRecommendedReportsData();
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
    private synchronized void getRecommendedReportsData() {
        if (getmCompositeDisposable() != null) {
            getmCompositeDisposable().clear();
        }
        RecommendedReportsRequest request = new RecommendedReportsRequest();
        if (headModel.cyclyData.get() != null) {
            request.cycle_id = headModel.cyclyData.get().getShowId();
        }
        request.type = headModel.type;
        request.p = headModel.p;
        request.pn = headModel.pn;
        Disposable disposable = model.getRecommendedReportsData(request)
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        //重新加载弹dialog
                        if (request.p <= 1) {
                            LoadingDialog.show(mActivity.get());
                        }
                    }
                })
                .subscribeWith(new HttpCallBack<RecommendedReportsResponse>() {
                    @Override
                    public void onResult(RecommendedReportsResponse vo) {

                        if (vo != null) {

                            //p<=1说明是第一页数据
                            if (request.p <= 1) {
                                bindModels.clear();
                                HashMap<String, RecommendedReportsResponse.CyclesDTO> cycles = vo.getCycles();
                                ArrayList<FilterView.IBaseVo> cs = new ArrayList<>();
                                for (Map.Entry<String, RecommendedReportsResponse.CyclesDTO> stringCyclesDTOEntry : cycles.entrySet()) {
                                    StatusVo statusVo = new StatusVo(stringCyclesDTOEntry.getKey(), stringCyclesDTOEntry.getValue().getTitle());
                                    cs.add(statusVo);
                                }
                                headModel.setCyclytList(cs);
                                bindModels.add(headModel);
                            }

                            RecommendedReportsResponse.ChildrenBillDTO childrenBill = vo.getChildrenBill();
                            if (childrenBill != null) {
                                List<RecommendedReportsResponse.ChildrenBillDTO.DataDTO> data = childrenBill.getData();
                                if (data != null && data.size() > 0) {
                                    for (RecommendedReportsResponse.ChildrenBillDTO.DataDTO dataDTO : data) {
                                        RecommendedReportsModel recommendedReportsModel = new RecommendedReportsModel();
                                        recommendedReportsModel.setBet(dataDTO.getBet());
                                        recommendedReportsModel.setProfitloss(dataDTO.getProfitloss());
                                        recommendedReportsModel.setPeople(dataDTO.getPeople());
                                        if (dataDTO.getUsername() != null) {
                                            recommendedReportsModel.setUserName(dataDTO.getUsername());
                                        } else {
                                            recommendedReportsModel.setUserName(dataDTO.getRefUsername());
                                        }
                                        recommendedReportsModel.setLabel(dataDTO.getLabel());
                                        recommendedReportsModel.setCycle(headModel.cyclyData.get().getShowName());
                                        bindModels.add(recommendedReportsModel);
                                    }
                                } else {
                                    if (request.p <= 1) {
                                        bindModels.add(empty);
                                    }
                                }
                            }

                            datas.setValue(bindModels);

                            BaseResponse2.MobilePageVo mobilePage = vo.mobile_page;
                            if (mobilePage != null &&
                                    mobilePage.total_page.equals(String.valueOf(request.p))) {
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
