package com.xtree.mine.ui.rebateagrt.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.drake.brv.BindingAdapter;
import com.xtree.base.mvvm.model.ToolbarModel;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.mine.R;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.data.source.APIManager;
import com.xtree.mine.ui.rebateagrt.fragment.GameDividendAgrtFragment;
import com.xtree.mine.ui.rebateagrt.fragment.GameRebateAgrtFragment;
import com.xtree.mine.ui.rebateagrt.fragment.RecommendedReportsFragment;
import com.xtree.mine.ui.rebateagrt.model.RebateAreegmentModel;
import com.xtree.mine.ui.rebateagrt.model.RebateAreegmentTypeEnum;

import java.util.ArrayList;

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

    private final MutableLiveData<String> titleData = new MutableLiveData<>(
            getApplication().getString(R.string.rebate_agrt_title)
    );

    @SuppressLint("UseCompatLoadingForDrawables")
    public MutableLiveData<ArrayList<RebateAreegmentModel>> datas = new MutableLiveData<>(new ArrayList<RebateAreegmentModel>(){
        {
            add(new RebateAreegmentModel("真人返水契约", getApplication().getDrawable(R.mipmap.icon_rebateagrt_live), RebateAreegmentTypeEnum.LIVE));
            add(new RebateAreegmentModel("体育返水契约",getApplication().getDrawable(R.mipmap.icon_rebateagrt_sport), RebateAreegmentTypeEnum.SPORT));
            add(new RebateAreegmentModel("棋牌返水契约", getApplication().getDrawable(R.mipmap.icon_rebateagrt_chess), RebateAreegmentTypeEnum.CHESS));
            add(new RebateAreegmentModel("电竞返水契约", getApplication().getDrawable(R.mipmap.icon_rebateagrt_game), RebateAreegmentTypeEnum.EGAME));
            add(new RebateAreegmentModel("时薪", getApplication().getDrawable(R.mipmap.icon_rebateagrt_game), RebateAreegmentTypeEnum.USER));
            add(new RebateAreegmentModel("彩票契约分红", getApplication().getDrawable(R.mipmap.icon_rebateagrt_all), RebateAreegmentTypeEnum.LOTTERIES));
            add(new RebateAreegmentModel("游戏推荐报表", getApplication().getDrawable(R.mipmap.icon_rebateagrt_all), RebateAreegmentTypeEnum.GAMEREPORTS));
            add(new RebateAreegmentModel("彩票推荐报表", getApplication().getDrawable(R.mipmap.icon_rebateagrt_all), RebateAreegmentTypeEnum.LOTTERIESREPORTS));
            add(new RebateAreegmentModel("游戏分红", getApplication().getDrawable(R.mipmap.icon_rebateagrt_all), RebateAreegmentTypeEnum.GAMEREBATE));
        }
    });

    public MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(new ArrayList<Integer>(){
        {
            add(R.layout.item_rebate_agreement);
        }
    });

    public BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {
        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder) {

        }

        @Override
        public void onItemClick(int modelPosition, int layoutPosition) {

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

    @Override
    public void onBack() {
        finish();
    }

    @Override
    public MutableLiveData<String> getTitle() {
        return titleData;
    }

}
