package com.xtree.mine.ui.rebateagrt.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.xtree.base.mvvm.model.ToolbarModel;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.mine.R;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.ui.rebateagrt.model.RebateAreegmentTypeEnum;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import me.xtree.mvvmhabit.base.BaseViewModel;

/**
 * Created by KAKA on 2024/3/29.
 * Describe: 佣金报表viewModel
 */
public class CommissionsReportsViewModel extends BaseViewModel<MineRepository> implements ToolbarModel {

    private final MutableLiveData<String> titleData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<ArrayList<BindModel>>(new ArrayList<>());
    public MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_recommendedreports);
                    add(R.layout.item_recommendedreports_head);
                    add(R.layout.item_empty);
                }
            });
    private WeakReference<FragmentActivity> mActivity = null;

    public CommissionsReportsViewModel(@NonNull Application application) {
        super(application);
    }

    public CommissionsReportsViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    public void initData(RebateAreegmentTypeEnum type) {
        //init data
        getRecommendedReportsData();
    }

    public void setActivity(FragmentActivity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
    }

    private synchronized void getRecommendedReportsData() {
        if (getmCompositeDisposable() != null) {
            getmCompositeDisposable().clear();
        }

//        Disposable disposable = model.getRecommendedReportsData(request)
//                .doOnSubscribe(new Consumer<Subscription>() {
//                    @Override
//                    public void accept(Subscription subscription) throws Exception {
//                        //重新加载弹dialog
//                        if (request.p <= 1) {
//                            LoadingDialog.show(mActivity.get());
//                        }
//                    }
//                })
//                .subscribeWith(new HttpCallBack<RecommendedReportsResponse>() {
//                    @Override
//                    public void onResult(RecommendedReportsResponse vo) {
//
//                    }
//
//                    @Override
//                    public void onFail(BusinessException t) {
//                        super.onFail(t);
//                        finishLoadMore(false);
//                    }
//                });
//        addSubscribe(disposable);
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
