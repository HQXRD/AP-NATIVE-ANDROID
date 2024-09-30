package com.xtree.mine.ui.rebateagrt.viewmodel;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.drake.brv.BindingAdapter;
import com.xtree.base.mvvm.model.ToolbarModel;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.widget.FilterView;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.ui.rebateagrt.fragment.DividendAgrtCheckDialogFragment;
import com.xtree.mine.ui.rebateagrt.model.DividendAgrtCheckEvent;
import com.xtree.mine.ui.rebateagrt.model.DividendAgrtCheckFoot;
import com.xtree.mine.ui.rebateagrt.model.DividendAgrtCheckModel;
import com.xtree.mine.ui.rebateagrt.model.RebateAgrtCreateAddModel;
import com.xtree.mine.ui.rebateagrt.model.RebateAgrtCreateHeadModel;
import com.xtree.mine.vo.StatusVo;
import com.xtree.mine.vo.request.DividendAgrtCheckRequest;
import com.xtree.mine.vo.request.DividendAgrtCreateRequest;
import com.xtree.mine.vo.response.DividendAgrtCheckResponse;
import com.xtree.mine.vo.response.DividendAgrtCreateResponse;

import org.reactivestreams.Subscription;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.http.BusinessException;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by KAKA on 2024/3/19.
 * Describe: 彩票、游戏契约详情查看
 */
public class DividendAgrtCheckViewModel extends BaseViewModel<MineRepository> implements ToolbarModel {

    public final MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_dividendagrt_check);
                    add(R.layout.item_rebateagrt_create_head);
                    add(R.layout.item_rebateagrt_create_add);
                    add(R.layout.item_dividendagrt_check_foot);
                }
            });
    private final MutableLiveData<String> titleData = new MutableLiveData<>(getApplication().getString(R.string.txt_dividendagrt_title));
    public final MutableLiveData<Boolean> showCreateBtnData = new MutableLiveData<>(false);
    private DividendAgrtCheckEvent event;
    private WeakReference<FragmentActivity> mActivity = null;
    private final RebateAgrtCreateHeadModel headModel = new RebateAgrtCreateHeadModel();
    private final RebateAgrtCreateAddModel addModel = new RebateAgrtCreateAddModel();
    private final ArrayList<BindModel> bindModels = new ArrayList<BindModel>() {{
        headModel.setItemType(1);
        addModel.setItemType(2);
        DividendAgrtCheckFoot footModel = new DividendAgrtCheckFoot();
        footModel.setItemType(3);
        add(addModel);
        add(headModel);
        add(footModel);
    }};
    //可选分红比例
    private ArrayList<FilterView.IBaseVo> ratios = new ArrayList<>();

    public final BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {

        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {
            if (itemViewType == R.layout.item_dividendagrt_check) {
                View deleteView = view.findViewById(R.id.item_delete);
                deleteView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ClickUtil.isFastClick()) {
                            return;
                        }

                        if (bindingViewHolder.getAdapter().getModelCount() <= 1) {
                            ToastUtils.show(getApplication()
                                            .getString(R.string.txt_rebateagrt_tip3),
                                    ToastUtils.ShowType.Fail);
                            return;
                        }

                        int position = bindingViewHolder.getModelPosition();
                        //加上头数据数量
                        bindModels.remove(position + 3);
                        bindingViewHolder.getAdapter().getMutable().remove(position);
                        bindingViewHolder.getAdapter().notifyItemRemoved(position + 2);
                        formatItem();
                    }
                });
            }
        }

        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {

        }

    };

    private final Consumer<DividendAgrtCheckModel> selectRatioConsumer = new Consumer<DividendAgrtCheckModel>() {
        @Override
        public void accept(DividendAgrtCheckModel dividendAgrtCheckModel) throws Exception {
            FilterView.showDialog(mActivity.get(), "分红比例", ratios, new FilterView.ICallBack() {
                @Override
                public void onTypeChanged(FilterView.IBaseVo vo) {
                    dividendAgrtCheckModel.setRatio(vo.getShowId());
                }
            });
        }
    };
    public DividendAgrtCheckViewModel(@NonNull Application application) {
        super(application);
    }

    public DividendAgrtCheckViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    public void initData(DividendAgrtCheckEvent event) {
        //init data
        this.event = event;
        initMode();
        datas.setValue(bindModels);
    }

    private void initMode() {
        if (event.getMode() == 1) {
            //创建模式
            showCreateBtnData.setValue(true);
            headModel.user.set(event.getUserName());
            if (event.getType().isEmpty()) {
                //招商月度佣金不显示添加规则
                addModel.openAdd.set(false);
            } else {
                addModel.openAdd.set(true);
            }
            addModel.setConsumer(v -> {
                addModel();
            });
            //设置契约条目
            if (event.getRules() != null) {
                ratios.clear();
                TypeReference<List<Map<String, List<String>>>> type = new TypeReference<List<Map<String, List<String>>>>() {
                };
                List<Map<String, List<String>>> maps = JSON.parseObject(event.getRules(), type);

                for (Map<String, List<String>> map : maps) {
                    for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                        DividendAgrtCheckModel model = new DividendAgrtCheckModel();
                        model.setRatio(entry.getKey());
                        if (event.getType().isEmpty()) {
                            model.editMode.set(false);
                            model.isShowItem2.set(false);
                            model.isShowDelete.set(false);
                            model.setSelectRatioCallBack(null);
                        } else {
                            model.editMode.set(true);
                            model.isShowItem2.set(true);
                            model.isShowDelete.set(true);
                            model.setSelectRatioCallBack(selectRatioConsumer);
                        }
                        //加入分红比例集
                        ratios.add(new StatusVo(entry.getKey(), entry.getKey()));

                        List<String> value = entry.getValue();
                        if (value.size() > 2) {
                            model.setProfit(value.get(0));
                            model.setPeople(value.get(1));
                            model.setNetProfit(value.get(2));
                        }
                        bindModels.add(model);
                    }
                }
                formatItem();
                datas.setValue(bindModels);
            } else {
                addModel();
            }

        } else {
            //查看模式
            showCreateBtnData.setValue(false);
            addModel.openAdd.set(false);
            getDividendAgrt();
        }
    }

    /**
     * 添加一条规则
     */
    private void addModel() {
        if (ClickUtil.isFastClick()) {
            return;
        }
        DividendAgrtCheckModel model = new DividendAgrtCheckModel();
        model.editMode.set(true);
        model.setSelectRatioCallBack(selectRatioConsumer);
        model.setRatio(ratios.get(0).getShowId());
        bindModels.add(model);
        formatItem();
        datas.setValue(bindModels);
    }

    public void setActivity(FragmentActivity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
    }

    private void formatItem() {
        //设置小标题
        for (int i = 3; i < bindModels.size(); i++) {
            BindModel bind = bindModels.get(i);
            if (bind instanceof DividendAgrtCheckModel) {
                DividendAgrtCheckModel model = (DividendAgrtCheckModel) bind;
                model.numText.set(getApplication().getString(R.string.txt_rules) + (i - 2));
            }
        }
    }

    /**
     * 获取契约详情
     */
    public void getDividendAgrt() {

        if (event == null) {
            ToastUtils.show("数据错误", ToastUtils.ShowType.Fail);
            return;
        }

        DividendAgrtCheckRequest dividendAgrtCheckRequest = new DividendAgrtCheckRequest();
        dividendAgrtCheckRequest.setUserid(event.getUserid());
        dividendAgrtCheckRequest.setType(event.getType());
        Disposable disposable = model.getDividendAgrtData(dividendAgrtCheckRequest)
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        LoadingDialog.show(mActivity.get());
                    }
                })
                .subscribeWith(new HttpCallBack<DividendAgrtCheckResponse>() {
                    @Override
                    public void onResult(DividendAgrtCheckResponse response) {

                        DividendAgrtCheckResponse.DataDTO data = response.getData();
                        if (response != null && data != null) {
                            for (DividendAgrtCheckResponse.DataDTO.RuleDTO ruleDTO : data.getRule()) {
                                DividendAgrtCheckModel dividendAgrtCheckModel = new DividendAgrtCheckModel();
                                dividendAgrtCheckModel.setRatio(ruleDTO.getRatio());
                                dividendAgrtCheckModel.setNetProfit(ruleDTO.getNet_profit());
                                dividendAgrtCheckModel.setProfit(ruleDTO.getProfit());
                                dividendAgrtCheckModel.setPeople(ruleDTO.getPeople());
                                if (dividendAgrtCheckRequest.getType().isEmpty()) {
                                    dividendAgrtCheckModel.isShowItem2.set(false);
                                    dividendAgrtCheckModel.isShowDelete.set(false);
                                    dividendAgrtCheckModel.editMode.set(false);
                                }
                                bindModels.add(dividendAgrtCheckModel);
                            }

                            headModel.user.set(data.getUsername());
                            formatItem();
                            datas.setValue(bindModels);
                        }
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                        ToastUtils.show(t.message, ToastUtils.ShowType.Fail);
                    }
                });
        addSubscribe(disposable);

    }

    /**
     * 创建契约
     */
    public void create() {
        if (ClickUtil.isFastClick()) {
            return;
        }

        DividendAgrtCreateRequest request = new DividendAgrtCreateRequest();

        //用戶
        ArrayList<String> users = new ArrayList<>();
        users.add(event.getUserid());
        request.setUserid(users);

        if (datas.getValue() != null) {
            ArrayList<String> ratioList = new ArrayList<>();
            ArrayList<String> profitList = new ArrayList<>();
            ArrayList<String> peopleList = new ArrayList<>();
            ArrayList<String> netProfitList = new ArrayList<>();
            ArrayList<String> dayPeopleList = new ArrayList<>();
            ArrayList<String> weekPeopleList = new ArrayList<>();

            for (BindModel bindModel : datas.getValue()) {
                if (bindModel instanceof DividendAgrtCheckModel) {
                    DividendAgrtCheckModel model = (DividendAgrtCheckModel) bindModel;
                    if (model.getRatio().isEmpty() || model.getPeople().isEmpty() || model.getProfit().isEmpty() || model.getNetProfit().isEmpty()) {
                        ToastUtils.show(getApplication().getString(R.string.txt_rebateagrt_tip2), ToastUtils.ShowType.Default);
                        return;
                    }
                    ratioList.add(model.getRatio());
                    profitList.add(model.getProfit());
                    peopleList.add(model.getPeople());
                    netProfitList.add(model.getNetProfit());
                    dayPeopleList.add("0");
                    weekPeopleList.add("0");
                }
            }
            request.setRatio(ratioList);
            request.setProfit(profitList);
            request.setPeople(peopleList);
            request.setNet_profit(netProfitList);
            request.setDay_people(dayPeopleList);
            request.setWeek_people(weekPeopleList);
        }
        request.setType(event.getType());

        Disposable disposable = (Disposable) model.getDividendAgrtCreateData(request)
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        LoadingDialog.show(mActivity.get());
                    }
                })
                .subscribeWith(new HttpCallBack<DividendAgrtCreateResponse>() {

                    @Override
                    public void onResult(DividendAgrtCreateResponse response) {
                        if (response != null) {

                            if (response.getMsgDetail() != null) {
                                //创建失败
                                ToastUtils.show(response.getMsgDetail(), ToastUtils.ShowType.Fail);
                            } else {
                                //创建成功
                                ToastUtils.show(response.getSMsg(), ToastUtils.ShowType.Success);
                                finish();
                                //发送完成消息
                                RxBus.getDefault().post(DividendAgrtCheckDialogFragment.CREATED);
                            }
                        }
                    }

                    @Override
                    public void onFail(BusinessException t) {
                        super.onFail(t);
                        ToastUtils.show(t.message, ToastUtils.ShowType.Fail);
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
