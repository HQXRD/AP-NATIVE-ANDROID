package com.xtree.mine.ui.rebateagrt.viewmodel;

import static com.xtree.mine.ui.rebateagrt.fragment.RebateAgrtCreateDialogFragment.CHECK_MODE;
import static com.xtree.mine.ui.rebateagrt.model.RebateAreegmentTypeEnum.CHESS;
import static com.xtree.mine.ui.rebateagrt.model.RebateAreegmentTypeEnum.EGAME;
import static com.xtree.mine.ui.rebateagrt.model.RebateAreegmentTypeEnum.LIVE;
import static com.xtree.mine.ui.rebateagrt.model.RebateAreegmentTypeEnum.SPORT;
import static com.xtree.mine.ui.rebateagrt.model.RebateAreegmentTypeEnum.USER;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableInt;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.drake.brv.BindingAdapter;
import com.xtree.base.mvvm.model.ToolbarModel;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.ui.rebateagrt.fragment.RebateAgrtSearchUserDialogFragment;
import com.xtree.mine.ui.rebateagrt.model.RebateAgrtCreateAddModel;
import com.xtree.mine.ui.rebateagrt.model.RebateAgrtCreateHeadModel;
import com.xtree.mine.ui.rebateagrt.model.RebateAgrtCreateModel;
import com.xtree.mine.ui.rebateagrt.model.RebateAgrtDetailModel;
import com.xtree.mine.ui.rebateagrt.model.RebateAgrtSearchUserResultModel;
import com.xtree.mine.vo.request.RebateAgrtCreateQuery;
import com.xtree.mine.vo.request.RebateAgrtCreateRequest;
import com.xtree.mine.vo.response.GameSubordinateAgrteResponse;
import com.xtree.mine.vo.response.RebateAgrtCreateResponse;

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
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by KAKA on 2024/3/13.
 * Describe: 返水契约创建弹窗 viewmodel
 */
public class RebateAgrtCreateViewModel extends BaseViewModel<MineRepository> implements ToolbarModel {

    public static final int CHECK_MODO = 0;
    public static final int CREATE_MODO = 1;
    public final MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_rebateagrt_create);
                    add(R.layout.item_rebateagrt_create_head);
                    add(R.layout.item_rebateagrt_create_add);
                }
            });
    private final MutableLiveData<String> titleData = new MutableLiveData<>();
    public ObservableInt viewMode = new ObservableInt(CREATE_MODO);
    public MutableLiveData<RebateAgrtSearchUserResultModel> searchUserResultLiveData = new MutableLiveData<>();
    private RebateAgrtDetailModel rebateAgrtDetailModel;
    private WeakReference<FragmentActivity> mActivity = null;
    public final BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {

        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {
            if (itemViewType == R.layout.item_rebateagrt_create) {
                view.findViewById(R.id.item_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (bindingViewHolder.getAdapter().getModelCount() <= 1) {
                            ToastUtils.show(getApplication()
                                            .getString(R.string.txt_rebateagrt_tip3),
                                    ToastUtils.ShowType.Fail);
                            return;
                        }

                        int position = bindingViewHolder.getModelPosition();
                        //加上头数据数量
                        bindModels.remove(position + 2);
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
    private final RebateAgrtCreateHeadModel headModel = new RebateAgrtCreateHeadModel(new Consumer<String>() {
        @Override
        public void accept(String s) throws Exception {
            RebateAgrtSearchUserDialogFragment.show(mActivity.get(), rebateAgrtDetailModel);
        }
    });
    private final RebateAgrtCreateAddModel addModel = new RebateAgrtCreateAddModel(new Consumer<String>() {
        @Override
        public void accept(String s) throws Exception {
            RebateAgrtCreateModel model = new RebateAgrtCreateModel();
            model.setType(rebateAgrtDetailModel.getSubData().getType());
            bindModels.add(model);
            formatItem();
            datas.setValue(bindModels);
        }
    });
    private final ArrayList<BindModel> bindModels = new ArrayList<BindModel>() {{
        headModel.setItemType(1);
        addModel.setItemType(2);
        add(addModel);
        add(headModel);

    }};

    public RebateAgrtCreateViewModel(@NonNull Application application) {
        super(application);
    }

    public RebateAgrtCreateViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    public void initData(RebateAgrtDetailModel response) {
        //init data
        rebateAgrtDetailModel = response;
        initMode();
        initTab();
        formatItem();
        datas.setValue(bindModels);
    }
    private void initMode() {
        if (rebateAgrtDetailModel.getMode() == CHECK_MODE) {
            //查看契约模式
            viewMode.set(CHECK_MODO);
            headModel.editState.set(false);

            for (GameSubordinateAgrteResponse.DataDTO datum : rebateAgrtDetailModel.getSubData().getData()) {
                if (datum.getUserid().equals(rebateAgrtDetailModel.getCheckUserId())) {
                    //设置头部用户名
                    headModel.user.set(datum.getUsername());
                    RebateAgrtSearchUserResultModel resultModel = new RebateAgrtSearchUserResultModel();
                    HashMap<String, String> map = new HashMap<>();
                    map.put(datum.getUserid(), datum.getUsername());
                    resultModel.setUser(map);
                    //设置数据
                    searchUserResultLiveData.setValue(resultModel);

                    //设置规则数据
                    List<GameSubordinateAgrteResponse.DataDTO.RuleDTO> rule = datum.getRule();
                    if (rule != null) {
                        for (GameSubordinateAgrteResponse.DataDTO.RuleDTO ruleDTO : rule) {
                            RebateAgrtCreateModel model = new RebateAgrtCreateModel();
                            model.setType(rebateAgrtDetailModel.getSubData().getType());
                            model.setMinBet(ruleDTO.getMin_bet());
                            model.setMinPlayer(ruleDTO.getMin_player());
                            model.setRatio(ruleDTO.getRatio());
                            bindModels.add(model);
                        }
                    }
                    return;
                }
            }
        } else {
            //创建契约模式
            viewMode.set(CREATE_MODO);
            //设置默认一条空规则
            RebateAgrtCreateModel model = new RebateAgrtCreateModel();
            if (rebateAgrtDetailModel != null && rebateAgrtDetailModel.getSubData() != null) {
                model.setType(rebateAgrtDetailModel.getSubData().getType());
            }
            bindModels.add(model);

            //是否携带用户名
            if (rebateAgrtDetailModel.getCheckUserId() != null) {
                headModel.editState.set(false);
                for (GameSubordinateAgrteResponse.DataDTO datum : rebateAgrtDetailModel.getSubData().getData()) {
                    if (datum.getUserid().equals(rebateAgrtDetailModel.getCheckUserId())) {
                        //设置头部用户名
                        headModel.user.set(datum.getUsername());
                        RebateAgrtSearchUserResultModel resultModel = new RebateAgrtSearchUserResultModel();
                        HashMap<String, String> map = new HashMap<>();
                        map.put(datum.getUserid(), datum.getUsername());
                        resultModel.setUser(map);
                        //设置数据
                        searchUserResultLiveData.setValue(resultModel);
                        return;
                    }
                }
            } else {
                headModel.editState.set(true);
            }
        }
    }

    public void setActivity(FragmentActivity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
    }

    public void setData(RebateAgrtSearchUserResultModel model) {
        searchUserResultLiveData.setValue(model);
        StringBuilder usreNames = new StringBuilder();
        for (Map.Entry<String, String> map : model.getUser().entrySet()) {
            usreNames.append(map.getValue()).append(",");
        }
        usreNames.deleteCharAt(usreNames.lastIndexOf(","));
        headModel.user.set(usreNames.toString());
    }
    private void formatItem() {
        //设置小标题
        for (int i = 0; i < bindModels.size(); i++) {
            BindModel bind = bindModels.get(i);
            if (bind instanceof RebateAgrtCreateModel) {
                RebateAgrtCreateModel model = (RebateAgrtCreateModel) bind;
                model.numText.set(getApplication().getString(R.string.txt_rules) + (i - 1));
            }
        }
    }

    /**
     * 创建/修改契约
     */
    public void create() {

        if (searchUserResultLiveData.getValue() == null || searchUserResultLiveData.getValue().getUser() == null) {
            ToastUtils.show(getApplication().getString(R.string.txt_rebateagrt_tip1), ToastUtils.ShowType.Default);
            return;
        }

        RebateAgrtCreateRequest request = new RebateAgrtCreateRequest();
        StringBuilder usreIds = new StringBuilder();
        for (Map.Entry<String, String> map : searchUserResultLiveData.getValue().getUser().entrySet()) {
            usreIds.append(map.getKey()).append(",");
        }
        usreIds.deleteCharAt(usreIds.lastIndexOf(","));
        request.setUsers(usreIds.toString());

        if (datas.getValue() != null) {
            ArrayList<String> minBetList = new ArrayList<>();
            ArrayList<String> minPlayerList = new ArrayList<>();
            ArrayList<String> ratioList = new ArrayList<>();
            for (BindModel bindModel : datas.getValue()) {
                if (bindModel instanceof RebateAgrtCreateModel) {
                    RebateAgrtCreateModel createModel = (RebateAgrtCreateModel) bindModel;
                    if (createModel.minBet.isEmpty() || createModel.minPlayer.isEmpty() || createModel.ratio.isEmpty()) {
                        ToastUtils.show(getApplication().getString(R.string.txt_rebateagrt_tip2), ToastUtils.ShowType.Default);
                        return;
                    }
                    minBetList.add(createModel.minBet);
                    minPlayerList.add(createModel.minPlayer);
                    ratioList.add(createModel.ratio);
                }
            }
            request.setMin_bet(minBetList);
            request.setMin_player(minPlayerList);
            request.setRatio(ratioList);
        }

        //区分创建或修改
        RebateAgrtCreateQuery query = new RebateAgrtCreateQuery();
        switch (rebateAgrtDetailModel.getSubData().getType()) {
            case "2": //LIVE
            case "3": //SPORT
            case "5": //CHESS
            case "6": // EGAME
            case "1": //USER
                query.setType(rebateAgrtDetailModel.getSubData().getType());
                break;
            default:
                break;
        }
        switch (viewMode.get()) {
            case CHECK_MODO:
                query.setAction("update");
                break;
            case CREATE_MODO:
                query.setAction("create");
                break;
        }

        Disposable disposable = model.getRebateAgrtCreateData(query, request)
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        LoadingDialog.show(mActivity.get());
                    }
                })
                .subscribeWith(new HttpCallBack<RebateAgrtCreateResponse>() {

                    @Override
                    public void onResult(RebateAgrtCreateResponse response) {

                        if (response != null) {

                            if (response.getSMsg() != null) {
                                //创建成功
                                ToastUtils.show(response.getSMsg(), ToastUtils.ShowType.Success);
                                finish();
                            } else {
                                //创建失败
                                ToastUtils.show(response.getMsg_detail(), ToastUtils.ShowType.Fail);
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
    private void initTab() {
        switch (rebateAgrtDetailModel.getSubData().getType()) {
            case "2": //LIVE
                titleData.setValue(LIVE.getName());
                break;
            case "3": //SPORT
                titleData.setValue(SPORT.getName());
                break;
            case "5": //CHESS
                titleData.setValue(CHESS.getName());
                break;
            case "6": // EGAME
                titleData.setValue(EGAME.getName());
                break;
            case "1": //USER
                titleData.setValue(USER.getName());
                break;
            default:
                break;
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
}
