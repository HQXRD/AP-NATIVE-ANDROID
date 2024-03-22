package com.xtree.mine.ui.rebateagrt.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.drake.brv.BindingAdapter;
import com.xtree.base.mvvm.model.ToolbarModel;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.ui.rebateagrt.fragment.GameDividendAgrtFragment;
import com.xtree.mine.ui.rebateagrt.fragment.GameRebateAgrtFragment;
import com.xtree.mine.ui.rebateagrt.fragment.RecommendedReportsFragment;
import com.xtree.mine.ui.rebateagrt.model.RebateAreegmentModel;
import com.xtree.mine.ui.rebateagrt.model.RebateAreegmentTypeEnum;
import com.xtree.mine.vo.response.FunctionMenuResponse;

import org.reactivestreams.Subscription;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.RxBus;

/**
 * Created by KAKA on 2024/3/8.
 * Describe: 返水契约 viewmodel
 */
public class RebateAgreementViewModel extends BaseViewModel<MineRepository> implements ToolbarModel {

    public RebateAgreementViewModel(@NonNull Application application) {
        super(application);
    }

    public RebateAgreementViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    private WeakReference<FragmentActivity> mActivity = null;

    private final MutableLiveData<String> titleData = new MutableLiveData<>(
            getApplication().getString(R.string.rebate_agrt_title)
    );

    @SuppressLint("UseCompatLoadingForDrawables")
    public MutableLiveData<ArrayList<RebateAreegmentModel>> datas = new MutableLiveData<>();

    private final ArrayList<RebateAreegmentModel> bindModels = new ArrayList<RebateAreegmentModel>() {
        {
            add(new RebateAreegmentModel(RebateAreegmentTypeEnum.LIVE));
            add(new RebateAreegmentModel(RebateAreegmentTypeEnum.SPORT));
            add(new RebateAreegmentModel(RebateAreegmentTypeEnum.CHESS));
            add(new RebateAreegmentModel(RebateAreegmentTypeEnum.EGAME));
            add(new RebateAreegmentModel(RebateAreegmentTypeEnum.USER));
            add(new RebateAreegmentModel(RebateAreegmentTypeEnum.LOTTERIES));
            add(new RebateAreegmentModel(RebateAreegmentTypeEnum.GAMEREPORTS));
            add(new RebateAreegmentModel(RebateAreegmentTypeEnum.LOTTERIESREPORTS));
            add(new RebateAreegmentModel(RebateAreegmentTypeEnum.GAMEREBATE));
        }
    };

    public MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(new ArrayList<Integer>(){
        {
            add(R.layout.item_rebate_agreement);
        }
    });

    public BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {

        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {

        }

        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {

            if (datas.getValue() != null) {
                RebateAreegmentModel bindModel = datas.getValue().get(modelPosition);
                switch (bindModel.type) {
                    case LIVE:
                    case SPORT:
                    case CHESS:
                    case EGAME:
                    case USER:
                        startContainerActivity(GameRebateAgrtFragment.class.getCanonicalName());
                        break;
                    case LOTTERIES:
                    case GAMEREBATE:
                        startContainerActivity(GameDividendAgrtFragment.class.getCanonicalName());
                        break;
                    case LOTTERIESREPORTS:
                    case GAMEREPORTS:
                        startContainerActivity(RecommendedReportsFragment.class.getCanonicalName());
                        break;
                    default:
                        break;
                }
                RxBus.getDefault().postSticky(bindModel);
            }
        }
    };

    public void initData() {

        //初始化菜单
        Disposable disposable = (Disposable) model.getFunctionMenuData()
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        //重新加载弹dialog
                        LoadingDialog.show(mActivity.get());
                    }
                })
                .subscribeWith(new HttpCallBack<List<FunctionMenuResponse>>() {
                    @Override
                    public void onResult(List<FunctionMenuResponse> data) {

                        ArrayList<RebateAreegmentModel> newDatas = new ArrayList<>();

                        for (int i = 0; i < bindModels.size(); i++) {
                            RebateAreegmentModel raMenu = bindModels.get(i);
                            for (FunctionMenuResponse datum : data) {
                                if (datum != null) {
                                    String id = datum.getId();
                                    if (raMenu.type.getIds().contains(id)) {
                                        newDatas.add(raMenu);
                                        break;
                                    }
                                }
                            }
                        }
                        datas.setValue(newDatas);
                    }
                });
        addSubscribe(disposable);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mActivity != null) {
            mActivity.clear();
            mActivity = null;
        }
    }
}
