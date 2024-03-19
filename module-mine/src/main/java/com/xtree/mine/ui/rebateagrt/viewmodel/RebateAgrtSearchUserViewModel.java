package com.xtree.mine.ui.rebateagrt.viewmodel;

import android.app.Application;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.drake.brv.BindingAdapter;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.xtree.base.mvvm.model.ToolbarModel;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.mine.R;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.ui.rebateagrt.model.RebateAgrtSearchUserLabelModel;
import com.xtree.mine.ui.rebateagrt.model.RebateAgrtSearchUserResultModel;
import com.xtree.mine.ui.rebateagrt.model.RebateAgrtDetailModel;
import com.xtree.mine.vo.response.GameSubordinateAgrteResponse;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by KAKA on 2024/3/18.
 * Describe:
 */
public class RebateAgrtSearchUserViewModel extends BaseViewModel<MineRepository> implements ToolbarModel, TextWatcher {

    public RebateAgrtSearchUserViewModel(@NonNull Application application) {
        super(application);
    }

    public RebateAgrtSearchUserViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    private RebateAgrtDetailModel searchUsreModel;

    private WeakReference<FragmentActivity> mActivity = null;

    private final MutableLiveData<String> titleData = new MutableLiveData<>();

    public final MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<>(new ArrayList<>());

    public final MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_flex_tag);
                }
            });

    public final RecyclerView.LayoutManager layoutManager = new FlexboxLayoutManager(getApplication());

    public ObservableField<String> pickNums = new ObservableField<>("选中0人");
    public final BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {

        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {
        }

        @Override
        public void onItemClick(int modelPosition, int layoutPosition) {
            //计算数量
            if (datas.getValue() != null) {
                int num = 0;
                for (BindModel bindModel : datas.getValue()) {
                    RebateAgrtSearchUserLabelModel label = (RebateAgrtSearchUserLabelModel) bindModel;
                    if (label.checked.get()) {
                        num++;
                    }
                }
                pickNums.set("选中" + num + "人");
            }
        }
    };

    private final ArrayList<BindModel> bindModels = new ArrayList<BindModel>();
    public void initData(RebateAgrtDetailModel rebateAgrtDetailModel) {
        //init data
        searchUsreModel = rebateAgrtDetailModel;
        initTab();
        List<GameSubordinateAgrteResponse.ChildrenDTO> children = rebateAgrtDetailModel.getSubData().getChildren();
        if (children != null) {
            for (GameSubordinateAgrteResponse.ChildrenDTO child : children) {
                bindModels.add(new RebateAgrtSearchUserLabelModel(child.getUserid(), child.getUsername()));
            }
        }
        datas.setValue(bindModels);
    }

    public void setActivity(FragmentActivity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
    }

    private synchronized void getRebateAgrtCreate() {

    }

    private void initTab() {
        switch (searchUsreModel.getSubData().getType()) {
            case "2": //LIVE
                titleData.setValue("真人返水契约");
                break;
            case "3": //SPORT
                titleData.setValue("体育返水契约");
                break;
            case "5": //CHESS
                titleData.setValue("棋牌返水契约");
                break;
            case "6": // EGAME
                titleData.setValue("电竞返水契约");
                break;
            case "1": //USER
                titleData.setValue("时薪");
                break;
            default:
                break;
        }
    }

    /**
     * 全选/取消 标签
     */
    public void overall() {
        ArrayList<BindModel> value = datas.getValue();
        if (value != null && value.size() > 0) {
            RebateAgrtSearchUserLabelModel itemFirst = (RebateAgrtSearchUserLabelModel) value.get(0);
            boolean isSame = true;
            for (BindModel bindModel : value) {
                RebateAgrtSearchUserLabelModel label = (RebateAgrtSearchUserLabelModel) bindModel;
                if (isSame && label.checked.get() != itemFirst.checked.get()) {
                    isSame = false;
                }
            }
            for (BindModel bindModel : value) {
                RebateAgrtSearchUserLabelModel label = (RebateAgrtSearchUserLabelModel) bindModel;
                if (isSame) {
                    label.checked.set(!label.checked.get());
                } else {
                    label.checked.set(true);
                }
            }
            if (itemFirst.checked.get()) {
                pickNums.set("选中" + value.size() + "人");
            } else {
                pickNums.set("选中" + 0 + "人");
            }
        }
    }

    /**
     * 确认选择
     */
    public void confirmSelection() {
        ArrayList<BindModel> value = datas.getValue();
        if (value != null && value.size() > 0) {
            HashMap<String, String> map = new HashMap<>();
            for (BindModel bindModel : value) {
                RebateAgrtSearchUserLabelModel label = (RebateAgrtSearchUserLabelModel) bindModel;
                if (label.checked.get()) {
                    map.put(label.userId, label.userName);
                }
            }
            RxBus.getDefault().post(new RebateAgrtSearchUserResultModel(map));
            finish();
        } else {
            ToastUtils.show("请选择用户", ToastUtils.ShowType.Fail);
        }
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        //模糊搜索
        String sc = s.toString().trim();
        ArrayList<BindModel> screenModels = new ArrayList<>();
        for (BindModel bindModel : bindModels) {
            RebateAgrtSearchUserLabelModel label = (RebateAgrtSearchUserLabelModel) bindModel;
            if (label.userName.contains(sc)) {
                screenModels.add(label);
            }
        }

        if (sc.isEmpty()) {
            datas.setValue(bindModels);
        } else {
            datas.setValue(screenModels);
        }
    }
}
