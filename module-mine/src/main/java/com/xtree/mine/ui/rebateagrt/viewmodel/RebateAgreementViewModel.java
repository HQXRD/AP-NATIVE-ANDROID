package com.xtree.mine.ui.rebateagrt.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.drake.brv.BindingAdapter;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.mvvm.model.ToolbarModel;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.base.widget.TipDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.ui.rebateagrt.fragment.CommissionsReports2Fragment;
import com.xtree.mine.ui.rebateagrt.fragment.CommissionsReportsFragment;
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
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by KAKA on 2024/3/8.
 * Describe: 返水契约 viewmodel
 */
public class RebateAgreementViewModel extends BaseViewModel<MineRepository> implements ToolbarModel {

    private final MutableLiveData<String> titleData = new MutableLiveData<>(
            getApplication().getString(R.string.rebate_agrt_title)
    );
    private final ArrayList<RebateAreegmentModel> bindModels = new ArrayList<RebateAreegmentModel>() {
        {
            add(new RebateAreegmentModel(RebateAreegmentTypeEnum.LIVE));
            add(new RebateAreegmentModel(RebateAreegmentTypeEnum.SPORT));
            add(new RebateAreegmentModel(RebateAreegmentTypeEnum.CHESS));
            add(new RebateAreegmentModel(RebateAreegmentTypeEnum.EGAME));
            add(new RebateAreegmentModel(RebateAreegmentTypeEnum.USER));
            add(new RebateAreegmentModel(RebateAreegmentTypeEnum.DAYREBATE));
            add(new RebateAreegmentModel(RebateAreegmentTypeEnum.LOTTERIES));
            add(new RebateAreegmentModel(RebateAreegmentTypeEnum.GAMEREPORTS));
            add(new RebateAreegmentModel(RebateAreegmentTypeEnum.LOTTERIESREPORTS));
            add(new RebateAreegmentModel(RebateAreegmentTypeEnum.GAMEREBATE));
            add(new RebateAreegmentModel(RebateAreegmentTypeEnum.COMMISSIONSREPORTS));
        }
    };
    @SuppressLint("UseCompatLoadingForDrawables")
    public MutableLiveData<ArrayList<RebateAreegmentModel>> datas = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(new ArrayList<Integer>() {
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
                    case DAYREBATE:
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
                    case COMMISSIONSREPORTS:

                        int level = SPUtils.getInstance().getInt(SPKeyGlobal.USER_LEVEL);
                        int type = SPUtils.getInstance().getInt(SPKeyGlobal.USER_TYPE);
                        int superAccout = SPUtils.getInstance().getInt(SPKeyGlobal.SUPER_ACCOUNT);

                        //超级总代
                        if (type == 1 && level == 1 && superAccout == 1) {
                            startContainerActivity(CommissionsReports2Fragment.class.getCanonicalName());
                        } else {
                            startContainerActivity(CommissionsReportsFragment.class.getCanonicalName());
                        }
                        break;
                    default:
                        break;
                }
                RxBus.getDefault().postSticky(bindModel);
            }
        }
    };
    private WeakReference<FragmentActivity> mActivity = null;

    public RebateAgreementViewModel(@NonNull Application application) {
        super(application);
    }

    public RebateAgreementViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

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

                        int level = SPUtils.getInstance().getInt(SPKeyGlobal.USER_LEVEL);
                        int type = SPUtils.getInstance().getInt(SPKeyGlobal.USER_TYPE);
                        int superAccout = SPUtils.getInstance().getInt(SPKeyGlobal.SUPER_ACCOUNT);

                        for (int i = 0; i < bindModels.size(); i++) {
                            RebateAreegmentModel raMenu = bindModels.get(i);
                            for (FunctionMenuResponse datum : data) {
                                if (datum != null) {
                                    String id = datum.getId();
                                    if (raMenu.type.getIds().contains(id)) {
                                        //用户等级=2才可以显示佣金报表
                                        if (raMenu.type == RebateAreegmentTypeEnum.COMMISSIONSREPORTS) {
                                            if (type == 1 && (level == 2 || level == 3)) {
                                                newDatas.add(raMenu);
                                            }
                                            //超级总代
                                            if (type == 1 && level == 1 && superAccout == 1) {
                                                raMenu.title = "佣金报表";
                                                newDatas.add(raMenu);
                                            }
                                        } else if (raMenu.type == RebateAreegmentTypeEnum.LOTTERIES) {
                                            //总代和会员不显示彩票报表
                                            if (type == 1 && level != 1) {
                                                newDatas.add(raMenu);
                                            }
                                        } else {
                                            newDatas.add(raMenu);
                                        }
                                        break;
                                    }
                                }
                            }
                        }

                        if (newDatas.size() > 0) {
                            datas.setValue(newDatas);
                        } else {
                            showTip();
                        }

                    }
                });
        addSubscribe(disposable);
    }

    public void setActivity(FragmentActivity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
    }

    @SuppressLint("StaticFieldLeak")
    private BasePopupView pop = null;
    private void showTip() {
        MsgDialog dialog = new MsgDialog(mActivity.get(), getApplication().getString(R.string.txt_kind_tips), "您没有相关契约", true, new TipDialog.ICallBack() {
            @Override
            public void onClickLeft() {

            }

            @Override
            public void onClickRight() {
                if (pop != null) {
                    pop.dismiss();
                    finish();
                }
            }
        });

        pop = new XPopup.Builder(mActivity.get())
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(false)
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
