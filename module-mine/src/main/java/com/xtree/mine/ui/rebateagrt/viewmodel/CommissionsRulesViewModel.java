package com.xtree.mine.ui.rebateagrt.viewmodel;

import static com.xtree.mine.ui.rebateagrt.dialog.CommissionsRulesDialogFragment.COMMISSIONS_MODE;

import android.app.Application;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.drake.brv.BindingAdapter;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.mine.R;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.ui.rebateagrt.model.CommissionsRulesModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import me.xtree.mvvmhabit.base.BaseViewModel;

/**
 * Created by KAKA on 2024/4/1.
 * Describe:
 */
public class CommissionsRulesViewModel extends BaseViewModel<MineRepository> {

    public final MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_commissions_rules);
                }
            });
    public final BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {
        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {
            TextView tv1 = view.findViewById(R.id.item_tv1);
            TextView tv2 = view.findViewById(R.id.item_tv2);
            TextView tv3 = view.findViewById(R.id.item_tv3);

            tv1.setTextColor(getApplication().getResources().getColor(R.color.clr_white));
            tv2.setTextColor(getApplication().getResources().getColor(R.color.clr_white));
            tv3.setTextColor(getApplication().getResources().getColor(R.color.clr_white));
        }
        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {

        }
    };
    public final MutableLiveData<String> title1 = new MutableLiveData<>();
    public final MutableLiveData<String> title2 = new MutableLiveData<>();
    public final MutableLiveData<String> title3 = new MutableLiveData<>();

    private final ArrayList<BindModel> comModels = new ArrayList<BindModel>() {
        {
            add(new CommissionsRulesModel("2", "0-10,000", "10%"));
            add(new CommissionsRulesModel("3", "10,001-30,000", "15%"));
            add(new CommissionsRulesModel("5", "30,001-50,000", "17%"));
            add(new CommissionsRulesModel("6", "50,001-100,000", "20%"));
            add(new CommissionsRulesModel("8", "100,001-200,000", "23%"));
            add(new CommissionsRulesModel("10", "200,001-300,000", "25%"));
            add(new CommissionsRulesModel("15", "≥300,000", "30%"));
        }
    };
    private final ArrayList<BindModel> agentModels = new ArrayList<BindModel>() {
        {
            add(new CommissionsRulesModel("3", "≥100.000", "2%"));
            add(new CommissionsRulesModel("3", "≥300.000", "4%"));
            add(new CommissionsRulesModel("5", "≥500.000", "5%"));
            add(new CommissionsRulesModel("8", "≥1000.000", "8%"));
            add(new CommissionsRulesModel("10", "≥1500.000", "10%"));
            add(new CommissionsRulesModel("15", "≥2000.000", "12%"));
        }
    };
    public final MutableLiveData<String> title = new MutableLiveData<>();
    private WeakReference<FragmentActivity> mActivity = null;

    public CommissionsRulesViewModel(@NonNull Application application) {
        super(application);
    }

    public CommissionsRulesViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    public void initData(Integer stickyEvent) {
        //init data
        if (stickyEvent == COMMISSIONS_MODE) {
            initCommissionsData();
        } else {
            initAgentData();
        }
    }

    private void initAgentData() {
        title.setValue("全民代理奖励制度");
        title1.setValue("达标（代理）");
        title2.setValue("总亏损");
        title3.setValue("佣金");
        datas.setValue(agentModels);
    }

    private void initCommissionsData() {
        title.setValue("月度佣金制度");
        title1.setValue("玩家活跃要求");
        title2.setValue("总输赢");
        title3.setValue("佣金");
        datas.setValue(comModels);
    }

    public void setActivity(FragmentActivity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mActivity != null) {
            mActivity.clear();
            mActivity = null;
        }
    }
}