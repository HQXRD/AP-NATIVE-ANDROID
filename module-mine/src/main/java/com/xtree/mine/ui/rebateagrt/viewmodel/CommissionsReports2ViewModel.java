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
import com.xtree.mine.ui.rebateagrt.model.CommissionsReports2HeadModel;
import com.xtree.mine.ui.rebateagrt.model.CommissionsReports2Model;
import com.xtree.mine.ui.rebateagrt.model.CommissionsReports2TotalModel;
import com.xtree.mine.vo.StatusVo;
import com.xtree.mine.vo.request.CommissionsReports2Request;
import com.xtree.mine.vo.response.CommissionsReports2Response;

import org.reactivestreams.Subscription;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.http.BusinessException;

/**
 * Created by KAKA on 2024/3/29.
 * Describe: 佣金报表查看viewModel
 */
public class CommissionsReports2ViewModel extends BaseViewModel<MineRepository> implements ToolbarModel {

    public CommissionsReports2ViewModel(@NonNull Application application) {
        super(application);
    }

    public CommissionsReports2ViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    private final MutableLiveData<String> titleData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<ArrayList<BindModel>>(new ArrayList<>());
    public MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_commissionsreports2);
                    add(R.layout.item_commissionsreports2_head);
                    add(R.layout.item_commissionsreports2_total);
                }
            });
    private WeakReference<FragmentActivity> mActivity = null;

    private final CommissionsReports2HeadModel headModel = new CommissionsReports2HeadModel(new CommissionsReports2HeadModel.OnCallBack() {

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
        add(headModel);
    }};

    public OnLoadMoreListener onLoadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            headModel.p++;
            getReportsData();
        }
    };

    public void initData() {
        titleData.setValue("体育分红");

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
        CommissionsReports2Request request = new CommissionsReports2Request();

        if (headModel.statuData.get() != null && !TextUtils.isEmpty(headModel.statuData.get().getShowId())) {
            request.setStatus(headModel.statuData.get().getShowId());
        }
        request.setSort(headModel.sortData.get());
        request.setStart_month(headModel.startTime);
        request.setEnd_month(headModel.endTime);
        request.setUsername(headModel.userNameData.get());
        request.setPage(headModel.p);
        request.setLimit(headModel.pn);

        Disposable disposable = model.getCommissions2Data(request)
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        //重新加载弹dialog
                        if (request.getPage() <= 1) {
                            LoadingDialog.show(mActivity.get());
                        }
                    }
                })
                .subscribeWith(new HttpCallBack<CommissionsReports2Response>() {
                    @Override
                    public void onResult(CommissionsReports2Response vo) {

                        if (vo != null) {
                            //p<=1说明是第一页数据
                            if (request.getPage() <= 1) {
                                bindModels.clear();

                                //total
                                CommissionsReports2TotalModel totalModel = new CommissionsReports2TotalModel();
                                totalModel.setItemType(2);

                                //自己的契约数据
                                CommissionsReports2Response.DataDTOX.AllCountDTO allCount = vo.getData().getAllCount();
                                if (allCount != null) {
                                    totalModel.setTotalMoney(allCount.getDue());
                                }

                                bindModels.add(headModel);
                                bindModels.add(totalModel);
                            }


                            CommissionsReports2Response.DataDTOX.DataDTO billData = vo.getData().getData();

                            if (billData != null) {
                                List<CommissionsReports2Response.DataDTOX.DataDTO.PartDTO> partData = billData.getPart();
                                if (partData != null) {
                                    for (CommissionsReports2Response.DataDTOX.DataDTO.PartDTO partDatum : partData) {
                                        CommissionsReports2Model commissionsReports2Model = new CommissionsReports2Model();
                                        commissionsReports2Model.setUsername(partDatum.getUsername());
                                        commissionsReports2Model.setActivity_people(partDatum.getNewActivityPeople());
                                        commissionsReports2Model.setPay_fee(partDatum.getPayFee());
                                        commissionsReports2Model.setFee(partDatum.getFee());
                                        commissionsReports2Model.setIncome(partDatum.getIncome());
                                        commissionsReports2Model.setDue(partDatum.getDue());
                                        commissionsReports2Model.setC_agency_model(partDatum.getCAgencyModel());
                                        commissionsReports2Model.setProfit(partDatum.getProfit());
                                        commissionsReports2Model.setWages(partDatum.getWages());
                                        commissionsReports2Model.setRemain(partDatum.getRemain());
                                        commissionsReports2Model.setPlan_ratio(partDatum.getPlanRatio());
                                        commissionsReports2Model.setSys_adjust(partDatum.getSysAdjust());

                                        bindModels.add(commissionsReports2Model);
                                    }
                                }
                            }

                            datas.setValue(bindModels);

//                            BaseResponse2.MobilePageVo mobilePage = vo.mobile_page;
//                            if (mobilePage != null &&
//                                    mobilePage.total_page.equals(String.valueOf(request.getPage()))) {
//                                loadMoreWithNoMoreData();
//                            } else {
//                                finishLoadMore(true);
//                            }
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
